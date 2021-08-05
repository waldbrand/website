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

package de.waldbrand.app.website.links;

import de.topobyte.webgun.resolving.pathspec.PathSpec;
import de.topobyte.webgun.resolving.smart.defs.PathDef0;
import de.topobyte.webgun.resolving.smart.defs.PathDef1;
import de.topobyte.webgun.resolving.smart.defs.PathDef2;
import de.topobyte.webgun.resolving.smart.mappers.IntMapper;
import de.topobyte.webgun.resolving.smart.mappers.LongMapper;
import de.topobyte.webgun.resolving.smart.mappers.StringMapper;
import de.waldbrand.app.website.osm.PoiType;

public class LinkDefs
{

	public static final String GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=de.waldbrandapp.brandenburg";

	/*
	 * Allgemein
	 */

	public static PathDef0 IMPRINT = new PathDef0(new PathSpec("impressum"));
	public static PathDef0 PRIVACY_POLICY = new PathDef0(
			new PathSpec("privacy-policy"));
	public static PathDef0 CONTACT = new PathDef0(new PathSpec("kontakt"));
	public static PathDef0 ABOUT = new PathDef0(new PathSpec("about"));

	public static PathDef0 LOGIN = new PathDef0(new PathSpec("login"));

	public static PathDef0 API_POIS = new PathDef0(new PathSpec("pois"));

	public static PathDef0 RELOAD_DATA = new PathDef0(
			new PathSpec("reload-data"));

	public static PathDef0 LANDING_APP = new PathDef0(
			new PathSpec("android-app"));

	public static PathDef0 LANDING_EDITOR = new PathDef0(
			new PathSpec("wes", "editor"));

	public static PathDef0 LANDING_WES = new PathDef0(new PathSpec("wes"));

	public static PathDef0 MAP = new PathDef0(
			new PathSpec("wes", "map", "osm-forst"));
	public static PathDef0 MAP_OSM = new PathDef0(
			new PathSpec("wes", "map", "osm"));

	public static PathDef0 EDITOR = new PathDef0(
			new PathSpec("mapcomplete", "waldbrand.html#start"));

	public static PathDef0 EDITOR_TEST = new PathDef0(new PathSpec(
			"mapcomplete",
			"waldbrand.html?z=15&lat=53.13066&lon=13.85354&test=true#start"));

	/*
	 * Editor-Statistiken
	 */

	public static PathDef0 OSM_CONTRIBUTIONS = new PathDef0(
			new PathSpec("internal", "osm", "contributions"));

	/*
	 * OSM
	 */

	public static PathDef0 OSM = new PathDef0(new PathSpec("wes", "osm"));
	public static PathDef0 OSM_STATS = new PathDef0(
			new PathSpec("wes", "osm", "stats"));
	public static PathDef0 OSM_MAPPING = new PathDef0(
			new PathSpec("wes", "osm", "mapping"));
	public static PathDef0 OSM_MAP_ALL = new PathDef0(
			new PathSpec("wes", "osm", "map", "alles"));

	public static PathDef1<Long> OSM_NODE = new PathDef1<>(
			new PathSpec("wes", "osm", "node", ":id:"), new LongMapper());

	public static PathDef1<Long> OSM_WAY = new PathDef1<>(
			new PathSpec("wes", "osm", "way", ":id:"), new LongMapper());

	public static PathDef1<Long> OSM_RELATION = new PathDef1<>(
			new PathSpec("wes", "osm", "relation", ":id:"), new LongMapper());

	public static PathDef1<PoiType> OSM_MAP = new PathDef1<>(
			new PathSpec("wes", "osm", "map", ":type:"), new PoiTypeMapper());

	public static PathDef1<PoiType> OSM_TYPE_STATS = new PathDef1<>(
			new PathSpec("wes", "osm", "type-stats", ":type:"),
			new PoiTypeMapper());

	public static PathDef2<PoiType, String> OSM_TYPE_STATS_KEY = new PathDef2<>(
			new PathSpec("wes", "osm", "type-stats", ":type:", "key", ":key:"),
			new PoiTypeMapper(), new StringMapper());

	/*
	 * Forst
	 */

	public static PathDef0 FORST = new PathDef0(new PathSpec("wes", "forst"));
	public static PathDef0 FORST_MAP = new PathDef0(
			new PathSpec("wes", "forst", "map"));

	public static PathDef0 FORST_MAP_FILTER_LANDKREIS_SELECT = new PathDef0(
			new PathSpec("wes", "forst", "map", "filter-landkreis-select"));

	public static PathDef1<String> FORST_MAP_LANDKREIS = new PathDef1<>(
			new PathSpec("wes", "forst", "map", "landkreis", ":kreis:"),
			new StringMapper());

	public static PathDef0 FORST_MAP_FILTER_OART_SELECT = new PathDef0(
			new PathSpec("wes", "forst", "map", "filter-oart-select"));

	public static PathDef1<Integer> FORST_MAP_OART = new PathDef1<>(
			new PathSpec("wes", "forst", "map", "oart", ":oart:"),
			new IntMapper());

	public static PathDef1<Integer> FORST_POI = new PathDef1<>(
			new PathSpec("wes", "forst", "poi", ":id:"), new IntMapper());

	public static PathDef0 FORST_STATS_OART = new PathDef0(
			new PathSpec("wes", "forst", "stats", "oart"));
	public static PathDef0 FORST_STATS_BAUJAHR = new PathDef0(
			new PathSpec("wes", "forst", "stats", "baujahr"));
	public static PathDef0 FORST_STATS_FSTATUS = new PathDef0(
			new PathSpec("wes", "forst", "stats", "fstatus"));
	public static PathDef0 FORST_STATS_FKT_FAEHIG = new PathDef0(
			new PathSpec("wes", "forst", "stats", "fkt_faehig"));
	public static PathDef0 FORST_STATS_MENGE = new PathDef0(
			new PathSpec("wes", "forst", "stats", "menge"));

	public static PathDef0 FORST_ADD = new PathDef0(
			new PathSpec("wes", "forst", "eintragen"));

}
