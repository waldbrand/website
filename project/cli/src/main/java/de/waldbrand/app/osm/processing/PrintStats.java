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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import de.topobyte.osm4j.core.access.OsmIteratorInput;
import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.iface.OsmTag;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmFileInput;

public class PrintStats
{

	private static Set<String> emergencyValues = new HashSet<>();
	static {
		emergencyValues.addAll(Arrays.asList("suction_point", "fire_hydrant",
				"fire_water_pond", "water_tank"));
	}

	public static void main(String[] args) throws IOException
	{
		if (args.length != 1) {
			System.out.println("usage: print-stats <osm-file.tbo>");
			System.exit(1);
		}

		Path input = Paths.get(args[0]);

		Map<EntityType, Multiset<OsmTag>> counts = new HashMap<>();
		for (EntityType type : EntityType.values()) {
			counts.put(type, HashMultiset.create());
		}
		Multiset<OsmTag> totals = HashMultiset.create();

		OsmFileInput fileInput = new OsmFileInput(input, FileFormat.TBO);
		OsmIteratorInput iterator = fileInput.createIterator(true, false);
		for (EntityContainer ec : iterator.getIterator()) {
			OsmEntity entity = ec.getEntity();
			Map<String, String> tags = OsmModelUtil.getTagsAsMap(entity);

			String emergency = tags.get("emergency");
			if (emergency != null && emergencyValues.contains(emergency)) {
				Tag tag = new Tag("emergency", emergency);
				counts.get(ec.getType()).add(tag);
				totals.add(tag);
			}
		}
		for (EntityType type : EntityType.values()) {
			System.out.println(String.format("## %s:", type));
			Multiset<OsmTag> set = counts.get(type);
			print(set);
		}

		System.out.println("## Insgesamt:");
		print(totals);
	}

	private static void print(Multiset<OsmTag> set)
	{
		if (set.isEmpty()) {
			System.out.println("* keine");
		}
		for (OsmTag tag : set.elementSet()) {
			int count = set.count(tag);
			System.out.println(String.format("* %d: %s", count, tag));
		}

	}

}
