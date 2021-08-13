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

package de.waldbrand.app.website.lbforst;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.A;
import de.waldbrand.app.website.lbforst.model.WesPoi;
import de.waldbrand.app.website.links.LinkDefs;

public class PoiLinks
{

	public static A link(WesPoi poi, String name)
	{
		return HTML.a(LinkDefs.FORST_POI.getLink(poi.getId()), name);
	}

}
