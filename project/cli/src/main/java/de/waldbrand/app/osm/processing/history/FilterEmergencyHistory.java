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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import de.topobyte.melon.io.StreamUtil;
import de.topobyte.osm4j.core.access.OsmIteratorInput;
import de.topobyte.osm4j.core.access.OsmOutputStream;
import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmRelation;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.osm4j.utils.OsmIoUtils;
import de.topobyte.osm4j.utils.OsmOutputConfig;

public class FilterEmergencyHistory
{

	private Path fileInput;
	private Path fileOutput;

	public FilterEmergencyHistory(Path fileInput, Path fileOutput)
	{
		this.fileInput = fileInput;
		this.fileOutput = fileOutput;
	}

	public void execute() throws IOException
	{
		OutputStream out = StreamUtil.bufferedOutputStream(fileOutput);
		OsmOutputStream osmOutput = OsmIoUtils.setupOsmOutput(out,
				new OsmOutputConfig(FileFormat.PBF, true));

		OsmFileInput osmInput = new OsmFileInput(fileInput, FileFormat.PBF);
		OsmIteratorInput iterator = osmInput.createIterator(true, true);
		HistoryIterator historyIterator = new HistoryIterator(
				iterator.getIterator());
		osmOutput.write(iterator.getIterator().getBounds());
		for (List<OsmEntity> versions : historyIterator) {
			if (!anyMatches(versions)) {
				continue;
			}
			for (OsmEntity entity : versions) {
				switch (entity.getType()) {
				default:
					break;
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
		}
		osmOutput.complete();
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

}
