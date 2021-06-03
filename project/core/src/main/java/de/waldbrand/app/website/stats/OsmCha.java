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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.topobyte.webgun.urls.LinkBuilder;

public class OsmCha
{

	public static String link(LocalDateTime time)
	{
		LinkBuilder lb = new LinkBuilder(
				"https://osmcha.org/api/v1/changesets/");
		lb.addParameter("date__gte", "2021-01-01");
		DateTimeFormatter pattern = DateTimeFormatter
				.ofPattern("yyyy-MM-dd HH:mm:ss");
		String date = pattern.format(time);
		lb.addParameter("date__lte", date);
		lb.addParameter("editor", "mapcomplete");
		lb.addParameter("metadata", "theme=waldbrand");
		lb.addParameter("page", "1");
		lb.addParameter("page_size", "1000");
		return lb.toString();
	}

}
