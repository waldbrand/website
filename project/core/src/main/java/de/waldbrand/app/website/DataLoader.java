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
import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.simplemapfile.core.EntityFile;
import de.topobyte.simplemapfile.xml.SmxFileReader;
import de.waldbrand.app.website.lbforst.model.Data;
import de.waldbrand.app.website.lbforst.model.Poi;
import de.waldbrand.app.website.osm.OsmUtil;
import de.waldbrand.app.website.osm.PoiType;
import lombok.Getter;

public class DataLoader
{

	final static Logger logger = LoggerFactory.getLogger(DataLoader.class);

	@Getter
	private Data data = new Data();

	public void loadData(Path fileWes, Path fileOsm)
			throws IOException, QueryException
	{
		loadWesData(fileWes);
		loadOsmData(fileOsm);
		loadKreise();

		process();
	}

	private void loadWesData(Path file) throws QueryException
	{
		SqliteDatabase db = new SqliteDatabase(file);

		Dao dao = new Dao(db.getConnection());
		List<Poi> pois = dao.getEntries();
		data.setPois(pois);

		for (Poi poi : pois) {
			data.getIdToPoi().put(poi.getId(), poi);
		}

		db.closeConnection(false);
	}

	private void loadOsmData(Path fileOsm) throws IOException
	{
		OsmFileInput osmFile = new OsmFileInput(fileOsm, FileFormat.TBO);
		OsmIteratorInput iterator = osmFile.createIterator(true, false);
		for (EntityContainer container : iterator.getIterator()) {
			if (container.getType() != EntityType.Node) {
				continue;
			}
			OsmNode node = (OsmNode) container.getEntity();
			Map<String, String> tags = OsmModelUtil.getTagsAsMap(node);

			EnumSet<PoiType> types = OsmUtil.classify(tags);
			for (PoiType type : types) {
				List<OsmNode> nodes = data.getTypeToNodes().get(type);
				if (nodes == null) {
					nodes = new ArrayList<>();
					data.getTypeToNodes().put(type, nodes);
				}
				nodes.add(node);
				data.getIdToNodes().put(node.getId(), node);
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
		for (Poi poi : data.getPois()) {
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
