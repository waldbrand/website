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

package de.waldbrand.app.website.lbforst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.waldbrand.app.website.Config;
import de.waldbrand.app.website.lbforst.model.WesPoi;

public class NameUtil
{

	public static String getName(WesPoi poi)
	{
		String name = String.format("%s %d", typeName(poi.getOart()),
				poi.getId());
		if (name == null) {
			name = Config.INSTANCE.getNullName();
		}
		return name;
	}

	private static Map<Integer, String> oartToName = new HashMap<>();

	static {
		for (WesType type : WesType.values()) {
			oartToName.put(type.getId(), type.getDescription());
		}
	}

	public static String typeName(int oart)
	{
		String name = oartToName.get(oart);
		return name != null ? name : Integer.toString(oart);
	}

	public static List<Integer> getOarts()
	{
		List<Integer> values = new ArrayList<>(oartToName.keySet());
		Collections.sort(values);
		return values;
	}

}
