package de.waldbrand.app.website.osm;

import java.util.ArrayList;
import java.util.List;

public class OsmTypes
{

	public static List<String> names(Iterable<PoiType> types)
	{
		List<String> names = new ArrayList<>();
		for (PoiType type : types) {
			names.add(type.getMultiple());
		}
		return names;
	}

}
