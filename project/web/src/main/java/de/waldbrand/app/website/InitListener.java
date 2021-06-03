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

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.geotools.referencing.factory.DeferredAuthorityFactory;
import org.geotools.util.WeakCollectionCleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import de.topobyte.cachebusting.CacheBusting;
import de.topobyte.ioutils.ShellPaths;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.melon.commons.io.Resources;
import de.topobyte.shiro.AuthInfo;
import de.topobyte.weblogin.realm.DbRealm;
import de.topobyte.weblogin.realm.Root;
import de.topobyte.weblogin.realm.UserInfo;
import de.waldbrand.app.website.config.MapPosition;
import de.waldbrand.app.website.db.ConnectionUtil;
import de.waldbrand.app.website.db.DatabasePoolDatabaseFactory;
import de.waldbrand.app.website.db.DbCreator;

@WebListener
public class InitListener implements ServletContextListener
{

	final static Logger logger = LoggerFactory.getLogger(InitListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		logger.info("context initialized");
		long start = System.currentTimeMillis();

		logger.info("initializing JDBC SQLite driver");
		ConnectionUtil.initJdbc();

		logger.info("setting up website factories");
		Website.INSTANCE.setCacheBuster(filename -> {
			return CacheBusting.resolve(filename);
		});

		logger.info("loading configuration...");
		Properties config = new Properties();
		try (InputStream input = Resources.stream("config.properties")) {
			config.load(input);
		} catch (Throwable e) {
			logger.error("Unable to load configuration", e);
		}

		logger.info("loading secure configuration...");
		Properties secureConfig = new Properties();
		try (InputStream input = Resources.stream("secure.properties")) {
			secureConfig.load(input);
		} catch (Throwable e) {
			logger.error("Unable to load secure configuration", e);
		}

		String defaultMapPosition = config.getProperty("default.map.position");
		String[] parts = defaultMapPosition.split(",");
		double lat = Double.parseDouble(parts[0]);
		double lon = Double.parseDouble(parts[1]);
		int zoom = Integer.parseInt(parts[2]);
		Config.INSTANCE.setDefaultMapPosition(new MapPosition(lat, lon, zoom));

		String mapTilesPattern = config.getProperty("map.tiles.pattern");
		Config.INSTANCE.setMapTilesPattern(mapTilesPattern);

		Path fileWesData = Paths.get(config.getProperty("wes.data"));
		Config.INSTANCE.setFileWesData(fileWesData);

		Path fileOsmData = Paths.get(config.getProperty("osm.data"));
		Config.INSTANCE.setFileOsmData(fileOsmData);
		Path fileOsmWaynodes = Paths.get(config.getProperty("osm.waynodes"));
		Config.INSTANCE.setFileOsmWaynodes(fileOsmWaynodes);

		Path databasePath = ShellPaths
				.resolve(config.getProperty("database.path"));
		Config.INSTANCE.setDatabase(databasePath);

		WebsiteData.load();

		logger.info("creating database if necessary");
		try {
			DbCreator dbCreator = new DbCreator();
			dbCreator.createDatabase();
		} catch (SQLException | QueryException e) {
			logger.warn("Error while creating database", e);
		}

		logger.info("setting up db realm");

		String rootLoginHash = secureConfig.getProperty("root.login.hash");
		String rootLoginSalt = secureConfig.getProperty("root.login.salt");

		if (rootLoginHash == null || rootLoginSalt == null) {
			throw new RuntimeException(
					"Make sure properties 'root.login.hash' and 'root.login.salt'"
							+ " are set in secure.properties");
		}

		DbRealm.DATABASE_FACTORY = new DatabasePoolDatabaseFactory();
		DbRealm.ROOT_USER_INFO = new UserInfo(new Root(),
				new AuthInfo(rootLoginHash, rootLoginSalt));

		long stop = System.currentTimeMillis();

		logger.info("done");
		logger.info(String.format("Initialized in %.2f seconds",
				(stop - start) / 1000d));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		logger.info("context destroyed");

		logger.info("shutting down GeoTools weak collection cleaner");
		WeakCollectionCleaner.DEFAULT.exit();

		logger.info("shutting down GeoTools authority factory");
		DeferredAuthorityFactory.exit();

		logger.info("deregistering JDBC drivers");
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			if (driver.getClass().getClassLoader() == cl) {
				try {
					logger.info("deregistering JDBC driver {}", driver);
					DriverManager.deregisterDriver(driver);
				} catch (SQLException ex) {
					logger.error("Error deregistering JDBC driver {}", driver,
							ex);
				}
			} else {
				logger.info(
						"Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader",
						driver);
			}
		}

		logger.info("shutting down Logback");
		LoggerContext loggerContext = (LoggerContext) LoggerFactory
				.getILoggerFactory();
		loggerContext.stop();
	}

}
