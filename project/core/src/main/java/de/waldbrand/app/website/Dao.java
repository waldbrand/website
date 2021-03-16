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

import java.util.ArrayList;
import java.util.List;

import de.topobyte.jsqltables.dialect.Dialect;
import de.topobyte.jsqltables.dialect.SqliteDialect;
import de.topobyte.jsqltables.query.Select;
import de.topobyte.jsqltables.query.order.OrderDirection;
import de.topobyte.jsqltables.query.order.SingleOrder;
import de.topobyte.jsqltables.query.select.NormalColumn;
import de.topobyte.jsqltables.table.QueryBuilder;
import de.topobyte.jsqltables.table.Table;
import de.topobyte.luqe.iface.IConnection;
import de.topobyte.luqe.iface.IPreparedStatement;
import de.topobyte.luqe.iface.IResultSet;
import de.topobyte.luqe.iface.QueryException;
import de.waldbrand.app.website.model.Poi;
import lombok.Getter;

public class Dao
{

	@Getter
	private IConnection connection;

	@Getter
	private Dialect dialect = new SqliteDialect();

	private QueryBuilder qb = new QueryBuilder(dialect);

	public Dao(IConnection connection)
	{
		this.connection = connection;
	}

	/*
	 * Get
	 */

	public List<Poi> getEntries() throws QueryException
	{
		List<Poi> list = new ArrayList<>();

		Table table = Tables.WES;
		Select select = new Select(table);
		select.addSelectColumn(
				new NormalColumn(select.getMainTable(), WesTable.COLNAME_ID));
		select.addSelectColumn(
				new NormalColumn(select.getMainTable(), WesTable.COLNAME_OART));
		select.addSelectColumn(new NormalColumn(select.getMainTable(),
				WesTable.COLNAME_BEMERKUNG));
		select.addSelectColumn(new NormalColumn(select.getMainTable(),
				WesTable.COLNAME_HOCH_W));
		select.addSelectColumn(new NormalColumn(select.getMainTable(),
				WesTable.COLNAME_RECHTS_W));
		select.order(new SingleOrder(select.getMainTable(), WesTable.COLNAME_ID,
				OrderDirection.ASC));

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

	private Poi entry(IResultSet results) throws QueryException
	{
		int c = 1;
		int id = results.getInt(c++);
		int oart = results.getInt(c++);
		String bemerkung = results.getString(c++);
		int hochW = results.getInt(c++);
		int rechtsW = results.getInt(c++);
		return new Poi(id, oart, bemerkung, hochW, rechtsW);
	}

}
