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
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmTag;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.webgun.exceptions.PageNotFoundException;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.osm.OsmTypes;
import de.waldbrand.app.website.osm.OsmUtil;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.util.MapUtil;

public class OsmDetailGenerator extends SimpleBaseGenerator
{

	private long id;

	public OsmDetailGenerator(WebPath path, long id)
	{
		super(path);
		this.id = id;
	}

	@Override
	protected void content()
	{
		OsmNode node = Website.INSTANCE.getData().getIdToNodes().get(id);
		if (node == null) {
			throw new PageNotFoundException();
		}

		// close button
		Div divClose = content.ac(HTML.div()).attr("style",
				"float:right; margin: 1em");

		divClose.ac(HTML.a("javascript:window.history.back()", "X"))
				.addClass("btn btn-secondary");

		// headline
		Map<String, String> tags = OsmModelUtil.getTagsAsMap(node);
		EnumSet<PoiType> types = OsmUtil.classify(tags);

		List<String> names = OsmTypes.names(types);
		content.ac(HTML.h1(Joiner.on(", ").join(names)));

		// clear div to avoid close button to interfere with other column
		// content
		content.ac(HTML.div()).attr("style", "clear:both");

		Div row = content.ac(Bootstrap.row());
		Div col1 = row.ac(HTML.div("col-12 col-md-6"));
		Div col2 = row.ac(HTML.div("col-12 col-md-6"));

		map(col1, node);
		data(col2, node);

		P p = col2.ac(HTML.p());
		p.appendText(
				"Angaben falsch, oder es fehlt was? Sag uns gerne bescheid: ");
		p.ac(HTML.a(String.format(
				"mailto:team@waldbrand-app.de?subject=Feedback: node id %d", id,
				node.getId()), "team@waldbrand-app.de"));

		OsmAttributionUtil.attribution(content);
	}

	private void data(Div container, OsmNode node)
	{
		Table table = container.ac(HTML.table());
		table.addClass("table");

		for (int i = 0; i < node.getNumberOfTags(); i++) {
			OsmTag tag = node.getTag(i);
			row(table, tag.getKey(), tag.getValue());
		}

		TableRow rowLinkOsm = table.row();
		rowLinkOsm.cell("OSM");
		rowLinkOsm.cell(HTML.a(
				String.format("https://www.openstreetmap.org/node/%d", id),
				String.format("Node %d", id)));
	}

	private void row(Table table, String val1, String val2)
	{
		TableRow row = table.row();
		row.cell(val1);
		row.cell(val2);
	}

	private void map(Div container, OsmNode node)
	{
		Head head = builder.getHead();
		MapUtil.head(head);

		MapUtil.addMap(container, node.getLatitude(), node.getLongitude(), 17);

		MapUtil.addMarkerDef(container, "red", "fa", "tint");

		Script script = container.ac(HTML.script());
		StringBuilder code = new StringBuilder();

		MapUtil.markerStart(code);
		MapUtil.addMarker(code, node.getLatitude(), node.getLongitude(),
				"Node " + id);
		script.ac(new DataNode(code.toString()));
		MapUtil.markerEnd(container, code);
	}

}
