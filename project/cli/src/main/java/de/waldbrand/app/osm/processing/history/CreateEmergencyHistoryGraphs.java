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

import java.awt.Color;
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

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.xml.sax.SAXException;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.slimjars.dist.gnu.trove.map.TLongObjectMap;

import de.topobyte.collections.util.ListUtil;
import de.topobyte.melon.commons.io.Resources;
import de.topobyte.osm4j.core.access.OsmIteratorInput;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.iface.OsmMetadata;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmTag;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.core.resolve.EntityNotFoundException;
import de.topobyte.osm4j.geometry.GeometryBuilder;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.simplemapfile.core.EntityFile;
import de.topobyte.simplemapfile.xml.SmxFileReader;
import de.waldbrand.app.osm.processing.Tag;

public class CreateEmergencyHistoryGraphs
{

	private static Set<String> emergencyValues = new TreeSet<>();
	static {
		emergencyValues.addAll(Arrays.asList("suction_point", "fire_hydrant",
				"fire_water_pond", "water_tank"));
	}

	private static Map<String, String> names = new HashMap<>();
	static {
		names.put("suction_point", "Saugstellen");
		names.put("fire_hydrant", "Hydranten");
		names.put("fire_water_pond", "Feuerlöschteiche");
		names.put("water_tank", "Zisternen");
	}

	private Path input;
	private Path dir;

	private List<LocalDate> dates = new ArrayList<>();
	private Map<LocalDate, Multiset<OsmTag>> allCounts = new HashMap<>();
	private PreparedGeometry boundary;

	private TLongObjectMap<List<OsmNode>> nodeLookup;

	public CreateEmergencyHistoryGraphs(Path input, Path dir)
	{
		this.input = input;
		this.dir = dir;
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

		nodeLookup = HistoryUtils.buildNodeLookup(fileInput);
		processData(fileInput);

		createCharts();
	}

	private void processData(OsmFileInput fileInput) throws IOException
	{
		OsmIteratorInput iterator = fileInput.createIterator(true, true);
		HistoryIterator historyIterator = new HistoryIterator(
				iterator.getIterator());
		for (List<OsmEntity> versions : historyIterator) {
			processVersions(versions);
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
							nodeLookup, metadata.getTimestamp());
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

	private void createCharts() throws IOException
	{
		for (String value : emergencyValues) {
			Path image = dir.resolve(value + ".png");

			String seriesName = names.get(value);

			DefaultCategoryDataset dataset = new DefaultCategoryDataset();

			for (LocalDate date : dates) {
				StringBuilder strb = new StringBuilder();
				strb.append(date.toString());
				Multiset<OsmTag> counts = allCounts.get(date);
				int count = counts.count(new Tag("emergency", value));
				dataset.addValue(count, seriesName, date);
			}

			CategoryAxis categoryAxis = new CategoryAxis("Monat");
			ValueAxis valueAxis = new NumberAxis("Anzahl");

			LineAndShapeRenderer renderer = new LineAndShapeRenderer(true,
					false);
			CategoryPlot plot = new CategoryPlot(dataset, categoryAxis,
					valueAxis, renderer);

			categoryAxis
					.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

			String title = String.format("%s (emergency=%s)", names.get(value),
					value);

			CategoryMarker marker = new CategoryMarker(
					LocalDate.of(2021, 4, 1));
			marker.setPaint(Color.BLUE);
			marker.setDrawAsLine(true);
			plot.addDomainMarker(marker);

			JFreeChart chart = new JFreeChart(title,
					JFreeChart.DEFAULT_TITLE_FONT, plot, true);

			ChartUtils.saveChartAsPNG(image.toFile(), chart, 1000, 800);
		}
	}

}
