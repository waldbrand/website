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

package de.waldbrand.app.website.pages.osm;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Head;
import de.topobyte.jsoup.components.P;
import de.topobyte.jsoup.components.Table;
import de.topobyte.jsoup.components.TableHead;
import de.topobyte.jsoup.components.TableRow;
import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.links.LinkDefs;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.osm.model.OsmPoi;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.util.MapUtil;

public class OsmTypeStatsKeysGenerator extends SimpleBaseGenerator
{

	private PoiType type;

	public OsmTypeStatsKeysGenerator(WebPath path, PoiType type)
	{
		super(path);
		this.type = type;
	}

	@Override
	protected void content() throws IOException
	{
		Head head = builder.getHead();
		MapUtil.head(head);

		List<OsmPoi> pois = Website.INSTANCE.getData().getTypeToPois()
				.get(type);

		content.ac(HTML.h2("Wasserentnahmestellen (OpenStreetMap)"));
		P p = content.ac(HTML.p());
		p.at(String.format("%s (%d insgesamt)", type.getMultiple(),
				pois.size()));

		Multiset<String> keys = HashMultiset.create();
		for (OsmPoi poi : pois) {
			OsmEntity entity = poi.getEntity();
			Map<String, String> tags = OsmModelUtil.getTagsAsMap(entity);
			keys.addAll(tags.keySet());
		}

		if (keys.isEmpty()) {
			content.ac(HTML.p()).at("Nichts gefunden");
		} else {
			stats(keys);
		}

		OsmAttributionUtil.attribution(content);
	}

	private void stats(Multiset<String> keys)
	{
		Table table = content.ac(HTML.table());
		table.addClass("table");
		TableHead tableHead = table.head();
		TableRow headRow = tableHead.row();
		headRow.cell("Schlüssel");
		headRow.cell("Anzahl");

		ImmutableMultiset<String> histogram = Multisets
				.copyHighestCountFirst(keys);
		for (String key : histogram.elementSet()) {
			TableRow row = table.row();
			row.cell().ac(HTML.a(LinkDefs.OSM_TYPE_STATS_KEY.getLink(type, key),
					key));
			row.cell(String.format("%d", keys.count(key)));
		}
	}

}
