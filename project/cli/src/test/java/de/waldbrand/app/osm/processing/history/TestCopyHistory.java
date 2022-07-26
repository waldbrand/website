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

import de.topobyte.melon.io.StreamUtil;
import de.topobyte.osm4j.core.access.OsmIteratorInput;
import de.topobyte.osm4j.core.access.OsmOutputStream;
import de.topobyte.osm4j.core.access.wrapper.OsmElementCounterIteratorAdapter;
import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmRelation;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.osm4j.utils.OsmIoUtils;
import de.topobyte.osm4j.utils.OsmOutputConfig;
import de.topobyte.system.utils.SystemPaths;

//TODO: this could go into osm4j
public class TestCopyHistory
{

	public static void main(String[] args) throws IOException
	{
		Path fileInput = SystemPaths.HOME
				.resolve("Downloads/liechtenstein-internal.osh.pbf");
		Path fileOutput = SystemPaths.HOME
				.resolve("Downloads/liechtenstein-internal-copy.osh.pbf");

		OutputStream out = StreamUtil.bufferedOutputStream(fileOutput);
		OsmOutputStream osmOutput = OsmIoUtils.setupOsmOutput(out,
				new OsmOutputConfig(FileFormat.PBF, true));

		OsmFileInput osmOutputFile = new OsmFileInput(fileOutput,
				FileFormat.PBF);

		OsmFileInput osmInputFile = new OsmFileInput(fileInput, FileFormat.PBF);
		OsmIteratorInput iterator = osmInputFile.createIterator(true, true);
		HistoryIterator historyIterator = new HistoryIterator(
				iterator.getIterator());
		osmOutput.write(iterator.getIterator().getBounds());
		for (List<OsmEntity> versions : historyIterator) {
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

		OsmElementCounterIteratorAdapter counter1 = new OsmElementCounterIteratorAdapter(
				osmInputFile.createIterator(true, false).getIterator());
		counter1.count();
		System.out.println(String.format("%d, %d, %d",
				counter1.getNumberOfNodes(), counter1.getNumberOfWays(),
				counter1.getNumberOfRelations()));

		OsmElementCounterIteratorAdapter counter2 = new OsmElementCounterIteratorAdapter(
				osmOutputFile.createIterator(true, false).getIterator());
		counter2.count();
		System.out.println(String.format("%d, %d, %d",
				counter2.getNumberOfNodes(), counter2.getNumberOfWays(),
				counter2.getNumberOfRelations()));
	}

}
