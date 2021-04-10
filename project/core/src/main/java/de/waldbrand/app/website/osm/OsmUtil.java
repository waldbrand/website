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
