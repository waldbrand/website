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
import de.topobyte.webgun.resolving.smart.SmartPathSpecResolver;
import de.waldbrand.app.website.lbforst.model.Poi;
import de.waldbrand.app.website.links.LinkDefs;
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
		extends SmartPathSpecResolver<ContentGeneratable, Void>
{

	{
		map(LinkDefs.FORST,
				(path, output, request, data) -> new WesGenerator(path));
		map(LinkDefs.FORST_MAP,
				(path, output, request, data) -> new WesMapGenerator(path));
		map(LinkDefs.FORST_MAP_FILTER_LANDKREIS_SELECT, (path, output, request,
				data) -> new WesMapLandkreisFilterGenerator(path));
		map(LinkDefs.FORST_MAP_FILTER_OART_SELECT, (path, output, request,
				data) -> new WesMapOartFilterGenerator(path));
		map(LinkDefs.FORST_MAP_LANDKREIS, (path, output, request, data,
				kreis) -> new WesMapKreisGenerator(path, kreis));
		map(LinkDefs.FORST_MAP_OART, (path, output, request, data,
				oart) -> new WesMapOartGenerator(path, oart));

		map(LinkDefs.FORST_POI, (path, output, request, data,
				id) -> new WesDetailGenerator(path, id));
		map(LinkDefs.FORST_STATS_OART, (path, output, request,
				data) -> new WesStatsOartGenerator(path));
		map(LinkDefs.FORST_STATS_BAUJAHR,
				(path, output, request, data) -> new WesStatsGenerator(path,
						"Baujahr", "Baujahre der Wasserentnahmestellen",
						Poi::getBaujahr));
		map(LinkDefs.FORST_STATS_FSTATUS,
				(path, output, request, data) -> new WesStatsGenerator(path,
						"Status",
						"Status der Wasserentnahmestellen (was heißt das?)",
						Poi::getFstatus));
		map(LinkDefs.FORST_STATS_FKT_FAEHIG,
				(path, output, request, data) -> new WesStatsGenerator(path,
						"Funktionsfähigkeit",
						"Funktionsfähigkeit der Wasserentnahmestellen (was heißt das?)",
						Poi::getFktFaehig));

		map(LinkDefs.FORST_STATS_MENGE,
				(path, output, request, data) -> new WesStatsGenerator(path,
						"Menge", "Menge (maximaler Durchfluss?)",
						Poi::getMenge));

		map(LinkDefs.FORST_ADD,
				(path, output, request, data) -> new WesAddGenerator(path));
	}

}
