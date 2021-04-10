package de.waldbrand.app.website.pages.osm.maps;

import org.jsoup.nodes.Node;

import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.waldbrand.app.website.OsmLinks;
import de.waldbrand.app.website.osm.PoiType;
import de.waldbrand.app.website.util.MapUtil;

public class OsmMapUtil
{

	public static void marker(StringBuilder code, OsmNode node, PoiType type,
			String markerId)
	{
		Node link = OsmLinks.link(node, "Details");
		String content = type.getName() + "<br>OSM node " + node.getId() + "  "
				+ link;
		MapUtil.addMarker(code, node.getLatitude(), node.getLongitude(),
				content, markerId);
	}

}
