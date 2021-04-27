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
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Head;
import de.topobyte.jsoup.components.P;
import de.topobyte.jsoup.components.Table;
import de.topobyte.jsoup.components.TableCell;
import de.topobyte.jsoup.components.TableHead;
import de.topobyte.jsoup.components.TableRow;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.icons.IconMapping;
import de.waldbrand.app.website.icons.IconUtil;
import de.waldbrand.app.website.osm.OsmTypes;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.osm.model.OsmPoi;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.util.MapUtil;

public class OsmMappingGenerator extends SimpleBaseGenerator
{

	public OsmMappingGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content() throws IOException
	{
		Head head = builder.getHead();
		MapUtil.head(head);

		List<String> names = OsmTypes
				.multiNames(Arrays.asList(PoiType.values()));

		content.ac(HTML.h2("Wasserentnahmestellen (OpenStreetMap)"));
		P p = content.ac(HTML.p());
		p.appendText("Typen: " + Joiner.on(", ").join(names));

		Table table = content.ac(HTML.table());
		table.addClass("table");
		TableHead tableHead = table.head();
		TableRow headRow = tableHead.row();
		headRow.cell("Art");
		headRow.cell("Anzahl");
		headRow.cell("Tags");
		headRow.cell("Icon");

		int total = 0;

		for (PoiType type : PoiType.values()) {
			List<OsmPoi> pois = Website.INSTANCE.getData().getTypeToPois()
					.get(type);
			total += pois.size();
			TableRow row = table.row();
			row.cell(type.getName());
			row.cell(String.format("%d", pois.size()));
			tagDef(row.cell(), type);
			IconUtil.icon(row.cell(), IconMapping.get(type));
		}

		TableRow row = table.row();
		row.cell("Insgesamt");
		row.cell(String.format("%d", total));
		row.cell();
		row.cell();

		OsmAttributionUtil.attribution(content);
	}

	private void tagDef(TableCell cell, PoiType type)
	{
		Joiner joiner = Joiner.on(", ");
		cell.at(joiner.join(type.getTags()));
		if (type.getMissingKeys() != null) {
			cell.at(";");
			cell.ac(HTML.br());
			cell.at("nicht: ");
			cell.at(joiner.join(Collections2.transform(type.getMissingKeys(),
					s -> s + "=*")));
		}
	}

}
