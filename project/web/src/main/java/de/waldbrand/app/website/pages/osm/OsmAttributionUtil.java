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

package de.waldbrand.app.website.pages.osm;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.bootstrap4.Bootstrap;
import de.topobyte.jsoup.bootstrap4.components.Alert;
import de.topobyte.jsoup.bootstrap4.components.ContextualType;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.nodes.Element;

public class OsmAttributionUtil
{

	public static void attribution(Element<?> element)
	{
		Alert div = element.ac(Bootstrap.alert(ContextualType.INFO));
		div.addClass("mt-2");

		Div p = div;
		p.at("Quelle: ");
		p.ac(HTML.a("https://www.openstreetmap.org/copyright",
				"© OpenStreetMap contributors")).attr("target", "_blank");
		p.at(", Lizenz: ");
		p.ac(HTML.a("https://opendatacommons.org/licenses/odbl/",
				"Open Data Commons Open Database License (ODbL)"))
				.attr("target", "_blank");
	}

}
