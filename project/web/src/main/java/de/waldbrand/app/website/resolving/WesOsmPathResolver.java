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
import de.topobyte.webgun.resolving.pathspec.PathSpec;
import de.topobyte.webgun.resolving.pathspec.PathSpecResolver;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.pages.osm.OsmDetailGenerator;
import de.waldbrand.app.website.pages.osm.OsmMappingGenerator;
import de.waldbrand.app.website.pages.osm.OsmStatsGenerator;
import de.waldbrand.app.website.pages.osm.WesGenerator;
import de.waldbrand.app.website.pages.osm.maps.WesMapAllGenerator;
import de.waldbrand.app.website.pages.osm.maps.WesMapGenerator;

public class WesOsmPathResolver
		extends PathSpecResolver<ContentGeneratable, Void>
{

	{
		map(new PathSpec("osm"), (path, output, request, data) -> {
			return new WesGenerator(path);
		});

		map(new PathSpec("osm", "stats"), (path, output, request, data) -> {
			return new OsmStatsGenerator(path);
		});
		map(new PathSpec("osm", "mapping"), (path, output, request, data) -> {
			return new OsmMappingGenerator(path);
		});

		map(new PathSpec("osm", "map", "alles"),
				(path, output, request, data) -> {
					return new WesMapAllGenerator(path);
				});

		for (PoiType type : PoiType.values()) {
			map(new PathSpec("osm", "map", type.getUrlKeyword()),
					(path, output, request, data) -> {
						return new WesMapGenerator(path, type);
					});
		}

		map(new PathSpec("osm", "node", ":id:"),
				(path, output, request, data) -> {
					String sId = output.getParameter("id");
					long id = Long.parseLong(sId);
					return new OsmDetailGenerator(path, EntityType.Node, id);
				});
		map(new PathSpec("osm", "way", ":id:"),
				(path, output, request, data) -> {
					String sId = output.getParameter("id");
					long id = Long.parseLong(sId);
					return new OsmDetailGenerator(path, EntityType.Way, id);
				});
	}

}
