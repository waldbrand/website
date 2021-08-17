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

package de.waldbrand.app.osm.processing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.topobyte.osm4j.core.resolve.OsmEntityProvider;
import de.topobyte.osm4j.diskstorage.EntityDbSetup;
import de.topobyte.osm4j.diskstorage.EntityProviderImpl;
import de.topobyte.osm4j.diskstorage.nodedb.NodeDB;
import de.topobyte.osm4j.diskstorage.vardb.VarDB;
import de.topobyte.osm4j.diskstorage.waydb.WayRecord;

public class EntityDbs
{
	private Path nodeDbIndex;
	private Path nodeDbData;
	private Path wayDbIndex;
	private Path wayDbData;

	public EntityDbs(Path dir)
	{
		nodeDbIndex = dir.resolve("nodedb.idx");
		nodeDbData = dir.resolve("nodedb.dat");
		wayDbIndex = dir.resolve("waydb.idx");
		wayDbData = dir.resolve("waydb.dat");
	}

	public void init(Path input) throws IOException
	{
		// create entity databases
		if (!Files.exists(nodeDbData)) {
			EntityDbSetup.createNodeDb(input, nodeDbIndex, nodeDbData);
			EntityDbSetup.createWayDb(input, wayDbIndex, wayDbData, false);
		}
	}

	public NodeDB nodeDb() throws FileNotFoundException
	{
		return new NodeDB(nodeDbData, nodeDbIndex);
	}

	public VarDB<WayRecord> wayDb() throws FileNotFoundException
	{
		return new VarDB<>(wayDbData, wayDbIndex, new WayRecord(0));
	}

	public OsmEntityProvider entityProvider() throws FileNotFoundException
	{
		return new EntityProviderImpl(nodeDb(), wayDb());
	}

}
