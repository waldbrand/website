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

package de.waldbrand.app.website.osm;

import static de.waldbrand.app.website.osm.PoiType.HYDRANT_PILLAR;
import static de.waldbrand.app.website.osm.PoiType.HYDRANT_PIPE;
import static de.waldbrand.app.website.osm.PoiType.SUCTION_POINT;

import java.util.EnumSet;
import java.util.Map;

public class OsmUtil
{

	public static EnumSet<PoiType> classify(Map<String, String> tags)
	{
		EnumSet<PoiType> types = EnumSet.noneOf(PoiType.class);

		String emergency = tags.get("emergency");
		String fireHydrantType = tags.get("fire_hydrant:type");

		if ("suction_point".equals(emergency)) {
			types.add(SUCTION_POINT);
		}
		if (fireHydrantType != null) {
			switch (fireHydrantType) {
			case "pillar":
				types.add(HYDRANT_PILLAR);
				break;
			case "pipe":
				types.add(HYDRANT_PIPE);
				break;
			}
		}

		return types;
	}

}
