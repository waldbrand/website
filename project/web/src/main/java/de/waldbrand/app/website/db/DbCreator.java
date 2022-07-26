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

import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jsqltables.dialect.SqliteDialect;
import de.topobyte.jsqltables.table.QueryBuilder;
import de.topobyte.jsqltables.table.Table;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.luqe.jdbc.database.Database;
import de.topobyte.weblogin.LoginTables;
import de.waldbrand.app.website.Config;

public class DbCreator
{

	final static Logger logger = LoggerFactory.getLogger(DbCreator.class);

	public void createDatabase() throws SQLException
	{
		boolean dbExists = Files.exists(Config.INSTANCE.getDatabase());

		Database database = new Database(
				"jdbc:sqlite:" + Config.INSTANCE.getDatabase());

		if (!dbExists) {
			logger.info("Database doesn't exists, creating it");
			createAllTables(database);
		}

		database.getJdbcConnection().close();
	}

	private void createAllTables(Database database)
	{
		QueryBuilder qb = new QueryBuilder(new SqliteDialect());

		List<Table> tables = Arrays.asList(LoginTables.USERS,
				LoginTables.LOGIN);

		List<String> createStatements = new ArrayList<>();

		for (Table table : tables) {
			createStatements.add(qb.create(table, true));
		}

		for (String statement : createStatements) {
			logger.info(statement);
		}

		try {
			for (String statement : createStatements) {
				database.getConnection().execute(statement);
			}

			database.getJdbcConnection().commit();
		} catch (QueryException | SQLException e) {
			logger.error("error while creating tables", e);
		}
	}

}
