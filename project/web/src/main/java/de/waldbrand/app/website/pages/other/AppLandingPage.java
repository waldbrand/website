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

import java.util.Arrays;

import de.topobyte.cachebusting.CacheBusting;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Div;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.content.MiscContent;
import de.waldbrand.app.website.links.LinkDefs;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;

public class AppLandingPage extends SimpleBaseGenerator
{

	public AppLandingPage(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content()
	{
		content.ac(HTML.h1("Android-App"));

		String linkApp = LinkDefs.GOOGLE_PLAY;

		Div deck = content.ac(HTML.div("row"));

		card(deck, "markdown/de/landing-app.md",
				Arrays.asList(HTML.a(linkApp, "Jetzt herunterladen")));

		card(deck, "/" + CacheBusting.resolve("images/app.png"), linkApp, null,
				Arrays.asList(), "So sieht die App in Benutzung aus.");

		MiscContent.rowSponsors(content);
	}

}
