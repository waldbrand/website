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

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.P;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;

public class ContactGenerator extends SimpleBaseGenerator
{

	public ContactGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content()
	{
		content.ac(HTML.h1("Feedback"));

		P p = content.ac(HTML.p());
		p.appendText("Falls du irgendein Feedback hast"
				+ " melde dich bitte hier: ");
		p.ac(HTML.a("mailto:team@waldbrand-app.de", "team@waldbrand-app.de"));

		p = content.ac(HTML.p());
		p.appendText("Danke! Sebastian und Mo");
	}

}
