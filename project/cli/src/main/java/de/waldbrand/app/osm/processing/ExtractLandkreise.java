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

package de.waldbrand.app.osm.processing;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.system.utils.SystemPaths;

public class ExtractLandkreise
{

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException, TransformerException
	{
		if (args.length != 1) {
			System.out.println("usage: extract-landkreise <osm-file.tbo>");
			System.exit(1);
		}

		Path input = Paths.get(args[0]);

		Path dirData = SystemPaths.CWD.resolve("data");
		Path dirKreise = dirData.resolve("kreise");

		Map<String, Path> mapping = new HashMap<>();
		mapping.put("Brandenburg.smx", dirKreise);

		RegionExtractor regionExtractor = new RegionExtractor(input, mapping,
				false);
		regionExtractor.prepare();
		regionExtractor.extract(tags -> {
			String adminLevel = tags.get("admin_level");
			if (!"6".equals(adminLevel)) {
				return false;
			}
			return true;
		}, entity -> {
			Map<String, String> tags = OsmModelUtil.getTagsAsMap(entity);
			String name = tags.get("name");
			return String.format("%s.smx", name);
		});
	}

}
