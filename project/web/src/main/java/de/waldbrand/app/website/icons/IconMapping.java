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
		osmToIcon.put(PoiType.HYDRANT_OTHER, Icon.HYDRANT);
		osmToIcon.put(PoiType.SUCTION_POINT, Icon.SAUGSTELLE);
		osmToIcon.put(PoiType.WATER_TANK, Icon.SPEICHER);
		osmToIcon.put(PoiType.WATER_POND, Icon.SPEICHER);
		osmToIcon.put(PoiType.HYDRANT_PIPE, Icon.TIEFBRUNNEN_E);
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
		forstToIcon.put(WesType.GRUNDWASSERTIEFBRUNNEN, Icon.TIEFBRUNNEN_E);
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
