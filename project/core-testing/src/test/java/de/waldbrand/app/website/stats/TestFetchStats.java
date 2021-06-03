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
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.melon.commons.io.Resources;
import de.waldbrand.app.website.stats.model.AggregatedStats;

public class TestFetchStats
{

	final static Logger logger = LoggerFactory.getLogger(TestFetchStats.class);

	public static void main(String[] args)
			throws UnsupportedOperationException, IOException
	{
		Properties secureConfig = new Properties();
		try (InputStream input = Resources.stream("secure.properties")) {
			secureConfig.load(input);
		} catch (Throwable e) {
			logger.error("Unable to load secure configuration", e);
		}

		StatsFetcher fetcher = new StatsFetcher(
				secureConfig.getProperty("osmcha.token"));
		AggregatedStats stats = fetcher.fetch();

		System.out.println(
				String.format("Number of users: %d", stats.getUsers().size()));
		System.out.println(String.format("Created: %d", stats.getCreated()));
		System.out.println(String.format("Modified: %d", stats.getModified()));
		System.out.println(String.format("Deleted: %d", stats.getDeleted()));
	}

}
