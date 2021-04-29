package de.waldbrand.app.website.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionUtil
{

	final static Logger logger = LoggerFactory.getLogger(ConnectionUtil.class);

	public static void initJdbc()
	{
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			logger.error("Error while loading SQL driver", e);
		}
	}

}
