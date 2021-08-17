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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.operation.distance.DistanceOp;
import org.xml.sax.SAXException;

import de.topobyte.geomath.WGS84;
import de.topobyte.osm4j.core.access.OsmIteratorInput;
import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.geometry.GeometryBuilder;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.simplemapfile.core.EntityFile;
import de.topobyte.simplemapfile.xml.SmxFileReader;

public class PrintHydrantForestDistanceStats
{

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException, TransformerException
	{
		if (args.length != 2) {
			System.out.println(
					"usage: print-hydrant-forest-distance-stats <forest.smx> <osm-file.tbo>");
			System.exit(1);
		}

		Path inputForest = Paths.get(args[0]);
		Path input = Paths.get(args[1]);

		EntityFile forest = SmxFileReader.read(inputForest);
		Geometry geometry = forest.getGeometry();

		GeometryBuilder gb = new GeometryBuilder();

		List<Double> distances = new ArrayList<>();

		OsmFileInput fileInput = new OsmFileInput(input, FileFormat.TBO);
		OsmIteratorInput iterator = fileInput.createIterator(true, false);
		for (EntityContainer ec : iterator.getIterator()) {
			if (ec.getType() != EntityType.Node) {
				continue;
			}

			OsmNode node = (OsmNode) ec.getEntity();
			Map<String, String> tags = OsmModelUtil.getTagsAsMap(node);

			String emergency = tags.get("emergency");
			if ("fire_hydrant".equals(emergency)) {
				String type = tags.get("fire_hydrant:type");
				if ("underground".equals(type)) {
					Point point = gb.build(node);
					DistanceOp distanceOp = new DistanceOp(geometry, point);
					Coordinate[] nearest = distanceOp.nearestPoints();
					double meters = WGS84.haversineDistance(nearest[0].x,
							nearest[0].y, nearest[1].x, nearest[1].y);
					distances.add(meters);
				}
			}
		}

		Collections.sort(distances);
		int n = 10;
		double fraction = distances.size() / n;
		for (int i = 0; i < n; i++) {
			int pos1 = (int) Math.round((i) * fraction);
			int pos2 = (int) Math.round((i + 1) * fraction);
			if (i < n - 1) {
				pos2 -= 1;
			}
			double m1 = distances.get(pos1);
			double m2 = distances.get(pos2);
			System.out
					.println(String.format("%d: %d-%d between %.1f, and %.1fm",
							i + 1, pos1, pos2, m1, m2));
		}
	}

}
