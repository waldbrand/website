package de.waldbrand.app.website.pages.osm.maps;

import static de.waldbrand.app.website.osm.PoiType.HYDRANT_OTHER;
import static de.waldbrand.app.website.osm.PoiType.HYDRANT_PILLAR;
import static de.waldbrand.app.website.osm.PoiType.HYDRANT_PIPE;
import static de.waldbrand.app.website.osm.PoiType.HYDRANT_UNDERGROUND;
import static de.waldbrand.app.website.osm.PoiType.SUCTION_POINT;
import static de.waldbrand.app.website.osm.PoiType.WATER_POND;
import static de.waldbrand.app.website.osm.PoiType.WATER_TANK;

import java.util.HashMap;
import java.util.Map;

import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.util.MarkerShape;

public class OsmMarkers
{

	private static Map<PoiType, String> typeToColor = new HashMap<>();
	static {
		typeToColor.put(HYDRANT_PILLAR, "red");
		typeToColor.put(HYDRANT_PIPE, "purple");
		typeToColor.put(HYDRANT_UNDERGROUND, "green");
		typeToColor.put(HYDRANT_OTHER, "white");
		typeToColor.put(SUCTION_POINT, "blue");
		typeToColor.put(WATER_TANK, "red");
		typeToColor.put(WATER_POND, "blue");
	}

	private static Map<PoiType, MarkerShape> typeToShape = new HashMap<>();
	static {
		typeToShape.put(HYDRANT_PILLAR, MarkerShape.CIRCLE);
		typeToShape.put(HYDRANT_PIPE, MarkerShape.CIRCLE);
		typeToShape.put(HYDRANT_UNDERGROUND, MarkerShape.PENTA);
		typeToShape.put(HYDRANT_OTHER, MarkerShape.STAR);
		typeToShape.put(SUCTION_POINT, MarkerShape.CIRCLE);
		typeToShape.put(WATER_TANK, MarkerShape.SQUARE);
		typeToShape.put(WATER_POND, MarkerShape.SQUARE);
	}

	public static String getColor(PoiType type)
	{
		return get(typeToColor.get(type), "red");
	}

	public static MarkerShape getShape(PoiType type)
	{
		return get(typeToShape.get(type), MarkerShape.CIRCLE);
	}

	private static <T> T get(T value, T defaultValue)
	{
		return value != null ? value : defaultValue;
	}

}
