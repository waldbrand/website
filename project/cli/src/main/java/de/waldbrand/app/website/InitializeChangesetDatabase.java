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

import static java.time.LocalDateTime.ofEpochSecond;
import static org.joda.time.DateTimeZone.UTC;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.Map;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.luqe.iface.QueryException;
import de.topobyte.luqe.jdbc.database.SqliteDatabase;
import de.topobyte.osm4j.changeset.ChangesetUtil;
import de.topobyte.osm4j.changeset.OsmChangeset;
import de.topobyte.osm4j.changeset.dynsax.OsmChangesetsHandler;
import de.topobyte.osm4j.changeset.dynsax.OsmChangesetsReader;
import de.topobyte.osm4j.core.access.OsmInputException;
import de.topobyte.osm4j.replication.ReplicationFiles;
import de.topobyte.osm4j.replication.ReplicationInfo;
import de.topobyte.osm4j.replication.ReplicationUtil;
import de.waldbrand.app.website.stats.continuous.DatabaseCreator;
import de.waldbrand.app.website.stats.continuous.StatsDao;
import de.waldbrand.app.website.stats.continuous.model.DbChangeset;

public class InitializeChangesetDatabase implements OsmChangesetsHandler
{

	final static Logger logger = LoggerFactory
			.getLogger(InitializeChangesetDatabase.class);

	private Path file;

	private CloseableHttpClient httpclient = HttpClients.createDefault();
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
		updateDatabase();
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

	private void updateDatabase() throws MalformedURLException, IOException,
			OsmInputException, QueryException, SQLException
	{
		StatsDao dao = new StatsDao(database.getConnection());
		long last = dao.getSequence();
		long start = last + 1;
		System.out.println("start sequence number: " + start);

		ReplicationUtil util = new ReplicationUtil();
		ReplicationInfo changesetInfo = util.getChangesetInfo();
		long max = changesetInfo.getSequenceNumber();
		System.out.println("last sequence number: " + max);

		long todo = max - start;
		System.out.println("files to process: " + todo);

		for (long i = start; i <= changesetInfo.getSequenceNumber(); i++) {
			fetchAndParse(i);
			dao.updateSequence(i);
			database.getJdbcConnection().commit();
		}
	}

	private long sequence;

	private void fetchAndParse(long sequence)
			throws ClientProtocolException, IOException, OsmInputException
	{
		this.sequence = sequence;
		String url = ReplicationFiles.changesets(sequence);
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse response = httpclient.execute(get);
		HttpEntity entity = response.getEntity();

		InputStream cinput = entity.getContent();
		InputStream input = new GzipCompressorInputStream(cinput);

		OsmChangesetsReader reader = new OsmChangesetsReader(input);
		reader.setHandler(this);
		reader.read();
	}

	@Override
	public void handle(OsmChangeset changeset) throws IOException
	{
		Map<String, String> tags = ChangesetUtil.getTagsAsMap(changeset);
		String createdBy = tags.get("created_by");
		if (createdBy == null) {
			return;
		}
		if (createdBy.startsWith("MapComplete")) {
			String theme = tags.get("theme");
			if (!"waldbrand".equals(theme)) {
				return;
			}
			long id = changeset.getId();
			long createdAt = changeset.getCreatedAt();
			long closedAt = changeset.getClosedAt();
			String user = changeset.getUser();
			long uid = changeset.getUid();
			int numChanges = changeset.getNumChanges();
			boolean open = changeset.isOpen();
			System.out.println(String.format(
					"%d: %d open? %b %s - %s %s (%d) %s %d changes", sequence,
					id, open,
					ofEpochSecond(createdAt / 1000, 0, ZoneOffset.UTC),
					ofEpochSecond(closedAt / 1000, 0, ZoneOffset.UTC), user,
					uid, theme, numChanges));
			try {
				dao.insertChangeset(new DbChangeset(id, createdAt, closedAt,
						open, user, uid, numChanges, numChanges));
			} catch (QueryException e) {
				logger.error("Error while inserting changeset", e);
			}
		}
	}

	@Override
	public void complete() throws IOException
	{
		// do nothing
	}

}
