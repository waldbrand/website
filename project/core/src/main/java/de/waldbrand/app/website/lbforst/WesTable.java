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

public class WesTable extends Table
{

	public static String COLNAME_ID = "fid";
	public static String COLNAME_FSTATUS = "fstatus";
	public static String COLNAME_AKZ = "akz";
	public static String COLNAME_BAUJAHR = "baujahr";
	public static String COLNAME_FKT_FAEHIG = "fkt_faehig";
	public static String COLNAME_BEMERKUNG = "bemerkung";
	public static String COLNAME_OART = "oart";
	public static String COLNAME_MENGE = "menge";
	public static String COLNAME_RECHTS_W = "rechts_w";
	public static String COLNAME_HOCH_W = "hoch_w";
	public static String COLNAME_LAND = "land";
	public static String COLNAME_GDB_GEOMATTR_DATA = "gdb_geomattr_data";
	public static String COLNAME_SHAP = "shap";

	public WesTable()
	{
		super("wes");

		addColumn(ColumnClass.LONG, COLNAME_ID);
		addColumn(ColumnClass.LONG, COLNAME_FSTATUS);
		addColumn(ColumnClass.LONG, COLNAME_AKZ);
		addColumn(ColumnClass.LONG, COLNAME_BAUJAHR);
		addColumn(ColumnClass.LONG, COLNAME_FKT_FAEHIG);
		addColumn(ColumnClass.VARCHAR, COLNAME_BEMERKUNG);
		addColumn(ColumnClass.LONG, COLNAME_OART);
		addColumn(ColumnClass.LONG, COLNAME_MENGE);
		addColumn(ColumnClass.LONG, COLNAME_RECHTS_W);
		addColumn(ColumnClass.LONG, COLNAME_HOCH_W);
		addColumn(ColumnClass.LONG, COLNAME_LAND);
		addColumn(ColumnClass.LONG, COLNAME_GDB_GEOMATTR_DATA);
		addColumn(ColumnClass.LONG, COLNAME_SHAP);
	}

}
