package de.waldbrand.app.website.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.waldbrand.app.website.lbforst.model.Poi;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.osm.model.OsmPoi;
import lombok.Getter;

public class PoiMarkers
{

	@Getter
	private Map<String, List<Marker>> markers = new LinkedHashMap<>();

	public void add(PoiType type, List<OsmPoi> pois)
	{
		List<Marker> list = new ArrayList<>();
		markers.put(type.toString(), list);
		for (OsmPoi poi : pois) {
			list.add(new Marker(poi, type));
		}
	}

	public void add(String type, List<Poi> pois)
	{
		List<Marker> list = new ArrayList<>();
		markers.put(type.toString(), list);
		for (Poi poi : pois) {
			list.add(new Marker(poi));
		}
	}

}