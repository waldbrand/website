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

package de.waldbrand.app.website.pages.other;

import static de.waldbrand.app.website.widgets.Cards.card;
import static de.waldbrand.app.website.widgets.Cards.emptyCard;

import java.util.Arrays;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slimjars.dist.gnu.trove.set.TLongSet;
import com.slimjars.dist.gnu.trove.set.hash.TLongHashSet;

import de.topobyte.cachebusting.CacheBusting;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Div;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.content.MiscContent;
import de.waldbrand.app.website.links.LinkDefs;
import de.waldbrand.app.website.pages.base.DatabaseBaseGenerator;
import de.waldbrand.app.website.stats.continuous.StatsDao;
import de.waldbrand.app.website.stats.continuous.model.DbChangeset;

public class EditorLandingPage extends DatabaseBaseGenerator
{

	final static Logger logger = LoggerFactory
			.getLogger(EditorLandingPage.class);

	public EditorLandingPage(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content()
	{
		content.ac(HTML.h1()
				.append("Editor für Wasser&shy;ent&shy;nahme&shy;stellen"));

		String linkEditor = LinkDefs.EDITOR.getLink();

		Div deck = content.ac(HTML.div("row"));

		card(deck, "markdown/de/landing-editor-hinweise.md",
				Arrays.asList(HTML.a(linkEditor, "Los geht's, zum Editor")));

		card(deck, "/" + CacheBusting.resolve("images/editor.png"), linkEditor,
				null, Arrays.asList(), "So sieht der Editor in Benutzung aus.");

		try {
			stats(deck);
		} catch (QueryException e) {
			logger.error("Error while fetching stats from database", e);
		}

		card(deck, "markdown/de/landing-editor-testversion.md",
				Arrays.asList(HTML.a(LinkDefs.EDITOR_TEST.getLink(),
						"Zur Testversion des Editors")));

		MiscContent.rowSponsors(content);
	}

	private void stats(Div deck) throws QueryException
	{
		StatsDao statsDao = new StatsDao(db.getConnection());
		List<DbChangeset> changesets = statsDao.getChangesets();
		int totalChanges = 0;
		TLongSet users = new TLongHashSet();
		for (DbChangeset changeset : changesets) {
			if (changeset.isOpen()) {
				continue;
			}
			totalChanges += changeset.getNumChanges();
			users.add(changeset.getUserId());
		}

		Div body = emptyCard(deck, "Statistiken");
		body.ac(HTML.p()).at(String.format(
				"Bis heute haben mit dem Editor %d Nutzer:innen %d Einträge"
						+ " hinzugefügt oder Änderungen vorgenommen."
						+ " 💪 Vielen Dank an alle, die schon mitgearbeitet haben! 💖",
				users.size(), totalChanges));

		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			body.ac(HTML.p()).ac(HTML.a(LinkDefs.OSM_CONTRIBUTIONS.getLink(),
					"Details zu unseren Beiträgen zu OSM"));
		}
	}

}
