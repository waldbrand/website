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

package de.waldbrand.app.website.lbforst;

import de.topobyte.jsqltables.table.ColumnClass;
import de.topobyte.jsqltables.table.Table;

public class RettungspunkteTable extends Table
{

	public static String COLNAME_ID = "fid";
	public static String COLNAME_RECHTS_W = "rechts_w";
	public static String COLNAME_HOCH_W = "hoch_w";

	public RettungspunkteTable()
	{
		super("rettungspunkte");

		addColumn(ColumnClass.LONG, COLNAME_ID);
		addColumn(ColumnClass.LONG, COLNAME_RECHTS_W);
		addColumn(ColumnClass.LONG, COLNAME_HOCH_W);
	}

}
