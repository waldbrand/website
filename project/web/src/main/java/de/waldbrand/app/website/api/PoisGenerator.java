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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.locationtech.jts.geom.Envelope;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.topobyte.adt.geo.BBox;
import de.topobyte.adt.geo.BBoxString;
import de.topobyte.webgun.util.ParameterUtil;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.ApiEndpoint;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.lbforst.model.Data;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.osm.model.OsmPoi;

public class PoisGenerator implements ApiEndpoint
{

	private HttpServletRequest request;

	public PoisGenerator(HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	public void respond(WebPath path, HttpServletResponse response,
			Map<String, String[]> parameters) throws IOException
	{
		String bbox = ParameterUtil.get(parameters, "bbox");
		BBox box = BBoxString.parse(bbox).toBbox();
		Envelope envelope = box.toEnvelope();

		PoiMarkers markers = new PoiMarkers();

		Data data = Website.INSTANCE.getData();
		for (PoiType type : PoiType.values()) {
			List<OsmPoi> pois = filter(data.getTypeToPois().get(type),
					envelope);
			markers.add(type, pois);
		}

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		response.getWriter().write(gson.toJson(markers));
	}

	private List<OsmPoi> filter(List<OsmPoi> pois, Envelope envelope)
	{
		List<OsmPoi> results = new ArrayList<>();
		for (OsmPoi poi : pois) {
			if (envelope.contains(poi.getLon(), poi.getLat())) {
				results.add(poi);
			}
		}
		return results;
	}

}
