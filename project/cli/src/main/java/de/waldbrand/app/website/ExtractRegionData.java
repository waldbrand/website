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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.topobyte.melon.commons.io.Resources;
import de.topobyte.osm4j.core.access.OsmIteratorInput;
import de.topobyte.osm4j.core.access.OsmOutputStream;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.osm4j.utils.OsmIoUtils;
import de.topobyte.osm4j.utils.OsmOutputConfig;
import de.topobyte.osm4j.utils.areafilter.RegionFilter;
import de.topobyte.simplemapfile.core.EntityFile;
import de.topobyte.simplemapfile.xml.SmxFileReader;

public class ExtractRegionData
{

	private Path dir;

	public ExtractRegionData(Path dir)
	{
		this.dir = dir;
	}

	public void execute()
			throws IOException, SAXException, ParserConfigurationException
	{
		Path fileInput = dir.resolve("Brandenburg-Berlin.pbf");
		Path fileOutput = dir.resolve("Brandenburg.tbo");
		System.out.println("input: " + fileOutput);
		System.out.println("output: " + fileOutput);

		System.out.println("extracting data...");

		OsmFileInput input = new OsmFileInput(fileInput, FileFormat.PBF);
		OsmIteratorInput osmInput = input.createIterator(true, true);

		EntityFile entity = SmxFileReader
				.read(Resources.stream("Brandenburg.smx"));

		OutputStream os = Files.newOutputStream(fileOutput);
		OsmOutputStream osmOutput = OsmIoUtils.setupOsmOutput(os,
				new OsmOutputConfig(FileFormat.TBO));

		RegionFilter regionFilter = new RegionFilter(osmOutput,
				osmInput.getIterator(), entity.getGeometry(), false);
		regionFilter.run();
	}

}
