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

package de.waldbrand.app.website.pages.rettungspunkte;

import static de.waldbrand.app.website.widgets.Cards.card;

import java.io.IOException;
import java.util.Arrays;

import de.topobyte.cachebusting.CacheBusting;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.Markdown;
import de.topobyte.jsoup.components.Div;
import de.topobyte.webgun.exceptions.InternalServerErrorException;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.content.MiscContent;
import de.waldbrand.app.website.links.LinkDefs;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;

public class RettungspunkteLandingPage extends SimpleBaseGenerator
{

	public RettungspunkteLandingPage(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content()
	{
		try {
			Markdown.renderResource(content.ac(HTML.p()),
					"markdown/de/landing-rettungspunkte-intro.md");
		} catch (IOException e) {
			throw new InternalServerErrorException(e);
		}

		Div deck = content.ac(HTML.div("row"));

		String linkWes = LinkDefs.MAP_RETTUNGSPUNKTE.getLink();
		card(deck,
				"/" + CacheBusting.resolve("images/feature-rettungspunkte.png"),
				linkWes, "Rettungspunkte (Landesbetrieb Forst)",
				Arrays.asList(HTML.a(linkWes, "Zu den Rettungspunkten")),
				"Hier gibt es eine Karte zu den Rettungspunkten die"
						+ " im Datensatz der Landesbetrieb Forst im  Geoportal Brandenburg"
						+ " verfügbar sind.");

		MiscContent.rowSponsors(content);
	}

}
