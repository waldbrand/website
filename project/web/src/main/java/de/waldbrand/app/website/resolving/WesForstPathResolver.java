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

package de.waldbrand.app.website.resolving;

import de.topobyte.jsoup.ContentGeneratable;
import de.topobyte.webgun.resolving.pathspec.PathSpec;
import de.topobyte.webgun.resolving.pathspec.PathSpecResolver;
import de.waldbrand.app.website.model.Poi;
import de.waldbrand.app.website.pages.wes.WesAddGenerator;
import de.waldbrand.app.website.pages.wes.WesDetailGenerator;
import de.waldbrand.app.website.pages.wes.WesGenerator;
import de.waldbrand.app.website.pages.wes.maps.WesMapGenerator;
import de.waldbrand.app.website.pages.wes.maps.WesMapKreisGenerator;
import de.waldbrand.app.website.pages.wes.maps.WesMapLandkreisFilterGenerator;
import de.waldbrand.app.website.pages.wes.maps.WesMapOartFilterGenerator;
import de.waldbrand.app.website.pages.wes.maps.WesMapOartGenerator;
import de.waldbrand.app.website.pages.wes.stats.WesStatsGenerator;
import de.waldbrand.app.website.pages.wes.stats.WesStatsOartGenerator;

public class WesForstPathResolver
		extends PathSpecResolver<ContentGeneratable, Void>
{

	{
		map(new PathSpec("wes"), (path, output, request, data) -> {
			return new WesGenerator(path);
		});
		map(new PathSpec("wes", "map"), (path, output, request, data) -> {
			return new WesMapGenerator(path);
		});
		map(new PathSpec("wes", "map", "filter-landkreis-select"),
				(path, output, request, data) -> {
					return new WesMapLandkreisFilterGenerator(path);
				});
		map(new PathSpec("wes", "map", "filter-oart-select"),
				(path, output, request, data) -> {
					return new WesMapOartFilterGenerator(path);
				});
		map(new PathSpec("wes", "map", "landkreis", ":kreis:"),
				(path, output, request, data) -> {
					String kreis = output.getParameter("kreis");
					return new WesMapKreisGenerator(path, kreis);
				});
		map(new PathSpec("wes", "map", "oart", ":oart:"),
				(path, output, request, data) -> {
					String sOart = output.getParameter("oart");
					try {
						int oart = Integer.parseInt(sOart);
						return new WesMapOartGenerator(path, oart);
					} catch (NumberFormatException e) {
						return null;
					}
				});
		map(new PathSpec("poi", ":id:"), (path, output, request, data) -> {
			int id = Integer.parseInt(output.getParameter("id"));
			return new WesDetailGenerator(path, id);
		});
		map(new PathSpec("wes", "stats", "oart"),
				(path, output, request, data) -> {
					return new WesStatsOartGenerator(path);
				});
		map(new PathSpec("wes", "stats", "baujahr"),
				(path, output, request, data) -> {
					return new WesStatsGenerator(path, "Baujahr",
							"Baujahre der Wasserentnahmestellen",
							Poi::getBaujahr);
				});
		map(new PathSpec("wes", "stats", "fstatus"),
				(path, output, request, data) -> {
					return new WesStatsGenerator(path, "Status",
							"Status der Wasserentnahmestellen (was heißt das?)",
							Poi::getFstatus);
				});
		map(new PathSpec("wes", "stats", "fkt_faehig"),
				(path, output, request, data) -> {
					return new WesStatsGenerator(path, "Funktionsfähigkeit",
							"Funktionsfähigkeit der Wasserentnahmestellen (was heißt das?)",
							Poi::getFktFaehig);
				});
		map(new PathSpec("wes", "stats", "menge"),
				(path, output, request, data) -> {
					return new WesStatsGenerator(path, "Menge",
							"Menge (maximaler Durchfluss?)", Poi::getMenge);
				});

		map(new PathSpec("wes", "eintragen"), (path, output, request, data) -> {
			return new WesAddGenerator(path);
		});
	}

}