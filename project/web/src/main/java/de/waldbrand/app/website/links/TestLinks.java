package de.waldbrand.app.website.links;

import de.waldbrand.app.website.osm.PoiType;

public class TestLinks
{

	public static void main(String[] args)
	{
		System.out.println(LinkDefs.OSM_MAP.getLink(PoiType.HYDRANT_OTHER));
		System.out.println(LinkDefs.OSM_MAP.getPathSpec());

		System.out.println(LinkDefs.OSM_TYPE_STATS_KEY
				.getLink(PoiType.HYDRANT_PILLAR, "foo"));
		System.out.println(LinkDefs.OSM_TYPE_STATS_KEY.getPathSpec());
	}

}
