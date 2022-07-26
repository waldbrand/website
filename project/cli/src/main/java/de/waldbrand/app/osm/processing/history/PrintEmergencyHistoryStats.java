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
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.xml.sax.SAXException;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.slimjars.dist.gnu.trove.map.TLongObjectMap;
import com.slimjars.dist.gnu.trove.map.hash.TLongObjectHashMap;

import de.topobyte.collections.util.ListUtil;
import de.topobyte.melon.commons.io.Resources;
import de.topobyte.osm4j.core.access.OsmIteratorInput;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.iface.OsmMetadata;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmRelation;
import de.topobyte.osm4j.core.model.iface.OsmTag;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.core.resolve.EntityNotFoundException;
import de.topobyte.osm4j.core.resolve.OsmEntityProvider;
import de.topobyte.osm4j.geometry.GeometryBuilder;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.simplemapfile.core.EntityFile;
import de.topobyte.simplemapfile.xml.SmxFileReader;
import de.waldbrand.app.osm.processing.Tag;

public class PrintEmergencyHistoryStats
{

	private static Set<String> emergencyValues = new TreeSet<>();
	static {
		emergencyValues.addAll(Arrays.asList("suction_point", "fire_hydrant",
				"fire_water_pond", "water_tank"));
	}

	private Path input;

	private List<LocalDate> dates = new ArrayList<>();
	private Map<LocalDate, Multiset<OsmTag>> allCounts = new HashMap<>();
	private PreparedGeometry boundary;

	private TLongObjectMap<List<OsmNode>> nodeLookup = new TLongObjectHashMap<>();

	public PrintEmergencyHistoryStats(Path input)
	{
		this.input = input;
	}

	public void execute()
			throws IOException, ParserConfigurationException, SAXException
	{
		dates.add(LocalDate.of(2011, 1, 1));
		while (true) {
			LocalDate date = ListUtil.last(dates).plusMonths(3);
			dates.add(date);
			if (date.isAfter(LocalDate.now())) {
				break;
			}
		}

		for (LocalDate date : dates) {
			allCounts.put(date, HashMultiset.create());
		}

		EntityFile boundaryFile;
		try (InputStream is = Resources.stream("Brandenburg.smx")) {
			boundaryFile = SmxFileReader.read(is);
		}
		Geometry unpreparedBoundary = boundaryFile.getGeometry();
		boundary = new PreparedGeometryFactory().create(unpreparedBoundary);

		OsmFileInput fileInput = new OsmFileInput(input, FileFormat.PBF);

		buildNodeLookup(fileInput);
		processData(fileInput);
	}

	private void buildNodeLookup(OsmFileInput fileInput) throws IOException
	{
		OsmIteratorInput iterator = fileInput.createIterator(true, true);
		HistoryIterator historyIterator = new HistoryIterator(
				iterator.getIterator());
		for (List<OsmEntity> versions : historyIterator) {
			OsmEntity first = versions.get(0);
			if (first.getType() != EntityType.Node) {
				return;
			}
			OsmNode firstNode = (OsmNode) first;
			long id = firstNode.getId();
			List<OsmNode> nodes = new ArrayList<>();
			for (OsmEntity entity : versions) {
				nodes.add((OsmNode) entity);
			}
			nodeLookup.put(id, nodes);
		}
	}

	private void processData(OsmFileInput fileInput) throws IOException
	{
		OsmIteratorInput iterator = fileInput.createIterator(true, true);
		HistoryIterator historyIterator = new HistoryIterator(
				iterator.getIterator());
		for (List<OsmEntity> versions : historyIterator) {
			processVersions(versions);
		}

		StringBuilder header = new StringBuilder();
		header.append("date");
		for (String value : emergencyValues) {
			header.append(",");
			header.append(value);
		}
		System.out.println(header);

		for (LocalDate date : dates) {
			StringBuilder strb = new StringBuilder();
			strb.append(date.toString());
			Multiset<OsmTag> counts = allCounts.get(date);
			for (String value : emergencyValues) {
				int count = counts.count(new Tag("emergency", value));
				strb.append(",");
				strb.append(count);
			}
			System.out.println(strb.toString());
		}
	}

	private void processVersions(List<OsmEntity> versions)
	{
		GeometryBuilder builder = new GeometryBuilder();
		for (LocalDate d : dates) {
			OsmEntity entity = HistoryUtils.findLatestVersionUntil(versions, d);
			if (entity == null) {
				continue;
			}

			Map<String, String> tags = OsmModelUtil.getTagsAsMap(entity);
			OsmMetadata metadata = entity.getMetadata();
			String emergency = tags.get("emergency");
			if (emergency == null || !emergencyValues.contains(emergency)) {
				continue;
			}
			if (!metadata.isVisible()) {
				continue;
			}
			if (emergency != null) {
				if (entity.getType() == EntityType.Node) {
					OsmNode node = (OsmNode) entity;
					if (!boundary.contains(builder.build(node))) {
						continue;
					}
				} else if (entity.getType() == EntityType.Way) {
					OsmWay way = (OsmWay) entity;
					EntityProviderHistory entityProvider = new EntityProviderHistory(
							metadata.getTimestamp());
					try {
						if (!boundary
								.contains(builder.build(way, entityProvider))) {
							continue;
						}
					} catch (EntityNotFoundException e) {
						continue;
					}
				}
				Tag tag = new Tag("emergency", emergency);
				Multiset<OsmTag> counts = allCounts.get(d);
				counts.add(tag);
			}
		}
	}

	private class EntityProviderHistory implements OsmEntityProvider
	{

		private long timestamp;

		public EntityProviderHistory(long timestamp)
		{
			this.timestamp = timestamp;
		}

		@Override
		public OsmNode getNode(long id) throws EntityNotFoundException
		{
			List<OsmNode> nodes = nodeLookup.get(id);
			if (nodes == null) {
				throw new EntityNotFoundException("not found");
			}
			OsmNode best = nodes.get(0);
			for (int i = 1; i < nodes.size(); i++) {
				OsmNode node = nodes.get(i);
				if (node.getMetadata().getTimestamp() <= timestamp) {
					best = node;
				}
			}
			return best;
		}

		@Override
		public OsmWay getWay(long id) throws EntityNotFoundException
		{
			throw new EntityNotFoundException("not found");
		}

		@Override
		public OsmRelation getRelation(long id) throws EntityNotFoundException
		{
			throw new EntityNotFoundException("not found");
		}

	}

}
