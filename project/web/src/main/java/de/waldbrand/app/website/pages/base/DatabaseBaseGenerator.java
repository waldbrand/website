package de.waldbrand.app.website.pages.base;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jsoup.Exceptions;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.P;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.luqe.jdbc.database.Database;
import de.topobyte.webgun.exceptions.WebStatusException;
import de.topobyte.weblogin.LoginDao;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.db.DatabasePool;

public abstract class DatabaseBaseGenerator extends BaseGenerator
{

	final static Logger logger = LoggerFactory
			.getLogger(DatabaseBaseGenerator.class);

	public DatabaseBaseGenerator(WebPath path)
	{
		super(path);
	}

	protected Database db;
	protected LoginDao loginDao;

	@Override
	public void generate() throws IOException
	{
		super.generate();

		menu();

		try {
			db = DatabasePool.getDatabase();
			loginDao = new LoginDao(db.getConnection());
			content();
		} catch (WebStatusException e) {
			throw e;
		} catch (Throwable e) {
			P p = content.ac(HTML.p());
			p.at("Error while generating content:");
			p.ac(HTML.br());
			Exceptions.appendStackTrace(p, e);
		} finally {
			if (db != null) {
				db.closeConnection(false);
			}
		}

		footer();
	}

	protected abstract void content()
			throws IOException, QueryException, SQLException, ServletException;

}
