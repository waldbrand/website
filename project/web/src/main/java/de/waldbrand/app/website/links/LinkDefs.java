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

	/*
	 * Allgemein
	 */

	public static PathDef0 IMPRINT = new PathDef0(new PathSpec("impressum"));
	public static PathDef0 PRIVACY_POLICY = new PathDef0(
			new PathSpec("privacy-policy"));
	public static PathDef0 CONTACT = new PathDef0(new PathSpec("kontakt"));

	public static PathDef0 MAP = new PathDef0(
			new PathSpec("wes", "map", "osm-forst"));

	/*
	 * OSM
	 */

	public static PathDef0 OSM = new PathDef0(new PathSpec("osm"));
	public static PathDef0 OSM_STATS = new PathDef0(
			new PathSpec("osm", "stats"));
	public static PathDef0 OSM_MAPPING = new PathDef0(
			new PathSpec("osm", "mapping"));
	public static PathDef0 OSM_MAP_ALL = new PathDef0(
			new PathSpec("osm", "map", "alles"));

	public static PathDef1<Long> OSM_NODE = new PathDef1<>(
			new PathSpec("osm", "node", ":id:"), new LongMapper());

	public static PathDef1<Long> OSM_WAY = new PathDef1<>(
			new PathSpec("osm", "way", ":id:"), new LongMapper());

	public static PathDef1<Long> OSM_RELATION = new PathDef1<>(
			new PathSpec("osm", "relation", ":id:"), new LongMapper());

	public static PathDef1<PoiType> OSM_MAP = new PathDef1<>(
			new PathSpec("osm", "map", ":type:"), new PoiTypeMapper());

	public static PathDef1<PoiType> OSM_TYPE_STATS = new PathDef1<>(
			new PathSpec("osm", "type-stats", ":type:"), new PoiTypeMapper());

	public static PathDef2<PoiType, String> OSM_TYPE_STATS_KEY = new PathDef2<>(
			new PathSpec("osm", "type-stats", ":type:", "key", ":key:"),
			new PoiTypeMapper(), new StringMapper());

	/*
	 * Forst
	 */

	public static PathDef0 FORST = new PathDef0(new PathSpec("wes"));
	public static PathDef0 FORST_MAP = new PathDef0(new PathSpec("wes", "map"));

	public static PathDef0 FORST_MAP_FILTER_LANDKREIS_SELECT = new PathDef0(
			new PathSpec("wes", "map", "filter-landkreis-select"));

	public static PathDef1<String> FORST_MAP_LANDKREIS = new PathDef1<>(
			new PathSpec("wes", "map", "landkreis", ":kreis:"),
			new StringMapper());

	public static PathDef0 FORST_MAP_FILTER_OART_SELECT = new PathDef0(
			new PathSpec("wes", "map", "filter-oart-select"));

	public static PathDef1<Integer> FORST_MAP_OART = new PathDef1<>(
			new PathSpec("wes", "map", "oart", ":oart:"), new IntMapper());

	public static PathDef1<Integer> FORST_POI = new PathDef1<>(
			new PathSpec("poi", ":id:"), new IntMapper());

	public static PathDef0 FORST_STATS_OART = new PathDef0(
			new PathSpec("wes", "stats", "oart"));
	public static PathDef0 FORST_STATS_BAUJAHR = new PathDef0(
			new PathSpec("wes", "stats", "baujahr"));
	public static PathDef0 FORST_STATS_FSTATUS = new PathDef0(
			new PathSpec("wes", "stats", "fstatus"));
	public static PathDef0 FORST_STATS_FKT_FAEHIG = new PathDef0(
			new PathSpec("wes", "stats", "fkt_faehig"));
	public static PathDef0 FORST_STATS_MENGE = new PathDef0(
			new PathSpec("wes", "stats", "menge"));

	public static PathDef0 FORST_ADD = new PathDef0(
			new PathSpec("wes", "eintragen"));

}
