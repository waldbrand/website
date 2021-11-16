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

package de.waldbrand.app.website.pages.internal;

import static de.topobyte.jsoup.HTML.a;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.A;
import de.topobyte.jsoup.components.Table;
import de.topobyte.jsoup.components.TableCell;
import de.topobyte.jsoup.components.TableRow;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.links.LinkDefs;
import de.waldbrand.app.website.pages.base.DatabaseBaseGenerator;
import de.waldbrand.app.website.stats.continuous.StatsDao;
import de.waldbrand.app.website.stats.continuous.model.DbChangeset;

public class OsmContributionsPage extends DatabaseBaseGenerator
{

	public OsmContributionsPage(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content()
			throws IOException, QueryException, SQLException, ServletException
	{
		content.ac(HTML.h1("Beiträge zu OSM mit dem Editor"));

		StatsDao dao = new StatsDao(db.getConnection());
		List<DbChangeset> changesets = dao.getChangesets();

		Collections.sort(changesets,
				(a, b) -> Long.compare(a.getCreatedAt(), b.getCreatedAt()));

		Set<Long> firstChangesets = new HashSet<>();

		int rankCount = 0;
		Map<Long, Integer> userToRank = new HashMap<>();
		for (DbChangeset changeset : changesets) {
			long userId = changeset.getUserId();
			if (!userToRank.containsKey(userId)) {
				userToRank.put(userId, ++rankCount);
				firstChangesets.add(changeset.getId());
			}
		}

		Collections.reverse(changesets);

		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.appendValue(YEAR, 4).appendLiteral("-")
				.appendValue(MONTH_OF_YEAR, 2).appendLiteral("-")
				.appendValue(DAY_OF_MONTH, 2).appendLiteral(" ")
				.appendValue(HOUR_OF_DAY, 2).appendLiteral(":")
				.appendValue(MINUTE_OF_HOUR, 2).appendLiteral(":")
				.appendValue(SECOND_OF_MINUTE, 2).toFormatter();

		Table table = content.ac(HTML.table());
		table.addClass("table");
		TableRow headrow = table.head().row();
		headrow.cell("Datum");
		headrow.cell("#");
		headrow.cell("Nutzer");
		headrow.cell("Changeset");
		headrow.cell("Änderungen");
		headrow.cell("Kommentare");
		for (DbChangeset changeset : changesets) {
			if (changeset.isOpen()) {
				continue;
			}
			boolean first = firstChangesets.contains(changeset.getId());
			int rank = userToRank.get(changeset.getUserId());
			TableRow row = table.row();
			LocalDateTime createdAt = LocalDateTime.ofEpochSecond(
					changeset.getCreatedAt() / 1000, 0, ZoneOffset.UTC);
			row.cell(formatter.format(createdAt));
			TableCell cr = row.cell().at(rank + ". ");
			if (first) {
				cr.attr("style", "font-weight: bold; color: #090");
			}
			row.cell().ac(linkUser(changeset));
			row.cell().ac(linkChangeset(changeset.getId()));
			row.cell(Integer.toString(changeset.getNumChanges()));
			row.cell(Integer.toString(changeset.getNumComments()));
		}
	}

	private A linkUser(DbChangeset changeset)
	{
		return a(LinkDefs.OSM_USER_FORWARD.getLink(changeset.getUserId()),
				changeset.getUser());
	}

	private A linkChangeset(long id)
	{
		return a(
				String.format("https://www.openstreetmap.org/changeset/%d", id),
				Long.toString(id));
	}

}
