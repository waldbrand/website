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

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;

import de.topobyte.luqe.iface.QueryException;
import de.topobyte.system.utils.SystemPaths;
import de.waldbrand.app.website.model.Data;
import de.waldbrand.app.website.model.Poi;

public class TestReadData
{

	public static void main(String[] args) throws IOException, QueryException
	{
		Path fileWes = SystemPaths.HOME.resolve(
				"github/waldbrand/wasserentnahmestellen/WES/daten/wes.gpkg");
		Path fileOsm = SystemPaths.HOME
				.resolve("github/waldbrand/osm-data/emergency.tbo");

		DataLoader dataLoader = new DataLoader();
		dataLoader.loadData(fileWes, fileOsm);

		Data data = dataLoader.getData();
		List<Poi> pois = data.getPois();
		System.out.println("WES count: " + pois.size());

		for (int i = 0; i < 3; i++) {
			Poi poi = pois.get(i);
			Coordinate c = poi.getCoordinate();
			System.out.println(String.format("%d: %d,%d, %f,%f", poi.getId(),
					poi.getRechtsW(), poi.getHochW(), c.getX(), c.getY()));
		}
	}

}
