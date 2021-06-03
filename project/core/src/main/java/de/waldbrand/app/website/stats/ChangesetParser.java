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

package de.waldbrand.app.website.stats;

import org.locationtech.jts.geom.Geometry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import de.waldbrand.app.website.stats.model.osmcha.Changesets;
import de.waldbrand.app.website.stats.model.osmcha.GeometryDeserializer;

public class ChangesetParser
{

	private Gson gson = new GsonBuilder()
			.registerTypeAdapter(Geometry.class, new GeometryDeserializer())
			.create();

	public Changesets parse(JsonElement json)
	{
		return gson.fromJson(json, Changesets.class);
	}

}
