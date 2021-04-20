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

import java.util.EnumSet;
import java.util.Map;

import de.topobyte.osm4j.core.model.iface.OsmTag;

public class OsmUtil
{

	public static EnumSet<PoiType> classify(Map<String, String> tags)
	{
		EnumSet<PoiType> types = EnumSet.noneOf(PoiType.class);

		for (PoiType type : PoiType.values()) {
			if (fits(type, tags)) {
				types.add(type);
			}
		}

		return types;
	}

	private static boolean fits(PoiType type, Map<String, String> tags)
	{
		if (type.getMissingKeys() != null) {
			for (String key : type.getMissingKeys()) {
				if (tags.containsKey(key)) {
					return false;
				}
			}
		}
		for (OsmTag tag : type.getTags()) {
			String value = tags.get(tag.getKey());
			if (!tag.getValue().equals(value)) {
				return false;
			}
		}
		return true;
	}

}
