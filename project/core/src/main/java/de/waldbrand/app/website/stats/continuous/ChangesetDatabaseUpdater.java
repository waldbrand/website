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

package de.waldbrand.app.website.stats.continuous;

import static java.time.LocalDateTime.ofEpochSecond;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.Map;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.luqe.iface.QueryException;
import de.topobyte.luqe.jdbc.database.Database;
import de.topobyte.osm4j.changeset.ChangesetUtil;
import de.topobyte.osm4j.changeset.OsmChangeset;
import de.topobyte.osm4j.changeset.dynsax.OsmChangesetsHandler;
import de.topobyte.osm4j.changeset.dynsax.OsmChangesetsReader;
import de.topobyte.osm4j.core.access.OsmInputException;
import de.topobyte.osm4j.replication.ReplicationFiles;
import de.topobyte.osm4j.replication.ReplicationInfo;
import de.topobyte.osm4j.replication.ReplicationUtil;
import de.waldbrand.app.website.stats.continuous.model.DbChangeset;

public class ChangesetDatabaseUpdater implements OsmChangesetsHandler
{

	final static Logger logger = LoggerFactory
			.getLogger(ChangesetDatabaseUpdater.class);

	private CloseableHttpClient httpclient;

	private StatsDao dao;
	private Database database;

	public ChangesetDatabaseUpdater(Database database)
	{
		this.database = database;
		dao = new StatsDao(database.getConnection());

		int timeout = 10;
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000)
				.setSocketTimeout(timeout * 1000).build();
		httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config)
				.build();
	}

	private static Object sync = new Object();
	private static boolean running = false;

	public void updateDatabase() throws MalformedURLException, IOException,
			OsmInputException, QueryException, SQLException
	{
		synchronized (sync) {
			if (running) {
				logger.info(
						"an updater is currently running already, doing nothing.");
				return;
			}
			running = true;
		}
		try {
			long last = dao.getSequence();
			long start = last + 1;
			logger.info("start sequence number: " + start);

			ReplicationUtil util = new ReplicationUtil(httpclient);
			ReplicationInfo changesetInfo = util.getChangesetInfo();
			long max = changesetInfo.getSequenceNumber();
			logger.info("last sequence number: " + max);

			long todo = max - start + 1;
			logger.info("files to process: " + todo);

			int nDone = 0;
			int percentDone = 0;

			for (long i = start; i <= changesetInfo.getSequenceNumber(); i++) {
				fetchAndParse(i);
				dao.updateSequence(i);
				database.getJdbcConnection().commit();
				nDone++;
				int percentDoneNow = (int) Math
						.round(nDone / (double) todo * 100);
				if (percentDoneNow > percentDone) {
					percentDone = percentDoneNow;
					logger.info(String.format("done: %d%%", percentDone));
				}
			}
		} finally {
			synchronized (sync) {
				running = false;
			}
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
			int numComments = changeset.getCommentsCount();
			boolean open = changeset.isOpen();
			logger.info(String.format(
					"%d: %d open? %b %s - %s %s (%d) %s %d changes", sequence,
					id, open,
					ofEpochSecond(createdAt / 1000, 0, ZoneOffset.UTC),
					ofEpochSecond(closedAt / 1000, 0, ZoneOffset.UTC), user,
					uid, theme, numChanges));
			try {
				dao.insertChangeset(new DbChangeset(id, createdAt, closedAt,
						open, user, uid, numChanges, numComments));
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
