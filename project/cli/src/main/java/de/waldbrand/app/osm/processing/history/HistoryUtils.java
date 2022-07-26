// Copyright 2022 Sebastian Kuerten
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

package de.waldbrand.app.osm.processing.history;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import com.slimjars.dist.gnu.trove.map.TLongObjectMap;
import com.slimjars.dist.gnu.trove.map.hash.TLongObjectHashMap;

import de.topobyte.osm4j.core.access.OsmIteratorInput;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.iface.OsmMetadata;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.utils.OsmFileInput;

public class HistoryUtils
{

	public static OsmEntity findLatestVersionUntil(List<OsmEntity> versions,
			LocalDate d)
	{
		for (int i = versions.size() - 1; i >= 0; i--) {
			OsmEntity entity = versions.get(i);
			OsmMetadata metadata = entity.getMetadata();
			LocalDate date = Instant.ofEpochMilli(metadata.getTimestamp())
					.atOffset(ZoneOffset.UTC).toLocalDate();
			if (date.isBefore(d)) {
				return entity;
			}
		}
		return null;
	}

	public static TLongObjectMap<List<OsmNode>> buildNodeLookup(
			OsmFileInput fileInput) throws IOException
	{
		TLongObjectMap<List<OsmNode>> nodeLookup = new TLongObjectHashMap<>();

		OsmIteratorInput iterator = fileInput.createIterator(true, true);
		HistoryIterator historyIterator = new HistoryIterator(
				iterator.getIterator());
		for (List<OsmEntity> versions : historyIterator) {
			OsmEntity first = versions.get(0);
			if (first.getType() != EntityType.Node) {
				break;
			}
			OsmNode firstNode = (OsmNode) first;
			long id = firstNode.getId();
			List<OsmNode> nodes = new ArrayList<>();
			for (OsmEntity entity : versions) {
				nodes.add((OsmNode) entity);
			}
			nodeLookup.put(id, nodes);
		}

		return nodeLookup;
	}

}
