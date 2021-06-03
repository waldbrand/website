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

package de.waldbrand.app.website.links;

import java.util.HashMap;
import java.util.Map;

import de.topobyte.webgun.resolving.smart.mappers.ParameterMapper;
import de.waldbrand.app.website.osm.PoiType;

public class PoiTypeMapper implements ParameterMapper<PoiType>
{

	private Map<String, PoiType> typeLookup = new HashMap<>();
	{
		for (PoiType type : PoiType.values()) {
			typeLookup.put(type.getUrlKeyword(), type);
		}
	}

	@Override
	public String toString(PoiType object)
	{
		return object.getUrlKeyword();
	}

	@Override
	public PoiType fromString(String s)
	{
		PoiType type = typeLookup.get(s);
		if (type == null) {
			throw new IllegalArgumentException();
		}
		return type;
	}

}
