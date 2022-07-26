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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.xml.sax.SAXException;

import com.slimjars.dist.gnu.trove.set.TLongSet;
import com.slimjars.dist.gnu.trove.set.hash.TLongHashSet;

import de.topobyte.jts.indexing.GeometryTesselationMap;
import de.topobyte.jts.utils.PolygonHelper;
import de.topobyte.melon.commons.io.Resources;
import de.topobyte.osm4j.core.access.OsmIteratorInput;
import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.iface.OsmRelation;
import de.topobyte.osm4j.core.model.iface.OsmRelationMember;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.core.resolve.EntityNotFoundException;
import de.topobyte.osm4j.core.resolve.OsmEntityProvider;
import de.topobyte.osm4j.geometry.GeometryBuilder;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.simplemapfile.core.EntityFile;
import de.topobyte.simplemapfile.xml.SmxFileReader;
import de.topobyte.simplemapfile.xml.SmxFileWriter;

public class RegionExtractor
{

	private Path input;
	private Map<String, Path> mapping;
	private boolean useWays;

	private EntityDbs entityDbs;

	private GeometryTesselationMap<Path> geometryToDir = new GeometryTesselationMap<>(
			true);

	public RegionExtractor(Path input, Map<String, Path> mapping,
			boolean useWays)
	{
		this.input = input;
		this.mapping = mapping;
		this.useWays = useWays;
	}

	public void prepare()
			throws IOException, ParserConfigurationException, SAXException
	{
		Path dir = Files.createTempDirectory("waldbrand-osm");
		entityDbs = new EntityDbs(dir);
		entityDbs.init(input);

		for (String resource : mapping.keySet()) {
			Path dirOutput = mapping.get(resource);
			try (InputStream input = Resources.stream(resource)) {
				EntityFile ef = SmxFileReader.read(input);
				Geometry buffer = ef.getGeometry().buffer(0.001);
				geometryToDir.add(buffer, dirOutput);
			}
		}
	}

	private GeometryBuilder geometryBuilder = new GeometryBuilder();
	private OsmEntityProvider entityProvider;

	// record way ids used for relation polygon building
	private TLongSet usedWays = new TLongHashSet();

	public void extract(Function<Map<String, String>, Boolean> selector,
			Function<OsmEntity, String> namer) throws IOException,
			ParserConfigurationException, TransformerException
	{
		// setup output directory
		for (Path dirOutput : mapping.values()) {
			Files.createDirectories(dirOutput);
		}

		// iterate data
		geometryBuilder.getRegionBuilder().setIncludePuntal(false);
		geometryBuilder.getRegionBuilder().setIncludeLineal(false);

		entityProvider = entityDbs.entityProvider();

		relations(selector, namer);
		ways(selector, namer);
	}

	private void relations(Function<Map<String, String>, Boolean> selector,
			Function<OsmEntity, String> namer) throws TransformerException,
			ParserConfigurationException, IOException
	{
		OsmFileInput fileInput = new OsmFileInput(input, FileFormat.TBO);
		OsmIteratorInput iterator = fileInput.createIterator(true, false);
		for (EntityContainer ec : iterator.getIterator()) {
			if (ec.getType() != EntityType.Relation) {
				continue;
			}
			OsmRelation relation = (OsmRelation) ec.getEntity();
			Map<String, String> tags = OsmModelUtil.getTagsAsMap(relation);

			if (!selector.apply(tags)) {
				continue;
			}

			if (useWays) {
				for (OsmRelationMember member : OsmModelUtil
						.membersAsList(relation)) {
					if (member.getType() == EntityType.Way) {
						usedWays.add(member.getId());
					}
				}
			}

			Geometry geometry;
			try {
				geometry = geometryBuilder.build(relation, entityProvider);
			} catch (EntityNotFoundException e) {
				continue;
			}

			Set<Path> dirsOutput = geometryToDir.covering(geometry);
			if (dirsOutput.isEmpty()) {
				continue;
			}

			if (geometry instanceof MultiPolygon) {
				geometry = PolygonHelper
						.unpackMultipolygon((MultiPolygon) geometry);
			}

			for (Path dirOutput : dirsOutput) {
				write(dirOutput, geometry, relation, tags, namer);
			}
		}
	}

	private void ways(Function<Map<String, String>, Boolean> selector,
			Function<OsmEntity, String> namer) throws IOException,
			TransformerException, ParserConfigurationException
	{
		OsmFileInput fileInput = new OsmFileInput(input, FileFormat.TBO);
		OsmIteratorInput iterator = fileInput.createIterator(true, false);
		for (EntityContainer ec : iterator.getIterator()) {
			if (ec.getType() != EntityType.Way) {
				continue;
			}
			OsmWay way = (OsmWay) ec.getEntity();
			Map<String, String> tags = OsmModelUtil.getTagsAsMap(way);

			if (!selector.apply(tags)) {
				continue;
			}
			if (usedWays.contains(way.getId())) {
				continue;
			}

			Geometry geometry;
			try {
				geometry = geometryBuilder.build(way, entityProvider);
			} catch (EntityNotFoundException e) {
				continue;
			}

			if (!(geometry instanceof LinearRing)) {
				continue;
			}

			Set<Path> dirsOutput = geometryToDir.covering(geometry);
			if (dirsOutput.isEmpty()) {
				continue;
			}

			geometry = geometryBuilder.getGeometryFactory()
					.createPolygon((LinearRing) geometry);

			for (Path dirOutput : dirsOutput) {
				write(dirOutput, geometry, way, tags, namer);
			}
		}
	}

	private void write(Path dirOutput, Geometry geometry, OsmEntity entity,
			Map<String, String> tags, Function<OsmEntity, String> namer)
			throws TransformerException, ParserConfigurationException,
			IOException
	{
		EntityFile entityFile = new EntityFile();
		entityFile.setGeometry(geometry);
		for (Entry<String, String> tag : tags.entrySet()) {
			entityFile.addTag(tag.getKey(), tag.getValue());
		}

		String name = namer.apply(entity);
		Path file = dirOutput.resolve(name);
		SmxFileWriter.write(entityFile, file);
	}

}
