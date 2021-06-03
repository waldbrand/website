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
import java.io.InputStream;
import java.time.LocalDateTime;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;

import de.topobyte.gson.GsonUtil;
import de.waldbrand.app.website.stats.model.AggregatedStats;
import de.waldbrand.app.website.stats.model.osmcha.Changesets;

public class StatsFetcher
{

	final static Logger logger = LoggerFactory.getLogger(StatsFetcher.class);

	private String authToken;

	private CloseableHttpClient client = HttpClientBuilder.create()
			.disableRedirectHandling()
			.setUserAgent(
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.96 Safari/537.36")
			.build();

	public StatsFetcher(String authToken)
	{
		this.authToken = authToken;
	}

	public AggregatedStats fetch()
			throws UnsupportedOperationException, IOException
	{
		LocalDateTime now = LocalDateTime.now();
		String link = OsmCha.link(now);
		logger.debug(String.format("fetching stats from: '%s'", link));

		ChangesetParser parser = new ChangesetParser();
		StatsAggregator aggregator = new StatsAggregator(now);

		int page = 0;
		int N_TRIES = 2;
		main: while (true) {
			page++;
			for (int i = 0; i < N_TRIES; i++) {
				HttpGet get = new HttpGet(link);
				get.setHeader("Authorization", "Token " + authToken);
				try (CloseableHttpResponse response = client.execute(get)) {
					int status = response.getStatusLine().getStatusCode();
					if (status == HttpStatus.SC_OK) {
						logger.debug("received data");
						HttpEntity entity = response.getEntity();
						InputStream input = entity.getContent();
						JsonElement json = GsonUtil.parse(input);
						Changesets changesets = parser.parse(json);
						aggregator.aggregate(changesets);
						String next = changesets.getNext();
						if (next != null) {
							link = next;
							continue main;
						}
						break main;
					} else {
						logger.warn(String.format("page %d, try %d: status: %d",
								page, i, status));
					}
				}
			}
			logger.warn(String.format("failed to fetch stats, page %d", page));
			break main;
		}

		return aggregator.getStats();
	}

}
