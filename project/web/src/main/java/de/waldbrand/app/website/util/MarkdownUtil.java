package de.waldbrand.app.website.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.topobyte.jsoup.Markdown;
import de.topobyte.melon.commons.io.Resources;

public class MarkdownUtil
{

	public static void appendMarkdown(Element element, String resource)
			throws IOException
	{
		String markdown = Resources.loadString(resource);
		String html = Markdown.toHtml(markdown);
		Document doc = Jsoup.parse(html);
		element.appendChild(doc.body());
	}

}
