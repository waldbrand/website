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

import static de.waldbrand.app.website.widgets.Cards.card;

import java.io.IOException;
import java.util.Arrays;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.bootstrap4.Bootstrap;
import de.topobyte.jsoup.bootstrap4.components.Alert;
import de.topobyte.jsoup.bootstrap4.components.ContextualType;
import de.topobyte.jsoup.components.Div;
import de.topobyte.webgun.exceptions.InternalServerErrorException;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.content.MiscContent;
import de.waldbrand.app.website.links.LinkDefs;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.util.MarkdownUtil;

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

		// P p = content.ac(HTML.p());
		// p.at(
		// "Im Rahmen des Prototype Fund entwickeln wir einen Baukasten zum"
		// + " Erstellen von offline-fähigen Waldbrand-Apps zur"
		// + " Unterstützung der Einsatzkräfte bei Waldbränden! 🔥🚒🌊🧯");

		// "Finde deine Wasserentnahmestellen"

		Alert alert = content.ac(Bootstrap.alert(ContextualType.WARNING));
		try {
			MarkdownUtil.appendMarkdown(alert, "markdown/de/warning.md");
		} catch (IOException e) {
			throw new InternalServerErrorException();
		}

		Div deck = content.ac(HTML.div("row"));

		card(deck, "markdown/de/intro-app.md",
				Arrays.asList(
						HTML.a(LinkDefs.LANDING_APP.getLink(), "Mehr erfahren"),
						HTML.a(LinkDefs.GOOGLE_PLAY, "Jetzt runterladen")));

		card(deck, "markdown/de/intro-editor.md", Arrays.asList(
				HTML.a(LinkDefs.LANDING_EDITOR.getLink(), "Mehr erfahren"),
				HTML.a(LinkDefs.EDITOR.getLink(), "Direkt zum Editor")));

		card(deck, "markdown/de/intro-wer.md");

		card(deck, "markdown/de/intro-was.md");

		card(deck, "markdown/de/intro-suche.md");

		card(deck, "markdown/de/intro-wes.md", Arrays.asList(
				HTML.a(LinkDefs.LANDING_WES.getLink(), "Mehr erfahren")));

		card(deck, "markdown/de/intro-rettungspunkte.md",
				Arrays.asList(HTML.a(LinkDefs.LANDING_RETTUNGSPUNKTE.getLink(),
						"Mehr erfahren")));

		MiscContent.rowSponsors(content);
	}

}
