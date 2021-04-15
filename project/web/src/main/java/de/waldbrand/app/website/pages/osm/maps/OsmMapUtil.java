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

package de.waldbrand.app.website.pages.osm.maps;

import org.jsoup.nodes.Node;

import de.waldbrand.app.website.osm.OsmLinks;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.osm.model.OsmPoi;
import de.waldbrand.app.website.util.MapUtil;

public class OsmMapUtil
{

	public static void marker(StringBuilder code, OsmPoi poi, PoiType type,
			String markerId)
	{
		String content = content(poi, type);
		MapUtil.addMarker(code, poi.getLat(), poi.getLon(), content, markerId);
	}

	public static String content(OsmPoi poi, PoiType type)
	{
		Node link = OsmLinks.link(poi, "Details");
		String name = poi.getEntity().getType().toString().toLowerCase();
		return type.getName() + "<br>OSM " + name + " "
				+ poi.getEntity().getId() + "  " + link;
	}

}
