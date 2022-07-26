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

import java.util.List;

import com.slimjars.dist.gnu.trove.map.TLongObjectMap;

import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmRelation;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.resolve.EntityNotFoundException;
import de.topobyte.osm4j.core.resolve.OsmEntityProvider;

class EntityProviderHistory implements OsmEntityProvider
{

	private TLongObjectMap<List<OsmNode>> nodeLookup;
	private long timestamp;

	public EntityProviderHistory(TLongObjectMap<List<OsmNode>> nodeLookup,
			long timestamp)
	{
		this.nodeLookup = nodeLookup;
		this.timestamp = timestamp;
	}

	@Override
	public OsmNode getNode(long id) throws EntityNotFoundException
	{
		List<OsmNode> nodes = nodeLookup.get(id);
		if (nodes == null) {
			throw new EntityNotFoundException("not found");
		}
		OsmNode best = nodes.get(0);
		for (int i = 1; i < nodes.size(); i++) {
			OsmNode node = nodes.get(i);
			if (node.getMetadata().getTimestamp() <= timestamp) {
				best = node;
			}
		}
		return best;
	}

	@Override
	public OsmWay getWay(long id) throws EntityNotFoundException
	{
		throw new EntityNotFoundException("not found");
	}

	@Override
	public OsmRelation getRelation(long id) throws EntityNotFoundException
	{
		throw new EntityNotFoundException("not found");
	}

}