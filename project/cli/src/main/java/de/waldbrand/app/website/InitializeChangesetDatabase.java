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

package de.waldbrand.app.website;

import static org.joda.time.DateTimeZone.UTC;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.luqe.iface.QueryException;
import de.topobyte.luqe.jdbc.database.SqliteDatabase;
import de.topobyte.osm4j.core.access.OsmInputException;
import de.topobyte.osm4j.replication.ReplicationInfo;
import de.topobyte.osm4j.replication.ReplicationUtil;
import de.waldbrand.app.website.stats.continuous.ChangesetDatabaseUpdater;
import de.waldbrand.app.website.stats.continuous.DatabaseCreator;
import de.waldbrand.app.website.stats.continuous.StatsDao;

public class InitializeChangesetDatabase
{

	final static Logger logger = LoggerFactory
			.getLogger(InitializeChangesetDatabase.class);

	private Path file;

	private DateTime earliestDate = new DateTime(2021, 05, 19, 8, 16, 0, UTC);

	public InitializeChangesetDatabase(Path file)
	{
		this.file = file;
	}

	private SqliteDatabase database;
	private StatsDao dao;

	public void execute() throws MalformedURLException, IOException,
			OsmInputException, QueryException, SQLException
	{
		database = new SqliteDatabase(file);
		dao = new StatsDao(database.getConnection());
		initDatabase();
		ChangesetDatabaseUpdater updater = new ChangesetDatabaseUpdater(
				database);
		updater.updateDatabase();
	}

	private void initDatabase() throws QueryException, SQLException,
			MalformedURLException, IOException
	{
		DatabaseCreator creator = new DatabaseCreator(database.getConnection(),
				database.getJdbcConnection());
		creator.createTables();
		creator.createIndexes();

		if (!dao.hasSequence()) {
			ReplicationUtil util = new ReplicationUtil();
			ReplicationInfo found = util.findChangeset(earliestDate);
			util.closeHttpClient();
			long start = found.getSequenceNumber();
			System.out.println("initializing start sequence number: " + start);
			dao.insertSequence(start - 1);
			database.getJdbcConnection().commit();
		}
	}

}
