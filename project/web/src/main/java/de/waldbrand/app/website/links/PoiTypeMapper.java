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
