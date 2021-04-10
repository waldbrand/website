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

package de.waldbrand.app.website.pages.wes;

import java.io.IOException;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.bootstrap4.Bootstrap;
import de.topobyte.jsoup.bootstrap4.components.ListGroupDiv;
import de.topobyte.jsoup.components.Head;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.util.MapUtil;

public class WesGenerator extends SimpleBaseGenerator
{

	public WesGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content() throws IOException
	{
		Head head = builder.getHead();
		MapUtil.head(head);

		content.ac(HTML.h2("Wasserentnahmestellen (Landesbetrieb Forst)"));

		ListGroupDiv list = content.ac(Bootstrap.listGroupDiv());
		list.addA("/wes/map", "Alle anzeigen");
		list.addA("/wes/map/filter-landkreis-select", "Landkreis-Filter");
		list.addA("/wes/map/filter-oart-select", "Art-Filter");

		content.ac(HTML.h3("Statistiken")).addClass("mt-3");

		list = content.ac(Bootstrap.listGroupDiv());
		list.addA("/wes/stats/oart", "Typen von Entnahmestellen");
		list.addA("/wes/stats/baujahr", "Baujahre");
		list.addA("/wes/stats/fstatus", "Status");
		list.addA("/wes/stats/fkt_faehig", "Funktionsfähigkeit");
		list.addA("/wes/stats/menge", "Menge");

		content.ac(HTML.h3("Mitmachen")).addClass("mt-3");

		list = content.ac(Bootstrap.listGroupDiv());
		list.addA("/wes/eintragen", "Wasserentnahmestelle eintragen");

		WesAttributionUtil.attribution(content);
	}

}
