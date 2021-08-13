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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.waldbrand.app.website.lbforst.model.WesPoi;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.osm.model.OsmPoi;
import lombok.Getter;

public class PoiMarkers
{

	@Getter
	private Map<String, MarkerList> markers = new LinkedHashMap<>();

	public void add(PoiType type, String iconId, Iterable<OsmPoi> pois)
	{
		List<Marker> list = new ArrayList<>();
		markers.put(type.toString(), new MarkerList(iconId, list));
		for (OsmPoi poi : pois) {
			list.add(new Marker(poi, type));
		}
	}

	public void add(String type, String iconId, Iterable<WesPoi> pois)
	{
		List<Marker> list = new ArrayList<>();
		markers.put(type.toString(), new MarkerList(iconId, list));
		for (WesPoi poi : pois) {
			list.add(new Marker(poi));
		}
	}

}