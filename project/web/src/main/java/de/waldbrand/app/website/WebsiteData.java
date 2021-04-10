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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.luqe.iface.QueryException;

public class WebsiteData
{

	final static Logger logger = LoggerFactory.getLogger(WebsiteData.class);

	public static void load()
	{
		logger.info("loading data...");
		try {
			DataLoader dataLoader = new DataLoader();
			dataLoader.loadData(Config.INSTANCE.getFileWesData(),
					Config.INSTANCE.getFileOsmData());
			Website.INSTANCE.setData(dataLoader.getData());
		} catch (QueryException | IOException e) {
			logger.error("Error while loading data", e);
		}
	}

}
