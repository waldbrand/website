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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import de.topobyte.osm4j.core.access.OsmIterator;
import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.OsmEntity;

// TODO: this could go into osm4j
public class HistoryIterator
		implements Iterable<List<OsmEntity>>, Iterator<List<OsmEntity>>
{

	private OsmIterator osmIterator;

	public HistoryIterator(OsmIterator osmIterator)
	{
		this.osmIterator = osmIterator;
	}

	@Override
	public Iterator<List<OsmEntity>> iterator()
	{
		return this;
	}

	@Override
	public boolean hasNext()
	{
		return last != null || osmIterator.hasNext();
	}

	private OsmEntity last = null;
	private List<OsmEntity> versions = new ArrayList<>();

	@Override
	public List<OsmEntity> next()
	{
		versions.clear();
		if (last == null) {
			if (osmIterator.hasNext()) {
				last = osmIterator.next().getEntity();
			} else {
				throw new NoSuchElementException();
			}
		}
		versions.add(last);
		OsmEntity oldLast = last;
		last = null;
		for (EntityContainer ec : osmIterator) {
			OsmEntity entity = ec.getEntity();
			if (entity.getId() == oldLast.getId()) {
				versions.add(entity);
			} else {
				last = entity;
				break;
			}
		}
		return versions;
	}

}
