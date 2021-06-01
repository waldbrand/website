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
