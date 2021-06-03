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

package de.waldbrand.app.website.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.luqe.jdbc.database.Database;
import de.waldbrand.app.website.Config;

public class DatabasePool
{

	final static Logger logger = LoggerFactory.getLogger(DatabasePool.class);

	private static BasicDataSource pool = new BasicDataSource();

	static {
		pool.setUrl("jdbc:sqlite:" + Config.INSTANCE.getDatabase());
		pool.setMinIdle(1);
		pool.setMaxIdle(2);
		pool.setMaxOpenPreparedStatements(100);
		pool.setDefaultAutoCommit(false);
	}

	public static Connection getConnection() throws SQLException
	{
		return pool.getConnection();
	}

	public static Database getDatabase() throws SQLException
	{
		Connection connection = getConnection();
		return new Database(connection);
	}

	public static void close()
	{
		try {
			pool.close();
		} catch (SQLException e) {
			logger.error("Error while closing database pool", e);
		}
	}

}
