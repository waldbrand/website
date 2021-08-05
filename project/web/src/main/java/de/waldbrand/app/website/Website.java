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

import de.topobyte.webgun.scheduler.Scheduler;
import de.topobyte.webgun.scheduler.SchedulerTask;
import de.topobyte.weblogin.WebsiteInfo;
import de.waldbrand.app.website.lbforst.model.Data;
import de.waldbrand.app.website.stats.model.AggregatedStats;
import lombok.Getter;
import lombok.Setter;

public class Website implements WebsiteInfo
{

	public static final String TITLE = "Waldbrand-App";
	public static final String CONTACT = "team@waldbrand-app.de";

	public static final Website INSTANCE = new Website();

	@Getter
	@Setter
	private CacheBuster cacheBuster;

	@Getter
	@Setter
	private Data data;

	@Getter
	@Setter
	private Scheduler<SchedulerTask> scheduler;

	@Getter
	@Setter
	private AggregatedStats stats;

	@Override
	public String getName()
	{
		return TITLE;
	}

	@Override
	public String getAdminContactEmail()
	{
		return CONTACT;
	}

}
