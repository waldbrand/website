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

	public void createDatabase() throws SQLException, QueryException
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
