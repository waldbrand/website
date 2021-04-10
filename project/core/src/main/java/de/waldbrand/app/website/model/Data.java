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

package de.waldbrand.app.website.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.simplemapfile.core.EntityFile;
import de.waldbrand.app.website.osm.PoiType;
import lombok.Getter;
import lombok.Setter;

public class Data
{

	@Getter
	@Setter
	private List<Poi> pois = new ArrayList<>();

	@Getter
	private Map<Integer, Poi> idToPoi = new HashMap<>();

	@Getter
	private Map<String, EntityFile> idToEntity = new TreeMap<>();

	@Getter
	private Map<PoiType, List<OsmNode>> typeToNodes = new HashMap<>();

	@Getter
	private Map<Long, OsmNode> idToNodes = new HashMap<>();

	public static String KEY_INTERNAL_ID = "internal-id";

	public void addKreis(EntityFile entity)
	{
		String name = entity.getTags().get("name:de");
		name = name.replaceAll("[()]+", "");
		name = name.replaceAll("[ ]+", "-");
		entity.addTag(KEY_INTERNAL_ID, name);
		idToEntity.put(name, entity);
	}

}
