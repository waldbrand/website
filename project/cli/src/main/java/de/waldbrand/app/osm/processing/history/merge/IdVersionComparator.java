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

package de.waldbrand.app.osm.processing.history.merge;

import java.util.Comparator;

import de.topobyte.osm4j.core.model.iface.OsmEntity;

public class IdVersionComparator implements Comparator<OsmEntity>
{

	@Override
	public int compare(OsmEntity o1, OsmEntity o2)
	{
		int cmpId = Long.compare(o1.getId(), o2.getId());
		if (cmpId != 0) {
			return cmpId;
		}
		return Long.compare(o1.getMetadata().getVersion(),
				o2.getMetadata().getVersion());
	}

}
