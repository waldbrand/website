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

package de.waldbrand.app.website.stats;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.luqe.iface.QueryException;
import de.topobyte.luqe.jdbc.database.Database;
import de.topobyte.osm4j.core.access.OsmInputException;
import de.topobyte.webgun.scheduler.SchedulerTask;
import de.waldbrand.app.website.db.DatabasePool;
import de.waldbrand.app.website.stats.continuous.ChangesetDatabaseUpdater;

public class StatsUpdaterTask extends SchedulerTask
{

	final static Logger logger = LoggerFactory
			.getLogger(StatsUpdaterTask.class);

	public StatsUpdaterTask()
	{
		super("Update stats");
	}

	@Override
	public void run()
	{
		logger.info("updating stats...");
		try {
			tryUpdate();
			logger.info("done");
		} catch (SQLException | IOException | OsmInputException
				| QueryException e) {
			logger.error("error while updating changesets database", e);
		}
	}

	private void tryUpdate()
			throws SQLException, IOException, OsmInputException, QueryException
	{
		Database database = DatabasePool.getDatabase();
		ChangesetDatabaseUpdater updater = new ChangesetDatabaseUpdater(
				database);
		try {
			updater.updateDatabase();
		} catch (IOException | OsmInputException | QueryException
				| SQLException e) {
			throw e;
		} finally {
			if (database != null) {
				database.closeConnection(true);
			}
		}
	}

}
