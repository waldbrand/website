// Copyright 2022 Sebastian Kuerten
//
// This file is part of waldbrand-website.
//
// waldbrand-website is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// waldbrand-website is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with waldbrand-website. If not, see <http://www.gnu.org/licenses/>.

package de.waldbrand.app.osm.processing.history;

import static de.topobyte.osm4j.core.model.iface.EntityType.Node;
import static de.topobyte.osm4j.core.model.iface.EntityType.Way;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.slimjars.dist.gnu.trove.list.TLongList;
import com.slimjars.dist.gnu.trove.set.TLongSet;
import com.slimjars.dist.gnu.trove.set.hash.TLongHashSet;

import de.topobyte.melon.io.StreamUtil;
import de.topobyte.osm4j.core.access.OsmIterator;
import de.topobyte.osm4j.core.access.OsmIteratorInput;
import de.topobyte.osm4j.core.access.OsmOutputStream;
import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.osm4j.utils.OsmIoUtils;
import de.topobyte.osm4j.utils.OsmOutputConfig;
import de.waldbrand.app.osm.processing.Osm4jUtil;
import de.waldbrand.app.osm.processing.history.merge.SortedMerge;

public class FilterEmergencyHistory
{

	private Path dir;
	private TLongSet nodeIds = new TLongHashSet();

	public FilterEmergencyHistory(Path dir)
	{
		this.dir = dir;
	}

	public void execute() throws IOException
	{
		Path fileInput = dir.resolve("brandenburg-internal.osh.pbf");
		Path fileOutput = dir.resolve("brandenburg-internal-emergency.osh.pbf");
		Path fileOutputNodes = dir
				.resolve("brandenburg-internal-emergency-waynodes.osh.pbf");
		Path fileOutputMerged = dir
				.resolve("brandenburg-internal-emergency-merged.osh.pbf");

		OsmFileInput osmInput = new OsmFileInput(fileInput, FileFormat.PBF);

		filter(osmInput, fileOutput);
		collectNodes(osmInput, fileOutputNodes);
		mergeFiles(fileOutput, fileOutputNodes, fileOutputMerged);
	}

	private void filter(OsmFileInput osmInput, Path fileOutput)
			throws IOException
	{
		OutputStream out = StreamUtil.bufferedOutputStream(fileOutput);
		OsmOutputStream osmOutput = OsmIoUtils.setupOsmOutput(out,
				new OsmOutputConfig(FileFormat.PBF, true));

		OsmIteratorInput iterator = osmInput.createIterator(true, true);
		HistoryIterator historyIterator = new HistoryIterator(
				iterator.getIterator());
		osmOutput.write(iterator.getIterator().getBounds());
		for (List<OsmEntity> versions : historyIterator) {
			if (!anyMatches(versions)) {
				continue;
			}
			for (OsmEntity entity : versions) {
				store(osmOutput, entity);
			}
		}
		osmOutput.complete();
	}

	private void store(OsmOutputStream osmOutput, OsmEntity entity)
			throws IOException
	{
		if (entity.getType() == Way) {
			TLongList members = OsmModelUtil.nodesAsList((OsmWay) entity);
			nodeIds.addAll(members);
		}
		Osm4jUtil.write(osmOutput, entity);
	}

	private static boolean anyMatches(List<OsmEntity> versions)
	{
		for (OsmEntity entity : versions) {
			Map<String, String> tags = OsmModelUtil.getTagsAsMap(entity);
			String emergency = tags.get("emergency");
			if (emergency != null) {
				return true;
			}
		}
		return false;
	}

	private void collectNodes(OsmFileInput osmInput, Path fileOutputNodes)
			throws IOException
	{
		OutputStream out = StreamUtil.bufferedOutputStream(fileOutputNodes);
		OsmOutputStream osmOutput = OsmIoUtils.setupOsmOutput(out,
				new OsmOutputConfig(FileFormat.PBF, true));
		OsmIteratorInput iterator = osmInput.createIterator(true, true);
		for (EntityContainer container : iterator.getIterator()) {
			OsmEntity entity = container.getEntity();
			if (entity.getType() == Node) {
				if (nodeIds.contains(entity.getId())) {
					Osm4jUtil.write(osmOutput, entity);
				}
			}
		}
		osmOutput.complete();
	}

	private void mergeFiles(Path fileOutput, Path fileWayNodes, Path fileMerged)
			throws IOException
	{
		OutputStream out = StreamUtil.bufferedOutputStream(fileMerged);
		OsmOutputStream osmOutput = OsmIoUtils.setupOsmOutput(out,
				new OsmOutputConfig(FileFormat.PBF, true));

		List<OsmIterator> iterators = new ArrayList<>();
		for (Path file : Arrays.asList(fileOutput, fileWayNodes)) {
			OsmFileInput input = new OsmFileInput(file, FileFormat.PBF);
			iterators.add(input.createIterator(true, true).getIterator());
		}

		SortedMerge task = new SortedMerge(osmOutput, iterators);
		task.run();
	}

}
