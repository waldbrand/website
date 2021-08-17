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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import de.topobyte.luqe.iface.QueryException;
import de.topobyte.mapocado.styles.rules.RuleFileReader;
import de.topobyte.mapocado.styles.rules.RuleSet;
import de.topobyte.osm4j.diskstorage.nodedb.NodeDB;
import de.topobyte.osm4j.diskstorage.vardb.VarDB;
import de.topobyte.osm4j.diskstorage.waydb.WayRecordWithTags;
import de.topobyte.osm4j.processing.entities.ExecutableEntityProcessor;
import de.topobyte.osm4j.processing.entities.filter.DefaultEntityFilter;
import de.topobyte.osm4j.utils.OsmFileInput;
import de.topobyte.simplemapfile.core.EntityFile;
import de.topobyte.simplemapfile.xml.SmxFileReader;

public class MapfileCreator
{

	private static Integer[] DEFAULT_LIMITS_NODES = new Integer[] { 12, 14,
			17 };
	private static Integer[] DEFAULT_LIMITS_WAYS = new Integer[] { 12, 14 };
	private static Integer[] DEFAULT_LIMITS_RELATIONS = new Integer[] { 12,
			14 };

	static final Logger logger = LoggerFactory.getLogger(MapfileCreator.class);

	public static MapfileCreator setup(Geometry boundary, Path nodesIndex,
			Path nodesData, Path waysIndex, Path waysData,
			OsmFileInput nodesFile, OsmFileInput waysFile,
			OsmFileInput relationsFile, Path fileRettungspunkte,
			Path outputFile, Path configDir, Path logsDir,
			Path landGeometryFile, String limitsNodesString,
			String limitsWaysString, String limitsRelationsString)
	{
		Geometry landGeometry = null;
		if (landGeometryFile != null) {
			try {
				EntityFile entity = SmxFileReader.read(landGeometryFile);
				landGeometry = entity.getGeometry();
			} catch (IOException e) {
				logger.warn("unable to read land geometry, IOException: "
						+ e.getMessage());
			} catch (ParserConfigurationException e) {
				logger.error(
						"unable to read land geometry, ParserConfigurationException: "
								+ e.getMessage());
			} catch (SAXException e) {
				logger.error("unable to read land geometry, SAXException: "
						+ e.getMessage());
			}
		}

		RuleSet config = new RuleSet();
		try {
			File[] ruleFiles = configDir.toFile().listFiles();
			for (File ruleFile : ruleFiles) {
				if (!ruleFile.getName().endsWith(".xml")) {
					continue;
				}
				RuleFileReader.read(config, ruleFile.getPath());
			}
		} catch (Exception e) {
			logger.error("Exception while parsing rules: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		NodeDB nodeDB = null;
		VarDB<WayRecordWithTags> wayDB = null;
		try {
			nodeDB = new NodeDB(nodesData, nodesIndex);
			wayDB = new VarDB<>(waysData, waysIndex, new WayRecordWithTags(0));
		} catch (FileNotFoundException e) {
			// do nothing
		}

		if (nodeDB == null || wayDB == null) {
			return null;
		}

		List<Integer> limitsNodes = Arrays.asList(DEFAULT_LIMITS_NODES);
		List<Integer> limitsWays = Arrays.asList(DEFAULT_LIMITS_WAYS);
		List<Integer> limitsRelations = Arrays.asList(DEFAULT_LIMITS_RELATIONS);

		if (limitsNodesString != null) {
			limitsNodes = parseLimits(limitsNodesString);
		}
		if (limitsWaysString != null) {
			limitsWays = parseLimits(limitsWaysString);
		}
		if (limitsRelationsString != null) {
			limitsRelations = parseLimits(limitsRelationsString);
		}

		MapfileCreator creator = new MapfileCreator(outputFile, config,
				nodesFile, waysFile, relationsFile, nodeDB, wayDB, boundary,
				logsDir, landGeometry, limitsNodes, limitsWays, limitsRelations,
				fileRettungspunkte);
		return creator;
	}

	private static List<Integer> parseLimits(String limitsString)
	{
		if (limitsString.equals("")) {
			return new ArrayList<>();
		}
		ArrayList<Integer> list = new ArrayList<>();
		String[] parts = limitsString.split(",");
		for (String part : parts) {
			int num = Integer.parseInt(part);
			list.add(num);
		}
		return list;
	}

	private MapformatCreator create;
	private ExecutableEntityProcessor processor;
	private OsmFileInput nodesFile, waysFile, relationsFile;
	private Path fileRettungspunkte;

	public MapfileCreator(Path outputFile, RuleSet config,
			OsmFileInput nodesFile, OsmFileInput waysFile,
			OsmFileInput relationsFile, NodeDB nodeDB,
			VarDB<WayRecordWithTags> wayDB, Geometry boundary, Path logsDir,
			Geometry landGeometry, List<Integer> limitsNodes,
			List<Integer> limitsWays, List<Integer> limitsRelations,
			Path fileRettungspunkte)
	{
		this.nodesFile = nodesFile;
		this.waysFile = waysFile;
		this.relationsFile = relationsFile;
		this.fileRettungspunkte = fileRettungspunkte;
		File fileLogsDir = logsDir == null ? null : logsDir.toFile();
		create = new MapformatCreator(outputFile.toFile(), config, nodesFile,
				waysFile, relationsFile, nodeDB, boundary, fileLogsDir,
				landGeometry, limitsNodes, limitsWays, limitsRelations);
		processor = new ExecutableEntityProcessor(create, nodeDB, wayDB,
				boundary, logsDir, new DefaultEntityFilter());
	}

	public void execute() throws IOException, QueryException
	{
		processor.prepare();
		create.prepare();

		processor.execute(nodesFile, waysFile, relationsFile);
		create.processRettungspunkte(fileRettungspunkte);

		create.createFile();
	}

}
