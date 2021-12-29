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

package de.waldbrand.app.website.pages.combined;

import java.io.IOException;

import org.jsoup.nodes.DataNode;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.components.Head;
import de.topobyte.jsoup.components.P;
import de.topobyte.jsoup.nodes.Element;
import de.topobyte.melon.commons.io.Resources;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.icons.Icon;
import de.waldbrand.app.website.icons.Icons;
import de.waldbrand.app.website.icons.LeafletIcon;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.pages.osm.OsmAttributionUtil;
import de.waldbrand.app.website.util.MapUtil;

public class WesDynamicMapOsmGenerator extends SimpleBaseGenerator
{

	public WesDynamicMapOsmGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content() throws IOException
	{
		Head head = builder.getHead();
		MapUtil.head(head);

		content.ac(HTML.h2("Wasserentnahmestellen"));
		P p = content.ac(HTML.p());
		p.at("Quelle: OpenStreetMap");

		Div container = content.ac(HTML.div());
		container.attr("style", "position:relative");
		Div overlay = container.ac(HTML.div());
		overlay.attr("id", "overlay");
		overlay.attr("style", "position: absolute; width: 100%; height: 100px;"
				+ " background-color: rgba(255,255,255, 0.5); z-index: 500; text-align: center;"
				+ " line-height: 100px");
		MapUtil.addMap(container);

		overlay.ac(HTML.span()).at(
				"Bitte Karte weiter vergrößern, um Wasserentnahmestellen anzuzeigen");

		StringBuilder code = new StringBuilder();
		code.append("var icons = new Map();");
		script(content, code);

		for (Icon icon : Icons.getAll()) {
			script(content, new LeafletIcon(icon.getName(), icon.getPath(),
					null, icon.getWidth(), icon.getHeight()).toString());
		}

		code = new StringBuilder();

		code.append("var markers = new Map();");
		for (Icon icon : Icons.getAll()) {
			MapUtil.markerStart(code, icon.getName());
			MapUtil.markerEnd(code, icon.getName());
		}
		script(content, code);

		code = new StringBuilder();
		code.append("var poiType = 'osm';");
		script(content, code);

		script(content, Resources.loadString("js/map-history.js"));
		script(content, Resources.loadString("js/map-update-wes.js"));

		OsmAttributionUtil.attribution(content);
	}

	private void script(Element<?> content, String code)
	{
		content.ac(HTML.script()).ac(new DataNode(code));
	}

	private void script(Element<?> content, StringBuilder code)
	{
		content.ac(HTML.script()).ac(new DataNode(code.toString()));
	}

}
