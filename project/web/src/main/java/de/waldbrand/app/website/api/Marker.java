package de.waldbrand.app.website.api;

import de.waldbrand.app.website.osm.model.OsmPoi;

public class Marker
{

	private double lon;
	private double lat;
	private String popup;

	public Marker(OsmPoi poi)
	{
		lon = poi.getLon();
		lat = poi.getLat();
		popup = "foo";
	}

}
