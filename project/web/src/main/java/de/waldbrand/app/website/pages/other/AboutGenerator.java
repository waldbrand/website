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

package de.waldbrand.app.website.pages.other;

import de.topobyte.cachebusting.CacheBusting;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.bootstrap4.Bootstrap;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.components.Img;
import de.topobyte.jsoup.components.P;
import de.topobyte.webpaths.WebPath;
import de.topobyte.webpaths.WebPaths;
import de.waldbrand.app.website.content.MiscContent;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;

public class AboutGenerator extends SimpleBaseGenerator
{

	public AboutGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content()
	{
		content.ac(HTML.h1("Waldbrand-App Baukasten"));

		Div row = content.ac(Bootstrap.row());
		row.addClass("align-items-center");

		Div colLeft = row.ac(HTML.div("col-12 col-sm-4"));
		Div colRight = row.ac(HTML.div("col-12 col-sm-8"));

		Img image = colLeft.ac(HTML.img(
				"/" + WebPaths.get(CacheBusting.resolve("images/icon.svg"))));
		image.addClass("img-fluid");
		image.attr("style", "width: 100%; padding: 15%");

		P p = colRight.ac(HTML.p());
		p.appendText(
				"Ein Projekt zur Unterstützung der Feuerwehr bei Waldbrandeinsätzen.");
		p = colRight.ac(HTML.p());
		p.appendText("Ein Projekt der Zielinski & Kürten GbR.");

		p = colRight.ac(HTML.p());
		p.appendText("Für Feedback bitte ");
		p.ac(HTML.a("/kontakt", "hier"));
		p.appendText(" vorbeischauen.");

		MiscContent.rowSponsors(content);
		p = content.ac(HTML.p());
		p.at("Das Projekt wird vom 01.03.2021 bis zum 31.08.2021 unter dem Förerkennzeichen 01IS21S06"
				+ " vom Bundesministerium für Bildung und Forschung (BMBF) über den PrototyeFund gefördert.");
	}

}
