package de.waldbrand.app.website.links;

import de.topobyte.webgun.resolving.pathspec.PathSpec;
import de.topobyte.webgun.resolving.smart.defs.PathDef0;
import de.topobyte.webgun.resolving.smart.defs.PathDef1;
import de.topobyte.webgun.resolving.smart.defs.PathDef2;
import de.topobyte.webgun.resolving.smart.mappers.LongMapper;
import de.topobyte.webgun.resolving.smart.mappers.StringMapper;
import de.waldbrand.app.website.osm.PoiType;

public class LinkDefs
{

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

}
