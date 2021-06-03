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

package de.waldbrand.app.website.stats.model.osmcha;

import java.lang.reflect.Type;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class GeometryDeserializer implements JsonDeserializer<Geometry>
{

	private GeometryFactory gf = new GeometryFactory();

	@Override
	public Geometry deserialize(JsonElement json, Type t,
			JsonDeserializationContext context) throws JsonParseException
	{
		JsonObject object = json.getAsJsonObject();
		String type = object.get("type").getAsString();
		if (type.equals("Polygon")) {
			JsonArray rings = object.get("coordinates").getAsJsonArray();
			LinearRing shell = ring(rings.get(0).getAsJsonArray());
			if (rings.size() > 1) {
				LinearRing[] holes = new LinearRing[rings.size() - 1];
				for (int i = 1; i < rings.size(); i++) {
					holes[i - 1] = ring(rings.get(i).getAsJsonArray());
				}
				return gf.createPolygon(shell, holes);
			} else {
				return gf.createPolygon(shell);
			}
		}
		return null;
	}

	private LinearRing ring(JsonArray ring)
	{
		Coordinate[] shell = new Coordinate[ring.size()];
		for (int i = 0; i < ring.size(); i++) {
			JsonArray c = ring.get(i).getAsJsonArray();
			double lon = c.get(0).getAsDouble();
			double lat = c.get(0).getAsDouble();
			shell[i] = new Coordinate(lon, lat);
		}
		return gf.createLinearRing(shell);
	}

}
