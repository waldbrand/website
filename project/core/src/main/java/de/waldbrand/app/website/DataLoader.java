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

import static de.topobyte.osm4j.core.model.iface.EntityType.Node;
import static de.topobyte.osm4j.core.model.iface.EntityType.Relation;
import static de.topobyte.osm4j.core.model.iface.EntityType.Way;
import static de.topobyte.osm4j.utils.FileFormat.TBO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import de.topobyte.jts.indexing.GeometryTesselationMap;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.luqe.jdbc.database.SqliteDatabase;
import de.topobyte.melon.resources.Resources;
import de.topobyte.osm4j.core.access.OsmIteratorInput;
import de.topobyte.osm4j.core.dataset.InMemoryListDataSet;
import de.topobyte.osm4j.core.dataset.ListDataSetLoader;
import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.core.resolve.EntityNotFoundException;
import de.topobyte.osm4j.geometry.GeometryBuilder;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.simplemapfile.core.EntityFile;
import de.topobyte.simplemapfile.xml.SmxFileReader;
import de.waldbrand.app.website.lbforst.model.Data;
import de.waldbrand.app.website.lbforst.model.RettungspunktPoi;
import de.waldbrand.app.website.lbforst.model.WesPoi;
import de.waldbrand.app.website.osm.OsmUtil;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.osm.model.OsmPoi;
import lombok.Getter;

public class DataLoader
{

	final static Logger logger = LoggerFactory.getLogger(DataLoader.class);

	@Getter
	private Data data = new Data();

	public void loadData(Path fileWes, Path fileRettungspunkte, Path fileOsm,
			Path fileWaynodes) throws IOException, QueryException
	{
		loadWesData(fileWes);
		loadRettungspunkteData(fileRettungspunkte);
		loadOsmData(fileOsm, fileWaynodes);
		loadKreise();

		process();
	}

	private void loadWesData(Path file) throws QueryException
	{
		SqliteDatabase db = new SqliteDatabase(file);

		Dao dao = new Dao(db.getConnection());
		List<WesPoi> pois = dao.getWesEntries();
		data.setWesPois(pois);

		for (WesPoi poi : pois) {
			data.getIdToWesPoi().put(poi.getId(), poi);
		}

		db.closeConnection(false);
	}

	private void loadRettungspunkteData(Path file) throws QueryException
	{
		SqliteDatabase db = new SqliteDatabase(file);

		Dao dao = new Dao(db.getConnection());
		List<RettungspunktPoi> pois = dao.getRettungspunkteEntries();
		data.setRettungspunktePois(pois);

		for (RettungspunktPoi poi : pois) {
			data.getIdToRettungspunktPoi().put(poi.getId(), poi);
		}

		db.closeConnection(false);
	}

	private void loadOsmData(Path fileData, Path fileWaynodes)
			throws IOException
	{
		OsmFileInput inputWaynodes = new OsmFileInput(fileWaynodes, TBO);
		OsmIteratorInput wayNodesIterator = inputWaynodes.createIterator(false,
				false);
		InMemoryListDataSet nodes = ListDataSetLoader.read(wayNodesIterator,
				false, false, false);

		OsmFileInput inputData = new OsmFileInput(fileData, TBO);
		OsmIteratorInput iterator = inputData.createIterator(true, false);
		for (EntityContainer container : iterator.getIterator()) {
			if (container.getType() == Relation) {
				continue;
			}
			OsmEntity entity = container.getEntity();
			Map<String, String> tags = OsmModelUtil.getTagsAsMap(entity);
			EnumSet<PoiType> types = OsmUtil.classify(tags);

			if (types.isEmpty()) {
				continue;
			}

			OsmPoi poi = null;

			GeometryBuilder gb = new GeometryBuilder();

			if (container.getType() == Node) {
				OsmNode node = (OsmNode) entity;
				poi = new OsmPoi(node, node.getLongitude(), node.getLatitude());
				data.getIdToNodes().put(node.getId(), poi);
			} else if (container.getType() == Way) {
				OsmWay way = (OsmWay) entity;
				try {
					Geometry geometry = gb.build(way, nodes);
					Point centroid = geometry.getCentroid();
					poi = new OsmPoi(way, centroid.getX(), centroid.getY());
					data.getIdToWays().put(way.getId(), poi);
				} catch (EntityNotFoundException e) {
					logger.warn(
							String.format("Error while building way %d (%s)",
									way.getId(), tags));
				}
			}

			if (poi == null) {
				continue;
			}

			for (PoiType type : types) {
				List<OsmPoi> pois = data.getTypeToPois().get(type);
				if (pois == null) {
					pois = new ArrayList<>();
					data.getTypeToPois().put(type, pois);
				}
				pois.add(poi);
			}
		}
	}

	private void loadKreise() throws IOException
	{
		try (InputStream inputList = Resources.stream("kreise/liste");
				Reader reader = new InputStreamReader(inputList);
				BufferedReader br = new BufferedReader(reader)) {
			while (true) {
				String filename = br.readLine();
				if (filename == null) {
					break;
				}
				try (InputStream input = Resources
						.stream("kreise/" + filename)) {
					EntityFile entity = SmxFileReader.read(input);
					data.addKreis(entity);
				} catch (SAXException | ParserConfigurationException e) {
					// continue
				}
			}
		}
	}

	private void process()
	{
		GeometryTesselationMap<String> tesselation = new GeometryTesselationMap<>();
		for (Entry<String, EntityFile> entry : data.getIdToEntity()
				.entrySet()) {
			tesselation.add(entry.getValue().getGeometry(), entry.getKey());
		}
		GeometryFactory gf = new GeometryFactory();
		for (WesPoi poi : data.getWesPois()) {
			Point point = gf.createPoint(poi.getCoordinate());
			Set<String> kreise = tesselation.covering(point);
			if (kreise.isEmpty()) {
				continue;
			}
			String kreis = kreise.iterator().next();
			poi.setKreis(kreis);
		}
	}

}
