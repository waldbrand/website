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

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.DataNode;

import com.google.common.base.Joiner;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.bootstrap4.Bootstrap;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.components.Head;
import de.topobyte.jsoup.components.P;
import de.topobyte.jsoup.components.Script;
import de.topobyte.jsoup.components.Table;
import de.topobyte.jsoup.components.TableRow;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.iface.OsmTag;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.webgun.exceptions.PageNotFoundException;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.lbforst.model.Data;
import de.waldbrand.app.website.osm.OsmTypes;
import de.waldbrand.app.website.osm.OsmUtil;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.osm.model.OsmPoi;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.util.MapUtil;
import de.waldbrand.app.website.util.MarkerShape;

public class OsmDetailGenerator extends SimpleBaseGenerator
{

	private EntityType type;
	private long id;

	public OsmDetailGenerator(WebPath path, EntityType type, long id)
	{
		super(path);
		this.type = type;
		this.id = id;
	}

	@Override
	protected void content()
	{
		Data data = Website.INSTANCE.getData();

		OsmPoi poi = OsmUtil.getPoi(data, type, id);

		if (poi == null) {
			throw new PageNotFoundException();
		}

		OsmEntity entity = poi.getEntity();

		// close button
		Div divClose = content.ac(HTML.div()).attr("style",
				"float:right; margin: 1em");

		divClose.ac(HTML.a("javascript:window.history.back()", "X"))
				.addClass("btn btn-secondary");

		// headline
		Map<String, String> tags = OsmModelUtil.getTagsAsMap(entity);
		EnumSet<PoiType> types = OsmUtil.classify(tags);

		List<String> names = OsmTypes.singleNames(types);
		content.ac(HTML.h1(Joiner.on(", ").join(names)));

		// clear div to avoid close button to interfere with other column
		// content
		content.ac(HTML.div()).attr("style", "clear:both");

		Div row = content.ac(Bootstrap.row());
		Div col1 = row.ac(HTML.div("col-12 col-md-6"));
		Div col2 = row.ac(HTML.div("col-12 col-md-6"));

		map(col1, poi);
		data(col2, entity);

		P p = col2.ac(HTML.p());
		p.appendText(
				"Angaben falsch, oder es fehlt was? Sag uns gerne bescheid: ");
		p.ac(HTML.a(String.format(
				"mailto:team@waldbrand-app.de?subject=Feedback: %s id %d",
				typename(type), id), "team@waldbrand-app.de"));

		OsmAttributionUtil.attribution(content);
	}

	private void data(Div container, OsmEntity entity)
	{
		Table table = container.ac(HTML.table());
		table.addClass("table");

		for (int i = 0; i < entity.getNumberOfTags(); i++) {
			OsmTag tag = entity.getTag(i);
			row(table, tag.getKey(), tag.getValue());
		}

		TableRow rowLinkOsm = table.row();
		rowLinkOsm.cell("OSM");
		String typename = typename(entity.getType());
		rowLinkOsm
				.cell(HTML.a(
						String.format("https://www.openstreetmap.org/%s/%d",
								typename, id),
						String.format("%s %d", typename, id)));
	}

	private String typename(EntityType type)
	{
		return type.toString().toLowerCase();
	}

	private void row(Table table, String val1, String val2)
	{
		TableRow row = table.row();
		row.cell(val1);
		row.cell(val2);
	}

	private void map(Div container, OsmPoi poi)
	{
		Head head = builder.getHead();
		MapUtil.head(head);

		MapUtil.addMap(container, poi.getLat(), poi.getLon(), 17);

		MapUtil.addMarkerDef(container, MarkerShape.CIRCLE, "red", "fa",
				"fa-tint");

		Script script = container.ac(HTML.script());
		StringBuilder code = new StringBuilder();

		String typename = typename(poi.getEntity().getType());

		MapUtil.markerStart(code);
		MapUtil.addMarker(code, poi.getLat(), poi.getLon(),
				String.format("%s %d", typename, id));
		MapUtil.markerEnd(code);
		script.ac(new DataNode(code.toString()));
	}

}
