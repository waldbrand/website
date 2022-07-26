// Copyright 2021 Sebastian Kuerten
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

package de.waldbrand.app.website;

import static de.topobyte.osm4j.core.model.iface.EntityType.Node;
import static de.topobyte.osm4j.core.model.iface.EntityType.Way;
import static de.topobyte.osm4j.core.model.util.OsmModelUtil.getTagsAsMap;

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
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmRelation;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.osm4j.utils.OsmIoUtils;
import de.topobyte.osm4j.utils.OsmOutputConfig;
import de.topobyte.osm4j.utils.merge.sorted.SortedMerge;

public class ExtractOsmData
{

	private Path dir;
	private TLongSet nodeIds = new TLongHashSet();

	public ExtractOsmData(Path dir)
	{
		this.dir = dir;
	}

	public void execute() throws IOException
	{
		Path fileInput = dir.resolve("Brandenburg.tbo");
		Path fileOutput = dir.resolve("emergency.tbo");
		Path fileWayNodes = dir.resolve("emergency-waynodes.tbo");
		Path fileMerged = dir.resolve("emergency-merged.tbo");
		System.out.println("input: " + fileInput);
		System.out.println("emergency: " + fileOutput);
		System.out.println("emergency waynodes: " + fileWayNodes);

		OsmFileInput inputFile = new OsmFileInput(fileInput, FileFormat.TBO);
		System.out.println("extracting data...");
		filter(inputFile, fileOutput);
		System.out.println("collecting waynodes...");
		collectNodes(inputFile, fileWayNodes);
		System.out.println("merging files...");
		mergeFiles(fileOutput, fileWayNodes, fileMerged);
		System.out.println("done");
	}

	private void filter(OsmFileInput inputFile, Path fileOutput)
			throws IOException
	{
		OutputStream out = StreamUtil.bufferedOutputStream(fileOutput);
		OsmOutputStream osmOutput = OsmIoUtils.setupOsmOutput(out,
				new OsmOutputConfig(FileFormat.TBO, true));
		OsmIteratorInput iterator = inputFile.createIterator(true, true);
		for (EntityContainer container : iterator.getIterator()) {
			OsmEntity entity = container.getEntity();
			Map<String, String> tags = getTagsAsMap(entity);
			if (tags.get("emergency") != null) {
				store(osmOutput, entity);
			}
		}
		osmOutput.complete();
	}

	private void collectNodes(OsmFileInput inputFile, Path fileOutput)
			throws IOException
	{
		OutputStream out = StreamUtil.bufferedOutputStream(fileOutput);
		OsmOutputStream osmOutput = OsmIoUtils.setupOsmOutput(out,
				new OsmOutputConfig(FileFormat.TBO, true));
		OsmIteratorInput iterator = inputFile.createIterator(true, true);
		for (EntityContainer container : iterator.getIterator()) {
			OsmEntity entity = container.getEntity();
			if (entity.getType() == Node) {
				if (nodeIds.contains(entity.getId())) {
					write(osmOutput, entity);
				}
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
		write(osmOutput, entity);
	}

	private void write(OsmOutputStream osmOutput, OsmEntity entity)
			throws IOException
	{
		switch (entity.getType()) {
		case Node:
			osmOutput.write((OsmNode) entity);
			break;
		case Way:
			osmOutput.write((OsmWay) entity);
			break;
		case Relation:
			osmOutput.write((OsmRelation) entity);
			break;
		}
	}

	private void mergeFiles(Path fileOutput, Path fileWayNodes, Path fileMerged)
			throws IOException
	{
		OutputStream out = StreamUtil.bufferedOutputStream(fileMerged);
		OsmOutputStream osmOutput = OsmIoUtils.setupOsmOutput(out,
				new OsmOutputConfig(FileFormat.TBO, true));

		List<OsmIterator> iterators = new ArrayList<>();
		for (Path file : Arrays.asList(fileOutput, fileWayNodes)) {
			OsmFileInput input = new OsmFileInput(file, FileFormat.TBO);
			iterators.add(input.createIterator(true, true).getIterator());
		}

		SortedMerge task = new SortedMerge(osmOutput, iterators);
		task.run();
	}

}
