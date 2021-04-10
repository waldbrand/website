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

package de.waldbrand.app.website.pages.wes.stats;

import java.io.IOException;
import java.util.function.Function;

import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.TreeMultiset;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Head;
import de.topobyte.jsoup.components.P;
import de.topobyte.jsoup.components.Table;
import de.topobyte.jsoup.components.TableHead;
import de.topobyte.jsoup.components.TableRow;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.model.Poi;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.pages.wes.WesAttributionUtil;
import de.waldbrand.app.website.util.MapUtil;

public class WesStatsGenerator extends SimpleBaseGenerator
{

	private String colTitle1;
	private String description;
	private Function<Poi, Integer> dataGetter;

	public WesStatsGenerator(WebPath path, String colTitle1, String description,
			Function<Poi, Integer> dataGetter)
	{
		super(path);
		this.colTitle1 = colTitle1;
		this.description = description;
		this.dataGetter = dataGetter;
	}

	@Override
	protected void content() throws IOException
	{
		Head head = builder.getHead();
		MapUtil.head(head);

		content.ac(HTML.h2("Wasserentnahmestellen"));
		P p = content.ac(HTML.p());
		p.appendText(description);

		Multiset<Integer> histogram = TreeMultiset.create();
		for (Poi poi : Website.INSTANCE.getData().getIdToPoi().values()) {
			histogram.add(dataGetter.apply(poi));
		}

		Table table = content.ac(HTML.table());
		table.addClass("table");
		TableHead tableHead = table.head();
		TableRow headRow = tableHead.row();
		headRow.cell(colTitle1);
		headRow.cell("Anzahl");

		for (Entry<Integer> entry : histogram.entrySet()) {
			TableRow row = table.row();
			int oart = entry.getElement();
			row.cell().at(String.format("%d", oart));
			row.cell().at(String.format("%d", entry.getCount()));
		}

		WesAttributionUtil.attribution(content);
	}

}
