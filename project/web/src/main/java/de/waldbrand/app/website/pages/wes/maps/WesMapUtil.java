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

package de.waldbrand.app.website.pages.wes.maps;

import org.jsoup.nodes.Node;
import org.locationtech.jts.geom.Coordinate;

import de.waldbrand.app.website.lbforst.NameUtil;
import de.waldbrand.app.website.lbforst.PoiLinks;
import de.waldbrand.app.website.lbforst.model.WesPoi;
import de.waldbrand.app.website.util.MapUtil;

public class WesMapUtil
{

	public static void marker(StringBuilder code, WesPoi poi, boolean withLink,
			String markerId, String markers)
	{
		String content = content(poi, withLink);
		Coordinate c = poi.getCoordinate();
		MapUtil.addMarker(code, c.getY(), c.getX(), content, markerId, markers);
	}

	public static String content(WesPoi poi, boolean withLink)
	{
		String name = NameUtil.getName(poi);

		String content = name;
		if (withLink) {
			Node link = PoiLinks.link(poi, "Details");
			content += " " + link.toString();
		}

		return content;
	}

}
