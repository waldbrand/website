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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.Markdown;
import de.topobyte.jsoup.components.A;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.components.Img;
import de.topobyte.jsoup.components.P;
import de.topobyte.melon.commons.io.Resources;
import de.topobyte.webgun.exceptions.InternalServerErrorException;

public class Cards
{

	public static void card(Div deck, String resource)
	{
		card(deck, resource, Arrays.asList());
	}

	public static void card(Div deck, String resource, List<A> links)
	{
		Div col = deck
				.ac(HTML.div("col-12 col-md-6 d-flex align-items-stretch"));
		Div card = col.ac(HTML.div("card mb-4 w-100"));
		Div body = card.ac(HTML.div("card-body"));
		try {
			String markdown = Resources.loadString(resource);
			String html = Markdown.toHtml(markdown);
			Document doc = Jsoup.parse(html);

			Element headline = doc.select("h1").first();
			if (headline != null) {
				headline.remove();
				body.ac(HTML.h5(headline.text())).addClass("card-title");
			}

			body.ac(doc.body());
		} catch (IOException e) {
			throw new InternalServerErrorException(e);
		}
		for (A link : links) {
			body.ac(link).addClass("card-link");
		}
	}

	public static void card(Div deck, String image, String imageLink,
			String title, List<A> links, String text)
	{
		Div col = deck
				.ac(HTML.div("col-12 col-md-6 d-flex align-items-stretch"));
		Div card = col.ac(HTML.div("card mb-4 w-100"));
		Img img = HTML.img(image).addClass("card-img-top");
		if (imageLink == null) {
			card.ac(img);
		} else {
			card.ac(HTML.a(imageLink)).ac(img);
		}
		Div body = card.ac(HTML.div("card-body"));
		if (title != null) {
			body.ac(HTML.h5(title)).addClass("card-title");
		}
		P p = body.ac(HTML.p());
		p.appendText(text);
		for (A link : links) {
			body.ac(link).addClass("card-link");
		}
	}

}
