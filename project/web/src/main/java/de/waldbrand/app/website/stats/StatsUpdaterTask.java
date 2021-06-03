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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.webgun.scheduler.SchedulerTask;
import de.waldbrand.app.website.Config;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.stats.model.AggregatedStats;

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
		tryUpdate();
	}

	private void tryUpdate()
	{
		StatsFetcher statsFetcher = new StatsFetcher(
				Config.INSTANCE.getOsmChaToken());

		try {
			AggregatedStats stats = statsFetcher.fetch();
			Website.INSTANCE.setStats(stats);
		} catch (UnsupportedOperationException | IOException e) {
			logger.warn("Error while fetching stats", e);
		}
	}

}
