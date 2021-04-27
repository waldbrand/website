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

package de.waldbrand.app.website.icons;

import java.util.HashMap;
import java.util.Map;

import de.waldbrand.app.website.lbforst.WesType;
import de.waldbrand.app.website.osm.PoiType;

public class IconMapping
{

	private static Map<PoiType, Icon> osmToIcon = new HashMap<>();
	static {
		osmToIcon.put(PoiType.HYDRANT_UNDERGROUND, Icon.HYDRANT);
		osmToIcon.put(PoiType.HYDRANT_PILLAR, Icon.HYDRANT);
		osmToIcon.put(PoiType.HYDRANT_WALL, Icon.HYDRANT);
		osmToIcon.put(PoiType.HYDRANT_OTHER, Icon.HYDRANT_UNSPEZIFIZIERT);
		osmToIcon.put(PoiType.WATER_TANK, Icon.SPEICHER);
		osmToIcon.put(PoiType.WATER_POND, Icon.SPEICHER);
		// TODO: Diese beiden sind noch nicht ganz klar!
		osmToIcon.put(PoiType.SUCTION_POINT, Icon.SAUGSTELLE);
		osmToIcon.put(PoiType.HYDRANT_PIPE, Icon.TIEFBRUNNEN);
	}

	public static Icon get(PoiType type)
	{
		return osmToIcon.get(type);
	}

	private static Map<WesType, Icon> forstToIcon = new HashMap<>();
	static {
		forstToIcon.put(WesType.SAUGSTELLE_ENDLICH, Icon.SAUGSTELLE);
		forstToIcon.put(WesType.SAUGSTELLE_ENDLICH_TKS, Icon.SAUGSTELLE);
		forstToIcon.put(WesType.SAUGSTELLE_UNENDLICH, Icon.SAUGSTELLE);
		forstToIcon.put(WesType.SAUGSTELLE_UNENDLICH_TKS, Icon.SAUGSTELLE);
		forstToIcon.put(WesType.GRUNDWASSERTIEFBRUNNEN, Icon.TIEFBRUNNEN);
		forstToIcon.put(WesType.FLACHSPIEGELBRUNEN, Icon.FLACHBRUNNEN);
		forstToIcon.put(WesType.HYDRANT, Icon.HYDRANT);
		forstToIcon.put(WesType.STAUEINRICHTUNG, Icon.SPEICHER);
		forstToIcon.put(WesType.STAUEINRICHTUNG_TKS, Icon.SPEICHER);
		forstToIcon.put(WesType.LOESCHWASSERTEICH, Icon.SPEICHER);
		forstToIcon.put(WesType.ZISTERNE, Icon.SPEICHER);
	}

	public static Icon get(WesType type)
	{
		return forstToIcon.get(type);
	}

}
