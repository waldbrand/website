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
import static de.topobyte.jsoup.HTML.p;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.bootstrap4.Bootstrap;
import de.topobyte.jsoup.bootstrap4.components.Container;
import de.topobyte.jsoup.components.A;
import de.topobyte.jsoup.components.P;
import de.topobyte.jsoup.components.UnorderedList;
import de.topobyte.jsoup.feather.Feather;
import de.topobyte.jsoup.nodes.Element;
import de.topobyte.pagegen.core.LinkResolver;
import de.waldbrand.app.website.PathHelper;

public class MainFooter extends Element<MainFooter>
{

	public MainFooter(LinkResolver resolver)
	{
		super("footer");
		attr("class", "footer");

		Container div = ac(Bootstrap.container());

		UnorderedList links = div.ac(HTML.ul());

		String imprintLink = resolver.getLink(PathHelper.imprint());
		A linkAbout = a(imprintLink, "Impressum");
		links.addItem(linkAbout);

		String privacyLink = resolver.getLink(PathHelper.privacy());
		A linkPrivacy = a(privacyLink, "Datenschutz");
		links.addItem(linkPrivacy);

		P p = div.ac(p().addClass("text-muted"));

		p.appendText("Made with ");
		p.ac(Feather.heart("1em"));
		p.appendText(" in Berlin");
	}

}
