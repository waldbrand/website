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
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.webgun.resolving.smart.SmartPathSpecResolver;
import de.waldbrand.app.website.links.LinkDefs;
import de.waldbrand.app.website.pages.osm.OsmDetailGenerator;
import de.waldbrand.app.website.pages.osm.OsmMappingGenerator;
import de.waldbrand.app.website.pages.osm.OsmStatsGenerator;
import de.waldbrand.app.website.pages.osm.OsmTypeStatsKeyValuesGenerator;
import de.waldbrand.app.website.pages.osm.OsmTypeStatsKeysGenerator;
import de.waldbrand.app.website.pages.osm.OsmWesGenerator;
import de.waldbrand.app.website.pages.osm.maps.OsmWesMapAllGenerator;
import de.waldbrand.app.website.pages.osm.maps.OsmWesMapGenerator;

public class WesOsmPathResolver
		extends SmartPathSpecResolver<ContentGeneratable, Void>
{

	{
		map(LinkDefs.OSM, (path, output, request, data) -> {
			return new OsmWesGenerator(path);
		});

		map(LinkDefs.OSM_STATS, (path, output, request, data) -> {
			return new OsmStatsGenerator(path);
		});
		map(LinkDefs.OSM_MAPPING, (path, output, request, data) -> {
			return new OsmMappingGenerator(path);
		});

		map(LinkDefs.OSM_MAP_ALL, (path, output, request, data) -> {
			return new OsmWesMapAllGenerator(path);
		});

		map(LinkDefs.OSM_MAP, (path, output, request, data,
				type) -> new OsmWesMapGenerator(path, type));

		map(LinkDefs.OSM_TYPE_STATS, (path, output, request, data,
				type) -> new OsmTypeStatsKeysGenerator(path, type));

		map(LinkDefs.OSM_TYPE_STATS_KEY, (path, output, request, data, type,
				key) -> new OsmTypeStatsKeyValuesGenerator(path, type, key));

		map(LinkDefs.OSM_NODE, (path, output, request, data,
				id) -> new OsmDetailGenerator(path, EntityType.Node, id));
		map(LinkDefs.OSM_WAY, (path, output, request, data,
				id) -> new OsmDetailGenerator(path, EntityType.Way, id));
	}

}
