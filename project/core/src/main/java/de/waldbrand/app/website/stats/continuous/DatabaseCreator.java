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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jsqltables.dialect.SqliteDialect;
import de.topobyte.jsqltables.table.QueryBuilder;
import de.topobyte.jsqltables.table.Table;
import de.topobyte.luqe.iface.IConnection;
import de.topobyte.luqe.iface.QueryException;
import de.waldbrand.app.website.stats.continuous.model.ChangesetsTable;
import de.waldbrand.app.website.stats.continuous.model.Tables;

public class DatabaseCreator
{

	final static Logger logger = LoggerFactory.getLogger(DatabaseCreator.class);

	private IConnection connection;
	private Connection jdbcConnection;

	public DatabaseCreator(IConnection connection, Connection jdbcConnection)
	{
		this.connection = connection;
		this.jdbcConnection = jdbcConnection;
	}

	public void dropTables() throws QueryException, SQLException
	{
		List<Table> tables = Arrays.asList( //
				Tables.STATUS, //
				Tables.CHANGESETS //
		);
		for (Table table : tables) {
			String dropStatement = String.format("drop table %s",
					table.getName());
			connection.execute(dropStatement);
		}
		jdbcConnection.commit();
	}

	public void createTables() throws QueryException, SQLException
	{
		QueryBuilder qb = new QueryBuilder(new SqliteDialect());

		List<Table> tables = Arrays.asList( //
				Tables.STATUS, //
				Tables.CHANGESETS //
		);

		List<String> creates = new ArrayList<>();
		for (Table table : tables) {
			creates.add(qb.create(table, true));
		}

		for (String create : creates) {
			logger.info(create);
		}

		for (String create : creates) {
			connection.execute(create);
		}

		jdbcConnection.commit();
	}

	public void createIndexes() throws QueryException
	{
		createIndex(Tables.CHANGESETS, "changeset_id",
				ChangesetsTable.COLNAME_ID);
	}

	private void createIndex(Table table, String name, String... columns)
			throws QueryException
	{
		String statement = table.createIndex(name, true, columns);
		connection.execute(statement);
	}

}
