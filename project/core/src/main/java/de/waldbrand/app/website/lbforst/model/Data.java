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

package de.waldbrand.app.website.lbforst.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.slimjars.dist.gnu.trove.map.TLongObjectMap;
import com.slimjars.dist.gnu.trove.map.hash.TLongObjectHashMap;

import de.topobyte.simplemapfile.core.EntityFile;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.osm.model.OsmPoi;
import lombok.Getter;
import lombok.Setter;

public class Data
{

	@Getter
	@Setter
	private List<WesPoi> wesPois = new ArrayList<>();

	@Getter
	@Setter
	private List<RettungspunktPoi> rettungspunktePois = new ArrayList<>();

	@Getter
	private Map<Integer, WesPoi> idToWesPoi = new HashMap<>();

	@Getter
	private Map<Integer, RettungspunktPoi> idToRettungspunktPoi = new HashMap<>();

	@Getter
	private Map<String, EntityFile> idToEntity = new TreeMap<>();

	@Getter
	private Map<PoiType, List<OsmPoi>> typeToPois = new HashMap<>();

	@Getter
	private TLongObjectMap<OsmPoi> idToNodes = new TLongObjectHashMap<>();
	@Getter
	private TLongObjectMap<OsmPoi> idToWays = new TLongObjectHashMap<>();

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
