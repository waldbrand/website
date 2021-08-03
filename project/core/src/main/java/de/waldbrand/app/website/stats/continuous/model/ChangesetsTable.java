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

package de.waldbrand.app.website.stats.continuous.model;

import de.topobyte.jsqltables.table.ColumnClass;
import de.topobyte.jsqltables.table.Table;

public class ChangesetsTable extends Table
{

	public static String COLNAME_ID = "id";
	public static String COLNAME_CREATED_AT = "created_at";
	public static String COLNAME_CLOSED_AT = "closed_at";
	public static String COLNAME_OPEN = "open";
	public static String COLNAME_USERNAME = "user";
	public static String COLNAME_USER_ID = "uid";
	public static String COLNAME_NUM_CHANGES = "num_changes";
	public static String COLNAME_COMMENTS_COUNT = "num_comments";

	public ChangesetsTable()
	{
		super("changesets");
		addColumn(ColumnClass.LONG, COLNAME_ID);
		addColumn(ColumnClass.LONG, COLNAME_CREATED_AT);
		addColumn(ColumnClass.LONG, COLNAME_CLOSED_AT);
		addColumn(ColumnClass.INT, COLNAME_OPEN);
		addColumn(ColumnClass.VARCHAR, COLNAME_USERNAME);
		addColumn(ColumnClass.LONG, COLNAME_USER_ID);
		addColumn(ColumnClass.INT, COLNAME_NUM_CHANGES);
		addColumn(ColumnClass.INT, COLNAME_COMMENTS_COUNT);
	}

}
