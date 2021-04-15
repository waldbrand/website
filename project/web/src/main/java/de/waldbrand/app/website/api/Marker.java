package de.waldbrand.app.website.api;

import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.osm.model.OsmPoi;
import de.waldbrand.app.website.pages.osm.maps.OsmMapUtil;

public class Marker
{

	private double lon;
	private double lat;
	private String popup;

	public Marker(OsmPoi poi, PoiType type)
	{
		lon = poi.getLon();
		lat = poi.getLat();
		popup = OsmMapUtil.content(poi, type);
	}

}
