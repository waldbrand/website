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
import java.util.List;

import javax.servlet.ServletException;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.A;
import de.topobyte.jsoup.components.Table;
import de.topobyte.jsoup.components.TableRow;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.webpaths.WebPath;
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
				(a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()));

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
		headrow.cell("Nutzer");
		headrow.cell("Changeset");
		headrow.cell("Änderungen");
		headrow.cell("Kommentare");
		for (DbChangeset changeset : changesets) {
			TableRow row = table.row();
			LocalDateTime createdAt = LocalDateTime.ofEpochSecond(
					changeset.getCreatedAt() / 1000, 0, ZoneOffset.UTC);
			row.cell(formatter.format(createdAt));
			row.cell().ac(linkUser(changeset));
			row.cell().ac(linkChangeset(changeset.getId()));
			row.cell(Integer.toString(changeset.getNumChanges()));
			row.cell(Integer.toString(changeset.getNumComments()));
		}
	}

	private A linkUser(DbChangeset changeset)
	{
		return a(String.format("https://www.openstreetmap.org/user/%s",
				changeset.getUser()), changeset.getUser());
	}

	private A linkChangeset(long id)
	{
		return a(
				String.format("https://www.openstreetmap.org/changeset/%d", id),
				Long.toString(id));
	}

}
