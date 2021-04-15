package de.waldbrand.app.website.api;

import org.locationtech.jts.geom.Coordinate;

import de.waldbrand.app.website.lbforst.model.Poi;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.osm.model.OsmPoi;
import de.waldbrand.app.website.pages.osm.maps.OsmMapUtil;
import de.waldbrand.app.website.pages.wes.maps.WesMapUtil;

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

	public Marker(Poi poi)
	{
		Coordinate c = poi.getCoordinate();
		lon = c.getX();
		lat = c.getY();
		popup = WesMapUtil.content(poi, true);
	}

}
