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
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.DataNode;

import com.google.common.base.Joiner;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Head;
import de.topobyte.jsoup.components.P;
import de.topobyte.jsoup.components.Script;
import de.topobyte.melon.commons.io.Resources;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.osm.OsmTypes;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.osm.model.OsmPoi;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.pages.osm.OsmAttributionUtil;
import de.waldbrand.app.website.util.MapUtil;

public class OsmWesMapAllGenerator extends SimpleBaseGenerator
{

	public OsmWesMapAllGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content() throws IOException
	{
		Head head = builder.getHead();
		MapUtil.head(head);

		List<String> names = OsmTypes.multiNames(Arrays.asList(PoiType.values()));

		content.ac(HTML.h2("Wasserentnahmestellen (OpenStreetMap)"));
		P p = content.ac(HTML.p());
		p.appendText("Typen: " + Joiner.on(", ").join(names));

		MapUtil.addMap(content);

		for (PoiType type : PoiType.values()) {
			MapUtil.addMarkerDef(content, type.toString(),
					OsmMarkers.getShape(type), OsmMarkers.getColor(type), "fa",
					"fa-tint");
		}

		Script script = content.ac(HTML.script());

		StringBuilder code = new StringBuilder();
		code.append("var markers = new Map();");
		for (PoiType type : PoiType.values()) {
			MapUtil.markerStart(code, type.toString());
			for (OsmPoi poi : Website.INSTANCE.getData().getTypeToPois()
					.get(type)) {
				OsmMapUtil.marker(code, poi, type, markerId(type),
						String.format("markers.get('%s')", type));
			}
			MapUtil.markerEnd(code, type.toString());
		}
		script.ac(new DataNode(code.toString()));

		script = content.ac(HTML.script());
		script.ac(new DataNode(Resources.loadString("js/map-history.js")));

		OsmAttributionUtil.attribution(content);
	}

	private String markerId(PoiType type)
	{
		return type.toString();
	}

}
