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

package de.waldbrand.app.website.osm;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.A;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.waldbrand.app.website.osm.model.OsmPoi;

public class OsmLinks
{

	public static A link(OsmPoi poi, String name)
	{
		if (poi.getEntity().getType() == EntityType.Node) {
			return HTML.a("/osm/node/" + poi.getEntity().getId(), name);
		} else if (poi.getEntity().getType() == EntityType.Way) {
			return HTML.a("/osm/way/" + poi.getEntity().getId(), name);
		} else {
			return HTML.a("/osm/relation/" + poi.getEntity().getId(), name);
		}
	}

}
