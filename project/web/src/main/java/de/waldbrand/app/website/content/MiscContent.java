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

package de.waldbrand.app.website.content;

import de.topobyte.cachebusting.CacheBusting;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.bootstrap4.Bootstrap;
import de.topobyte.jsoup.bootstrap4.components.Alert;
import de.topobyte.jsoup.bootstrap4.components.ContextualType;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.nodes.Element;

public class MiscContent
{

	public static void attribution(Element<?> element)
	{
		Alert div = element.ac(Bootstrap.alert(ContextualType.INFO));
		div.addClass("mt-2");

		Div p = div;
		p.at("Quelle: ");
		p.ac(HTML.a("https://www.brandenburg-forst.de",
				"Landesbetrieb Forst Brandenburg")).attr("target", "_blank");
		p.at(", Datensatz: ");
		p.ac(HTML.a(
				"https://geoportal.brandenburg.de/detailansichtdienst/render?view=gdibb&url=https%3A%2F%2Fgeoportal.brandenburg.de%2Fgs-json%2Fxml%3Ffileid%3D4F8A8656-3467-4BE7-ACCA-AF54ECB6AAF7",
				"Wasserentnahmestellen im Land Brandenburg - WMS"))
				.attr("target", "_blank");
		p.at(", Lizenz: ");
		p.ac(HTML.a("https://www.govdata.de/dl-de/by-2-0", "dl-de/by-2-0"))
				.attr("target", "_blank");
	}

	public static void rowSponsors(Element<?> content)
	{
		Div row = content.ac(HTML.div("row"));
		Div col = row.ac(HTML.div("col-12"));

		col.ac(HTML.span()).at("Gefördert durch:");
		col.ac(HTML.br());

		String style = "height: 9em; margin: 3em 1em";

		col.ac(HTML.img(CacheBusting.resolve("images/bmbf.png"))).attr("style",
				style);
		col.ac(HTML.img(CacheBusting.resolve("images/prototypefund.png")))
				.attr("style", style);
		col.ac(HTML.img(CacheBusting.resolve("images/okfn.png"))).attr("style",
				style);
	}

}
