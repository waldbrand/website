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

package de.waldbrand.app.website.util;

import org.jsoup.nodes.Element;

import de.topobyte.jsoup.HTML;

public class ErrorUtil
{

	public static void write404(Element content)
	{
		content.appendChild(HTML.h1().text("Seite nicht gefunden"));
		content.appendText("Fehlercode: 404");
	}

	public static void writeError(Element content)
	{
		content.appendChild(HTML.h1().text("Ouch!"));
		content.appendText("Da ist was schiefgleaufen");
	}

}
