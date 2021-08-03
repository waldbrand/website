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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.topobyte.jsqltables.dialect.Dialect;
import de.topobyte.jsqltables.dialect.SqliteDialect;
import de.topobyte.jsqltables.query.Select;
import de.topobyte.jsqltables.query.Update;
import de.topobyte.jsqltables.query.order.OrderDirection;
import de.topobyte.jsqltables.query.order.SingleOrder;
import de.topobyte.jsqltables.table.QueryBuilder;
import de.topobyte.jsqltables.table.Table;
import de.topobyte.luqe.iface.IConnection;
import de.topobyte.luqe.iface.IPreparedStatement;
import de.topobyte.luqe.iface.IResultSet;
import de.topobyte.luqe.iface.QueryException;
import de.waldbrand.app.website.stats.continuous.model.ChangesetsTable;
import de.waldbrand.app.website.stats.continuous.model.DbChangeset;
import de.waldbrand.app.website.stats.continuous.model.StatusTable;
import de.waldbrand.app.website.stats.continuous.model.Tables;
import lombok.Getter;

public class StatsDao
{

	@Getter
	private IConnection connection;

	@Getter
	private Dialect dialect = new SqliteDialect();

	private QueryBuilder qb = new QueryBuilder(dialect);

	public StatsDao(IConnection connection)
	{
		this.connection = connection;
	}

	/*
	 * Status
	 */

	public boolean hasSequence() throws QueryException
	{
		Select select = new Select(Tables.STATUS);
		try (IPreparedStatement stmt = connection
				.prepareStatement(select.sql())) {
			try (IResultSet results = stmt.executeQuery()) {
				return results.next();
			}
		}
	}

	public long getSequence() throws QueryException
	{
		Select select = new Select(Tables.STATUS);
		try (IPreparedStatement stmt = connection
				.prepareStatement(select.sql())) {
			try (IResultSet results = stmt.executeQuery()) {
				while (results.next()) {
					long status = results.getLong(1);
					return status;
				}
			}
		}
		return -1;
	}

	public void insertSequence(long sequence) throws QueryException
	{
		String insert = qb.insert(Tables.STATUS);
		IPreparedStatement stmt = connection.prepareStatement(insert);

		stmt.setLong(1, sequence);

		IResultSet results = stmt.executeQuery();
		results.close();
	}

	public void updateSequence(long sequence) throws QueryException
	{
		Update update = new Update(Tables.STATUS);
		update.addColum(StatusTable.COLNAME_REPLICATION_SEQUENCE);
		IPreparedStatement stmt = connection.prepareStatement(update.sql());

		stmt.setLong(1, sequence);

		IResultSet results = stmt.executeQuery();
		results.close();
	}

	/*
	 * Changesets
	 */

	/*
	 * Get
	 */

	public List<DbChangeset> getChangesets() throws QueryException
	{
		List<DbChangeset> list = new ArrayList<>();

		Table table = Tables.CHANGESETS;
		Select select = new Select(table);
		select.order(new SingleOrder(select.getMainTable(),
				ChangesetsTable.COLNAME_ID, OrderDirection.ASC));
		try (IPreparedStatement stmt = connection
				.prepareStatement(select.sql())) {
			try (IResultSet results = stmt.executeQuery()) {
				while (results.next()) {
					list.add(entry(results));
				}
			}
		}

		return list;
	}

	private DbChangeset entry(IResultSet results) throws QueryException
	{
		int c = 1;
		long id = results.getLong(c++);
		long createdAt = results.getLong(c++);
		long closedAt = results.getLong(c++);
		boolean open = results.getBoolean(c++);
		String username = results.getString(c++);
		long userId = results.getLong(c++);
		int numChanges = results.getInt(c++);
		int numComments = results.getInt(c++);
		return new DbChangeset(id, createdAt, closedAt, open, username, userId,
				numChanges, numComments);
	}

	/*
	 * Insert
	 */

	private Map<Table, IPreparedStatement> inserts = new HashMap<>();

	public void insertChangeset(DbChangeset cs) throws QueryException
	{
		Table table = Tables.CHANGESETS;
		IPreparedStatement stmt = inserts.get(table);
		if (stmt == null) {
			String insert = qb.insert(table);
			stmt = connection.prepareStatement(insert);
			inserts.put(table, stmt);
		}

		int idx = 1;
		stmt.setLong(idx++, cs.getId());
		stmt.setLong(idx++, cs.getCreatedAt());
		stmt.setLong(idx++, cs.getClosedAt());
		stmt.setBoolean(idx++, cs.isOpen());
		stmt.setString(idx++, cs.getUser());
		stmt.setLong(idx++, cs.getUserId());
		stmt.setInt(idx++, cs.getNumChanges());
		stmt.setInt(idx++, cs.getNumComments());

		IResultSet results = stmt.executeQuery();
		results.close();
	}

}
