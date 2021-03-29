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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import de.topobyte.luqe.iface.QueryException;
import de.topobyte.luqe.jdbc.database.SqliteDatabase;
import de.topobyte.melon.resources.Resources;
import de.topobyte.simplemapfile.core.EntityFile;
import de.topobyte.simplemapfile.xml.SmxFileReader;
import de.waldbrand.app.website.model.Data;
import de.waldbrand.app.website.model.Poi;
import lombok.Getter;

public class DataLoader
{

	final static Logger logger = LoggerFactory.getLogger(DataLoader.class);

	@Getter
	private Data data = new Data();

	public void loadData(Path file) throws IOException, QueryException
	{
		SqliteDatabase db = new SqliteDatabase(file);

		Dao dao = new Dao(db.getConnection());
		List<Poi> pois = dao.getEntries();
		data.setPois(pois);

		for (Poi poi : pois) {
			data.getIdToPoi().put(poi.getId(), poi);
		}

		db.closeConnection(false);

		loadKreise();
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

}
