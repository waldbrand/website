package de.waldbrand.app.website.api;

import java.util.ArrayList;
import java.util.List;

import de.waldbrand.app.website.osm.model.OsmPoi;
import lombok.Getter;

public class PoiMarkers
{

	@Getter
	private List<Marker> markers = new ArrayList<>();

	public PoiMarkers(List<OsmPoi> pois)
	{
		for (OsmPoi poi : pois) {
			markers.add(new Marker(poi));
		}
	}

}