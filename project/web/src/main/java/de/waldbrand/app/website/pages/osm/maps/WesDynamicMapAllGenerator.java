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

package de.waldbrand.app.website.pages.osm.maps;

import java.io.IOException;

import org.jsoup.nodes.DataNode;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Head;
import de.topobyte.jsoup.components.P;
import de.topobyte.jsoup.nodes.Element;
import de.topobyte.melon.commons.io.Resources;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.pages.osm.OsmAttributionUtil;
import de.waldbrand.app.website.util.MapUtil;
import de.waldbrand.app.website.util.MarkerShape;

public class WesDynamicMapAllGenerator extends SimpleBaseGenerator
{

	public WesDynamicMapAllGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content() throws IOException
	{
		Head head = builder.getHead();
		MapUtil.head(head);

		content.ac(HTML.h2("Wasserentnahmestellen (OpenStreetMap)"));
		P p = content.ac(HTML.p());
		p.appendText("Alle");

		MapUtil.addMap(content);

		StringBuilder code = new StringBuilder();
		code.append("var icons = new Map();");
		script(content, code);

		for (PoiType type : PoiType.values()) {
			MapUtil.addMarkerDef(content, "icons", type.toString(),
					OsmMarkers.getShape(type), OsmMarkers.getColor(type), "fa",
					"fa-tint");
		}

		MapUtil.addMarkerDef(content, "icons", "forst", MarkerShape.CIRCLE,
				"yellow", "fa", "fa-tint");

		code = new StringBuilder();

		code.append("var markers = new Map();");
		for (PoiType type : PoiType.values()) {
			MapUtil.markerStart(code, type.toString());
			MapUtil.markerEnd(code, type.toString());
		}
		MapUtil.markerStart(code, "forst");
		MapUtil.markerEnd(code, "forst");
		script(content, code);

		script(content, Resources.loadString("js/map-history.js"));
		script(content, Resources.loadString("js/map-update.js"));

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
