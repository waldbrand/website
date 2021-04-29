package de.waldbrand.app.website.db;

import java.sql.SQLException;

import de.topobyte.luqe.jdbc.database.Database;
import de.topobyte.weblogin.DatabaseFactory;

public class DatabasePoolDatabaseFactory implements DatabaseFactory
{

	@Override
	public Database getDatabase() throws SQLException
	{
		return DatabasePool.getDatabase();
	}

}
