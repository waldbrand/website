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

package de.waldbrand.app.website.widgets;

import static de.topobyte.jsoup.HTML.a;

import de.topobyte.cachebusting.CacheBusting;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.bootstrap4.components.Expand;
import de.topobyte.jsoup.bootstrap4.components.Menu;
import de.topobyte.jsoup.bootstrap4.components.MenuBuilder;
import de.topobyte.jsoup.components.A;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.components.Img;
import de.topobyte.jsoup.components.UnorderedList;
import de.topobyte.pagegen.core.LinkResolver;
import de.topobyte.webpaths.WebPaths;
import de.waldbrand.app.website.links.LinkDefs;

public class MainMenu
{

	public static Menu create(LinkResolver resolver)
	{
		MenuBuilder builder = new MenuBuilder();
		builder.setExpand(Expand.MD).setFixed(true);
		Menu menu = builder.create();

		menu.addClass("shadow-sm");

		A brand = a("/");

		Img image = HTML.img(
				"/" + WebPaths.get(CacheBusting.resolve("images/icon.svg")));
		image.attr("height", "40px");
		image.attr("style", "padding-right:15px");
		brand.ap(image);

		brand.appendText("Waldbrand-App");

		menu.addBrand(brand);
		menu.addToggleButton();

		Div collapse = menu.addCollapsible();
		UnorderedList main = menu.addSection(collapse);
		UnorderedList right = menu.addSectionRight(collapse);

		menu.addLink(main, LinkDefs.FORST.getLink(),
				"Wasserentnahmestellen (Landesbetrieb Forst)", false);
		menu.addLink(main, LinkDefs.OSM.getLink(),
				"Wasserentnahmestellen (OpenStreetMap)", false);

		menu.addLink(right, "/about", "Über", false);

		return menu;
	}

}
