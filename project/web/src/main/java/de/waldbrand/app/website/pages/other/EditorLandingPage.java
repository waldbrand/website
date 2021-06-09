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
import static de.waldbrand.app.website.widgets.Cards.emptyCard;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import de.topobyte.cachebusting.CacheBusting;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Div;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.content.MiscContent;
import de.waldbrand.app.website.links.LinkDefs;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.stats.model.AggregatedStats;

public class EditorLandingPage extends SimpleBaseGenerator
{

	public EditorLandingPage(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content()
	{
		content.ac(HTML.h1("Editor für Wasserentnahmestellen"));

		String linkEditor = LinkDefs.EDITOR.getLink();

		Div deck = content.ac(HTML.div("row"));

		card(deck, "markdown/de/landing-editor-hinweise.md",
				Arrays.asList(HTML.a(linkEditor, "Los geht's, zum Editor")));

		card(deck, "/" + CacheBusting.resolve("images/editor.png"), linkEditor,
				null, Arrays.asList(), "So sieht der Editor in Benutzung aus.");

		AggregatedStats stats = Website.INSTANCE.getStats();
		if (stats != null) {
			stats(deck, stats);
		}

		MiscContent.rowSponsors(content);
	}

	private void stats(Div deck, AggregatedStats stats)
	{
		Div body = emptyCard(deck, "Statistiken");
		DateTimeFormatter pattern = DateTimeFormatter
				.ofPattern("dd.MM.yyyy, HH:mm 'Uhr'");
		body.ac(HTML.p()).at(String.format(
				"Bis heute (Stand %s) haben mit dem Editor %d Nutzer:innen %d Einträge"
						+ " hinzugefügt und %d Ergänzungen oder Änderungen vorgenommen."
						+ " 💪 Vielen Dank an alle, die schon mitgearbeitet haben! 💖",
				pattern.format(stats.getTime()), stats.getUsers().size(),
				stats.getCreated(), stats.getModified()));
	}

}
