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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import de.topobyte.gson.GsonUtil;
import de.topobyte.melon.commons.io.Resources;
import de.waldbrand.app.website.stats.model.User;
import de.waldbrand.app.website.stats.model.osmcha.Changeset;
import de.waldbrand.app.website.stats.model.osmcha.Changesets;
import de.waldbrand.app.website.stats.model.osmcha.GeometryDeserializer;

public class TestParseStats
{

	final static Logger logger = LoggerFactory.getLogger(TestParseStats.class);

	public static void main(String[] args) throws IOException
	{
		InputStream input = Resources.stream("osmcha.json");
		JsonElement json = GsonUtil.parse(input);

		System.out.println(GsonUtil.prettyPrint(json));

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Geometry.class, new GeometryDeserializer())
				.create();
		Changesets changesets = gson.fromJson(json, Changesets.class);
		System.out.println(
				String.format("found %d changesets", changesets.getCount()));

		for (Changeset cs : changesets.getFeatures()) {
			System.out.println(cs.toString());
		}

		Set<User> users = new HashSet<>();
		int created = 0;
		int modified = 0;
		int deleted = 0;

		for (Changeset cs : changesets.getFeatures()) {
			users.add(new User(cs.getProperties().getUid(),
					cs.getProperties().getUser()));
			created += cs.getProperties().getCreate();
			modified += cs.getProperties().getModify();
			deleted += cs.getProperties().getDelete();
		}

		System.out.println(String.format("Number of users: %d", users.size()));
		System.out.println(String.format("Created: %d", created));
		System.out.println(String.format("Modified: %d", modified));
		System.out.println(String.format("Deleted: %d", deleted));
	}

}
