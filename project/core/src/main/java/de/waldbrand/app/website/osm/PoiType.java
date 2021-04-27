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

package de.waldbrand.app.website.osm;

import java.util.Arrays;
import java.util.List;

import de.topobyte.osm4j.core.model.iface.OsmTag;
import de.topobyte.osm4j.core.model.impl.Tag;
import lombok.Getter;

public enum PoiType {

	SUCTION_POINT(
			"Saugstelle",
			"Saugstellen",
			Arrays.asList(new Tag("emergency", "suction_point"))),
	HYDRANT_PILLAR(
			"Überflurhydrant",
			"Überflurhydranten",
			Arrays.asList(new Tag("emergency", "fire_hydrant"),
					new Tag("fire_hydrant:type", "pillar"))),
	HYDRANT_UNDERGROUND(
			"Unterflurhydrant",
			"Unterflurhydranten",
			Arrays.asList(new Tag("emergency", "fire_hydrant"),
					new Tag("fire_hydrant:type", "underground"))),
	HYDRANT_WALL(
			"Wandhydrant",
			"Wandhydranten",
			Arrays.asList(new Tag("emergency", "fire_hydrant"),
					new Tag("fire_hydrant:type", "wall"))),
	HYDRANT_PIPE(
			"Druckloser Anschluss",
			"Drucklose Anschlüsse",
			Arrays.asList(new Tag("emergency", "fire_hydrant"),
					new Tag("fire_hydrant:type", "pipe"))),
	HYDRANT_OTHER(
			"Unspezifizierter Hydrant",
			"Unspezifizierte Hydranten",
			Arrays.asList(new Tag("emergency", "fire_hydrant")),
			Arrays.asList("fire_hydrant:type")),
	WATER_TANK(
			"Wasserbecken",
			"Wasserbecken",
			Arrays.asList(new Tag("emergency", "water_tank"))),
	WATER_POND(
			"Löschwasserteich",
			"Löschwasserteiche",
			Arrays.asList(new Tag("emergency", "fire_water_pond")));

	@Getter
	private String name;
	@Getter
	private String multiple;
	@Getter
	private List<OsmTag> tags;
	@Getter
	private List<String> missingKeys;

	private PoiType(String name, String multiple, List<OsmTag> tags)
	{
		this.name = name;
		this.multiple = multiple;
		this.tags = tags;
		this.missingKeys = null;
	}

	private PoiType(String name, String multiple, List<OsmTag> tags,
			List<String> missingKeys)
	{
		this.name = name;
		this.multiple = multiple;
		this.tags = tags;
		this.missingKeys = missingKeys;
	}

	public String getUrlKeyword()
	{
		return multiple.toLowerCase().replace(" ", "-").replace("ü", "ue")
				.replace("ö", "oe").replace("ä", "ae").replace("ß", "ss");
	}

}
