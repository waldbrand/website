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
import de.topobyte.jsoup.components.A;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.components.P;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;

public class IndexGenerator extends SimpleBaseGenerator
{

	public IndexGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content()
	{
		content.ac(HTML.h1("Die Waldbrand-App für Brandenburg"));

		P p = content.ac(HTML.p());
		p.appendText(
				"Im Rahmen des Prototype Fund entwickeln wir einen Baukasten zum"
						+ " Erstellen von offline-fähigen Waldbrand-Apps zur"
						+ " Unterstützung der Einsatzkräft bei Waldbränden! 🔥🚒🌊🧯");

		Div deck = content.ac(HTML.div("row"));
		card(deck, CacheBusting.resolve("images/feature-karte.png"),
				"Wasserentnahmestellen", HTML.a("/wes", "Karte öffnen"),
				"Auf dieser Karte werden die Wasserentnahmestellen angezeigt");
	}

	private void card(Div deck, String image, String title, A link, String text)
	{
		Div col = deck
				.ac(HTML.div("col-12 col-md-6 d-flex align-items-stretch"));
		Div card = col.ac(HTML.div("card mb-4"));
		card.ac(HTML.img(image)).addClass("card-img-top");
		Div body = card.ac(HTML.div("card-body"));
		body.ac(HTML.h5(title)).addClass("card-title");
		P p = body.ac(HTML.p());
		p.appendText(text);
		if (link != null) {
			body.ac(link).addClass("card-link");
		}
	}

}
