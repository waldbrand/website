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

package de.waldbrand.app.website.pages.wes;

import org.jsoup.nodes.DataNode;
import org.locationtech.jts.geom.Coordinate;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.bootstrap4.Bootstrap;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.components.Head;
import de.topobyte.jsoup.components.P;
import de.topobyte.jsoup.components.Script;
import de.topobyte.jsoup.components.Table;
import de.topobyte.jsoup.components.TableRow;
import de.topobyte.webgun.exceptions.PageNotFoundException;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.lbforst.NameUtil;
import de.waldbrand.app.website.lbforst.model.Poi;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.util.MapUtil;
import de.waldbrand.app.website.util.MarkerShape;

public class WesDetailGenerator extends SimpleBaseGenerator
{

	private int id;

	public WesDetailGenerator(WebPath path, int id)
	{
		super(path);
		this.id = id;
	}

	@Override
	protected void content()
	{
		Poi poi = Website.INSTANCE.getData().getIdToPoi().get(id);
		if (poi == null) {
			throw new PageNotFoundException();
		}

		// close button
		Div divClose = content.ac(HTML.div()).attr("style",
				"float:right; margin: 1em");

		divClose.ac(HTML.a("javascript:window.history.back()", "X"))
				.addClass("btn btn-secondary");

		// headline
		String name = NameUtil.getName(poi);
		content.ac(HTML.h1(name));

		// clear div to avoid close button to interfere with other column
		// content
		content.ac(HTML.div()).attr("style", "clear:both");

		Div row = content.ac(Bootstrap.row());
		Div col1 = row.ac(HTML.div("col-12 col-md-6"));
		Div col2 = row.ac(HTML.div("col-12 col-md-6"));

		map(col1, poi);
		data(col2, poi);

		P p = col2.ac(HTML.p());
		p.appendText(
				"Angaben falsch, oder es fehlt was? Sag uns gerne bescheid: ");
		p.ac(HTML.a(String.format(
				"mailto:team@waldbrand-app.de?subject=Feedback: %s (id=%s)",
				name, poi.getId()), "team@waldbrand-app.de"));

		WesAttributionUtil.attribution(content);
	}

	private void data(Div container, Poi poi)
	{
		Table table = container.ac(HTML.table());
		table.addClass("table");

		String name = NameUtil.getName(poi);

		row(table, "Name", name);
		row(table, "ID", poi.getId());
		row(table, "oart", String.format("%s (%d)",
				NameUtil.typeName(poi.getOart()), poi.getOart()));
		row(table, "fstatus", poi.getFstatus());
		row(table, "fkt_faehig", poi.getFktFaehig());
		row(table, "akz", poi.getAkz());
		row(table, "Baujahr", poi.getBaujahr());
		row(table, "Menge", poi.getMenge());
		row(table, "Bemerkung",
				poi.getBemerkung() != null ? poi.getBemerkung() : "-");
	}

	private void row(Table table, String val1, String val2)
	{
		TableRow row = table.row();
		row.cell(val1);
		row.cell(val2);
	}

	private void row(Table table, String val1, int val2)
	{
		row(table, val1, Integer.toString(val2));
	}

	private void row(Table table, String val1, long val2)
	{
		row(table, val1, Long.toString(val2));
	}

	private void map(Div container, Poi poi)
	{
		Head head = builder.getHead();
		MapUtil.head(head);

		Coordinate c = poi.getCoordinate();
		MapUtil.addMap(container, c.getY(), c.getX(), 17);

		MapUtil.addMarkerDef(container, MarkerShape.CIRCLE, "red", "fa",
				"fa-tint");

		Script script = container.ac(HTML.script());
		StringBuilder code = new StringBuilder();

		MapUtil.markerStart(code);
		MapUtil.addMarker(code, poi, false);
		script.ac(new DataNode(code.toString()));
		MapUtil.markerEnd(container, code);
	}

}
