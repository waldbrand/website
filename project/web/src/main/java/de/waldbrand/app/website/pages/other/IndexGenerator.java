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
import de.topobyte.jsoup.components.Img;
import de.topobyte.jsoup.components.P;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.content.MiscContent;
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
						+ " Unterstützung der Einsatzkräfte bei Waldbränden! 🔥🚒🌊🧯");

		Div deck = content.ac(HTML.div("row"));
		String linkMap = "/wes/map/osm-forst";
		card(deck, CacheBusting.resolve("images/feature-karte.png"), linkMap,
				"Wasserentnahmestellen", HTML.a(linkMap, "Zur Karte"),
				"Hier gibt es eine Karte mit Wasserentnahmestellen die"
						+ " in den Datensätzen der OpenStreetMap und des Landesbetrieb Forst"
						+ " verfügbar sind.");

		cardIntro(deck);

		String linkWes = "/wes";
		card(deck, CacheBusting.resolve("images/feature-karte.png"), linkWes,
				"Wasserentnahmestellen (Landesbetrieb Forst)",
				HTML.a(linkWes, "Zu den Wasserentnahmestellen"),
				"Hier gibt es Infos und Karten zu den Wasserentnahmestellen die"
						+ " im Datensatz der Landesbetrieb Forst im  Geoportal Brandenburg"
						+ " verfügbar sind.");

		String linkOsm = "/osm";
		card(deck, CacheBusting.resolve("images/feature-karte.png"), linkOsm,
				"Wasserentnahmestellen (OpenStreetMap)",
				HTML.a(linkOsm, "Zu den Wasserentnahmestellen"),
				"Hier gibt es Infos und Karten zu den Wasserentnahmestellen die"
						+ " im Community-Projekt OpenStreetMap verfügbar sind.");

		MiscContent.rowSponsors(content);
	}

	private void cardIntro(Div deck)
	{
		Div col = deck
				.ac(HTML.div("col-12 col-md-6 d-flex align-items-stretch"));
		Div card = col.ac(HTML.div("card mb-4"));
		Div body = card.ac(HTML.div("card-body"));
		body.ac(HTML.h5("Die Waldbrand-App")).addClass("card-title");
		P p = body.ac(HTML.p());
		p.at("Im Rahmen des Prototype-Fund entwickeln wir"
				+ " im Zeitraum März bis September 2021 eine App, die relevante"
				+ " Informationen für die Brandenburger Feuerwehren zur Bekämpfung"
				+ " von Waldbränden in einer offline-fähigen App verfügbar macht.");
		p = body.ac(HTML.p());
		p.at("Eine wichtige Rolle spielen in diesem Zusammenhang die Wasserentnahmestellen."
				+ " Auf dieser Seite sind verschiedene Datenquellen zu diesem Thema"
				+ " visualisiert.");
	}

	private void card(Div deck, String image, String imageLink, String title,
			A link, String text)
	{
		Div col = deck
				.ac(HTML.div("col-12 col-md-6 d-flex align-items-stretch"));
		Div card = col.ac(HTML.div("card mb-4"));
		Img img = HTML.img(image).addClass("card-img-top");
		if (imageLink == null) {
			card.ac(img);
		} else {
			card.ac(HTML.a(imageLink)).ac(img);
		}
		Div body = card.ac(HTML.div("card-body"));
		body.ac(HTML.h5(title)).addClass("card-title");
		P p = body.ac(HTML.p());
		p.appendText(text);
		if (link != null) {
			body.ac(link).addClass("card-link");
		}
	}

}
