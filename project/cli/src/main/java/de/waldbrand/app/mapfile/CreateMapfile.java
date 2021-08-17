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

package de.waldbrand.app.mapfile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import de.topobyte.luqe.iface.QueryException;
import de.topobyte.melon.commons.io.Resources;
import de.topobyte.osm4j.diskstorage.DbExtensions;
import de.topobyte.osm4j.diskstorage.EntityDbSetup;
import de.topobyte.osm4j.utils.FileFormat;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.simplemapfile.core.EntityFile;
import de.topobyte.simplemapfile.xml.SmxFileReader;
import de.topobyte.system.utils.SystemPaths;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;

public class CreateMapfile
{

	final static Logger logger = LoggerFactory.getLogger(CreateMapfile.class);

	final static String HELP_MESSAGE = CreateMapfile.class.getSimpleName()
			+ " [options]";

	private static final String OPTION_NODE_DB = "node-db";
	private static final String OPTION_WAY_DB = "way-db";
	private static final String OPTION_INPUT = "input";
	private static final String OPTION_NODES_FILE = "nodes";
	private static final String OPTION_WAYS_FILE = "ways";
	private static final String OPTION_RELATIONS_FILE = "relations";
	private static final String OPTION_RULES = "rules";
	private static final String OPTION_OUTPUT = "output";
	private final static String OPTION_LOGS = "logs";
	private static final String OPTION_LAND_GEOMETRY = "land-geometry";
	private static final String OPTION_LIMITS_NODES = "limits-nodes";
	private static final String OPTION_LIMITS_WAYS = "limits-ways";
	private static final String OPTION_LIMITS_RELATIONS = "limits-relations";

	/**
	 * @param args
	 *            input, output_index, output_data
	 */
	public static void main(String[] args)
	{
		String precedenceExplanation = " (takes precedence over the file specified via -"
				+ OPTION_INPUT + ")";
		// @formatter:off
		Options options = new Options();
		OptionHelper.addL(options, OPTION_INPUT, true, false, "file", "an osm input file");
		OptionHelper.addL(options, OPTION_NODES_FILE, true, false, "file", "an osm file containing the nodes" + precedenceExplanation);
		OptionHelper.addL(options, OPTION_WAYS_FILE, true, false, "file", "an osm file containing the ways" + precedenceExplanation);
		OptionHelper.addL(options, OPTION_RELATIONS_FILE, true, false, "file", "an osm file containing the relations" + precedenceExplanation);
		OptionHelper.addL(options, OPTION_NODE_DB, true, false, "a node database");
		OptionHelper.addL(options, OPTION_WAY_DB, true, false, "a way database");
		OptionHelper.addL(options, OPTION_OUTPUT, true, true, "file", "a file to put results to");
		OptionHelper.addL(options, OPTION_RULES, true, true, "directory", "where to look for rule definitions");
		OptionHelper.addL(options, OPTION_LOGS, true, true, "directory", "where to dump debug information");
		OptionHelper.addL(options, OPTION_LAND_GEOMETRY, true, false, "file", "a geometry representing the land to generate sea");
		OptionHelper.addL(options, OPTION_LIMITS_NODES, true, false, "a comma separated list of integers");
		OptionHelper.addL(options, OPTION_LIMITS_WAYS, true, false, "a comma separated list of integers");
		OptionHelper.addL(options, OPTION_LIMITS_RELATIONS, true, false, "a comma separated list of integers");
		// @formatter:on

		CommandLine line = null;
		try {
			line = new DefaultParser().parse(options, args);
		} catch (ParseException e) {
			System.out
					.println("unable to parse command line: " + e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.setOptionComparator(null);
			formatter.printHelp(HELP_MESSAGE, options);
			System.exit(1);
		}

		if (line == null) {
			return;
		}

		// @formatter:off
		String argNodeDb = line.getOptionValue(OPTION_NODE_DB);
		String argWayDb = line.getOptionValue(OPTION_WAY_DB);
		String argInput = line.getOptionValue(OPTION_INPUT);
		String argNodes = line.getOptionValue(OPTION_NODES_FILE);
		String argWays = line.getOptionValue(OPTION_WAYS_FILE);
		String argRelations = line.getOptionValue(OPTION_RELATIONS_FILE);
		String argOutput = line.getOptionValue(OPTION_OUTPUT);
		String argRules = line.getOptionValue(OPTION_RULES);
		String argLog = line.getOptionValue(OPTION_LOGS);
		String landGeometryFile = line.getOptionValue(OPTION_LAND_GEOMETRY);
		String limitsNodesString = line.getOptionValue(OPTION_LIMITS_NODES);
		String limitsWaysString = line.getOptionValue(OPTION_LIMITS_WAYS);
		String limitsRelationsString = line.getOptionValue(OPTION_LIMITS_RELATIONS);
		// @formatter:on

		if (argInput == null) {
			if (argNodes == null) {
				System.out.println("Please specify an -" + OPTION_INPUT
						+ " paramter or a -" + OPTION_NODES_FILE
						+ " parameter");
				System.exit(1);
			}
			if (argWays == null) {
				System.out.println("Please specify an -" + OPTION_INPUT
						+ " paramter or a -" + OPTION_WAYS_FILE + " parameter");
				System.exit(1);
			}
			if (argRelations == null) {
				System.out.println("Please specify an -" + OPTION_INPUT
						+ " paramter or a -" + OPTION_RELATIONS_FILE
						+ " parameter");
				System.exit(1);
			}
		}

		Path pathNodes = argNodes != null ? Paths.get(argNodes)
				: Paths.get(argInput);
		Path pathWays = argWays != null ? Paths.get(argWays)
				: Paths.get(argInput);
		Path pathRelations = argRelations != null ? Paths.get(argRelations)
				: Paths.get(argInput);

		FileFormat inputFormat = FileFormat.TBO;
		OsmFileInput inputNodes = new OsmFileInput(pathNodes, inputFormat);
		OsmFileInput inputWays = new OsmFileInput(pathWays, inputFormat);
		OsmFileInput inputRelations = new OsmFileInput(pathRelations,
				inputFormat);

		Path pathRules = Paths.get(argRules);
		Path pathOutput = Paths.get(argOutput);

		Path nodeIndex = Paths.get(argNodeDb + DbExtensions.EXTENSION_INDEX);
		Path nodeData = Paths.get(argNodeDb + DbExtensions.EXTENSION_DATA);
		Path wayIndex = Paths.get(argWayDb + DbExtensions.EXTENSION_INDEX);
		Path wayData = Paths.get(argWayDb + DbExtensions.EXTENSION_DATA);

		logger.info("nodedb index: " + nodeIndex);
		logger.info("nodedb data: " + nodeData);
		logger.info("waydb index: " + wayIndex);
		logger.info("waydb data: " + wayData);

		try {
			EntityDbSetup.createNodeDb(pathNodes, nodeIndex, nodeData);
		} catch (IOException e) {
			logger.error("errror while creating node database", e);
			System.exit(1);
		}

		try {
			EntityDbSetup.createWayDb(pathWays, wayIndex, wayData, true);
		} catch (IOException e) {
			logger.error("errror while creating way database", e);
			System.exit(1);
		}

		Path pathLogs = argLog == null ? null : Paths.get(argLog);
		Path landFile = landGeometryFile == null ? null
				: Paths.get(landGeometryFile);

		Geometry boundary = null;
		try (InputStream is = Resources.stream("Brandenburg.smx")) {
			EntityFile entity = SmxFileReader.read(is);
			boundary = entity.getGeometry();
		} catch (IOException | ParserConfigurationException | SAXException e) {
			logger.error("unable to read geometry", e);
		}

		Path repoRettungspunkte = SystemPaths.HOME
				.resolve("github/waldbrand/rettungspunkte");
		Path fileRettungspunkte = repoRettungspunkte
				.resolve("daten/rettungspunkte.gpkg");

		MapfileCreator creator = MapfileCreator.setup(boundary, nodeIndex,
				nodeData, wayIndex, wayData, inputNodes, inputWays,
				inputRelations, fileRettungspunkte, pathOutput, pathRules,
				pathLogs, landFile, limitsNodesString, limitsWaysString,
				limitsRelationsString);

		if (creator == null) {
			logger.error("error while setting up creator instance");
			System.exit(1);
		}

		try {
			creator.execute();
		} catch (IOException | QueryException e) {
			logger.error("error while creating file", e);
			System.exit(1);
		}
	}

}
