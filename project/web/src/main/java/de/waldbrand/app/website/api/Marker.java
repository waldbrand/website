// Copyright 2021 Sebastian Kuerten
//
// This file is part of waldbrand-website.
//
// waldbrand-website is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// waldbrand-website is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with waldbrand-website. If not, see <http://www.gnu.org/licenses/>.

package de.waldbrand.app.website.api;

import org.locationtech.jts.geom.Coordinate;

import de.waldbrand.app.website.lbforst.model.WesPoi;
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

	public Marker(WesPoi poi)
	{
		Coordinate c = poi.getCoordinate();
		lon = c.getX();
		lat = c.getY();
		popup = WesMapUtil.content(poi, true);
	}

}
