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

import static de.waldbrand.app.website.osm.PoiType.HYDRANT_PILLAR;
import static de.waldbrand.app.website.osm.PoiType.HYDRANT_PIPE;
import static de.waldbrand.app.website.osm.PoiType.SUCTION_POINT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.DataNode;

import com.google.common.base.Joiner;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Head;
import de.topobyte.jsoup.components.P;
import de.topobyte.jsoup.components.Script;
import de.topobyte.melon.commons.io.Resources;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.pages.wes.WesAttributionUtil;
import de.waldbrand.app.website.util.MapUtil;

public class WesMapAllGenerator extends SimpleBaseGenerator
{

	public WesMapAllGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content() throws IOException
	{
		Head head = builder.getHead();
		MapUtil.head(head);

		List<String> names = new ArrayList<>();
		for (PoiType type : PoiType.values()) {
			names.add(type.toString());
		}

		content.ac(HTML.h2("Wasserentnahmestellen (OpenStreetMap)"));
		P p = content.ac(HTML.p());
		p.appendText("Typen: " + Joiner.on(", ").join(names));

		MapUtil.addMap(content);

		Map<PoiType, String> typeToColor = new HashMap<>();
		typeToColor.put(HYDRANT_PILLAR, "red");
		typeToColor.put(HYDRANT_PIPE, "purple");
		typeToColor.put(SUCTION_POINT, "blue");

		for (PoiType type : PoiType.values()) {
			markerId(type);
			MapUtil.addMarkerDef(content, type.toString(),
					typeToColor.get(type), "fa", "tint");
		}

		Script script = content.ac(HTML.script());
		StringBuilder code = new StringBuilder();

		for (PoiType type : PoiType.values()) {
			MapUtil.markerStart(code);
			for (OsmNode node : Website.INSTANCE.getData().getTypeToNodes()
					.get(type)) {
				MapUtil.addMarker(code, node.getLatitude(), node.getLongitude(),
						type + " (node " + node.getId() + ")", markerId(type));
			}
			script.ac(new DataNode(code.toString()));
			MapUtil.markerEnd(content, code);
		}

		script = content.ac(HTML.script());
		script.ac(new DataNode(Resources.loadString("js/map-history.js")));

		WesAttributionUtil.attribution(content);
	}

	private String markerId(PoiType type)
	{
		return type.toString();
	}

}
