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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Polygonal;
import org.locationtech.jts.operation.union.CascadedPolygonUnion;
import org.xml.sax.SAXException;

import de.topobyte.melon.paths.PathUtil;
import de.topobyte.simplemapfile.core.EntityFile;
import de.topobyte.simplemapfile.xml.SmxFileReader;
import de.topobyte.simplemapfile.xml.SmxFileWriter;

public class MergeForest
{

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException, TransformerException
	{
		if (args.length != 3) {
			System.out.println(
					"usage: merge-forest <dir with smx files> <output file> <buffer distance>");
			System.exit(1);
		}

		Path input = Paths.get(args[0]);
		Path output = Paths.get(args[1]);
		double distance = Double.parseDouble(args[2]);

		System.out.println("loading files...");
		List<Path> files = PathUtil.list(input);
		List<Geometry> geometries = new ArrayList<>();

		for (Path file : files) {
			EntityFile entity = SmxFileReader.read(file);
			geometries.add(entity.getGeometry());
		}
		System.out.println(String.format("loaded %d files", files.size()));

		System.out.println("buffering...");
		geometries = buffer(geometries, distance);

		System.out.println("merging...");
		Geometry union = CascadedPolygonUnion.union(geometries);
		// Geometry union = union(geometries);

		EntityFile result = new EntityFile();
		result.setGeometry(union);
		SmxFileWriter.write(result, output);
	}

	private static List<Geometry> buffer(List<Geometry> geometries,
			double distance)
	{
		List<Geometry> buffered = new ArrayList<>();
		for (Geometry geometry : geometries) {
			buffered.add(geometry.buffer(distance));
		}
		return buffered;
	}

	private static Geometry union(List<Geometry> geometries)
	{
		Geometry union = geometries.get(0);
		for (int i = 1; i < geometries.size(); i++) {
			if (!(union instanceof Polygonal)) {
				union = polygonal((GeometryCollection) union);
			}
			Geometry next = geometries.get(i);
			System.out.println(String.format("%d %s + %s", i,
					union.getClass().getSimpleName(),
					next.getClass().getSimpleName()));
			union = union.union(next);
		}
		return union;
	}

	private static Geometry polygonal(GeometryCollection union)
	{
		GeometryFactory gf = new GeometryFactory();
		List<Polygon> polygons = new ArrayList<>();

		for (int i = 0; i < union.getNumGeometries(); i++) {
			Geometry geometry = union.getGeometryN(i);
			if (geometry instanceof Polygon) {
				polygons.add((Polygon) geometry);
			} else if (geometry instanceof MultiPolygon) {
				MultiPolygon mp = (MultiPolygon) geometry;
				polygons.addAll(polygons(mp));
			}
		}
		return gf.createMultiPolygon(polygons.toArray(new Polygon[0]));
	}

	private static List<Polygon> polygons(MultiPolygon mp)
	{
		List<Polygon> polygons = new ArrayList<>();
		for (int k = 0; k < mp.getNumGeometries(); k++) {
			Geometry pn = mp.getGeometryN(k);
			if (pn instanceof Polygon) {
				polygons.add((Polygon) pn);
			} else if (pn instanceof MultiPolygon) {
				polygons.addAll(polygons((MultiPolygon) pn));
			}
		}
		return polygons;
	}

}
