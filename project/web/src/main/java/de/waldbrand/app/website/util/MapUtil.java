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

package de.waldbrand.app.website.util;

import static de.topobyte.jsoup.ElementBuilder.script;
import static de.topobyte.jsoup.ElementBuilder.styleSheet;

import java.util.Locale;

import org.jsoup.nodes.DataNode;
import org.locationtech.jts.geom.Coordinate;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.components.Head;
import de.topobyte.jsoup.components.Script;
import de.topobyte.jsoup.nodes.Element;
import de.topobyte.webgun.util.CacheBuster;
import de.waldbrand.app.website.Config;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.config.MapPosition;

public class MapUtil
{

	private static final String DEFAULT_MARKER_ID = "redMarker";

	public static String getDefaultMarkerId()
	{
		return DEFAULT_MARKER_ID;
	}

	public static void head(Head head)
	{
		CacheBuster cacheBuster = Website.INSTANCE.getCacheBuster();

		head.ac(styleSheet(cacheBuster.resolve("client/leaflet/leaflet.css")));
		head.ac(script(cacheBuster.resolve("client/leaflet/leaflet.js")));
		head.ac(styleSheet(cacheBuster
				.resolve("client/leaflet.markercluster/MarkerCluster.css")));
		head.ac(styleSheet(cacheBuster.resolve(
				"client/leaflet.markercluster/MarkerCluster.Default.css")));
		head.ac(script(cacheBuster.resolve(
				"client/leaflet.markercluster/leaflet.markercluster.js")));
		head.ac(styleSheet(
				"https://use.fontawesome.com/releases/v5.7.2/css/all.css"));
		head.ac(styleSheet(cacheBuster.resolve(
				"client/leaflet-extra-markers/css/leaflet.extra-markers.min.css")));
		head.ac(script(cacheBuster.resolve(
				"client/leaflet-extra-markers/js/leaflet.extra-markers.js")));
	}

	public static void addMap(Element<?> content)
	{
		MapPosition position = Config.INSTANCE.getDefaultMapPosition();
		addMap(content, position.getLat(), position.getLon(),
				position.getZoom());
	}

	public static void addMap(Element<?> content, Coordinate coordinate,
			int zoom)
	{
		addMap(content, coordinate.getY(), coordinate.getX(), zoom);
	}

	public static void addMap(Element<?> content, double lat, double lon,
			int zoom)
	{
		Div divMap = content.ac(HTML.div());
		divMap.attr("id", "map");
		divMap.attr("style", "height: 80vh");

		String mapTilesPattern = Config.INSTANCE.getMapTilesPattern();
		if (mapTilesPattern == null) {
			mapTilesPattern = "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png";
		}

		StringBuilder code = new StringBuilder();

		code.append(String.format(
				"var map = L.map('map', {minZoom: 4, maxZoom: 19}).setView([%f, %f], %d);\n",
				lat, lon, zoom));
		code.append("\n");
		code.append("L.tileLayer('" + mapTilesPattern + "', {\n");
		code.append("    maxZoom: 19,\n");
		code.append(
				"    attribution: '&copy; <a href=\"https://osm.org/copyright\">OpenStreetMap</a> contributors'\n");
		code.append("    }).addTo(map);\n");

		Script script = content.ac(HTML.script());
		script.ac(new DataNode(code.toString()));
	}

	public static void addMarkerDef(Element<?> content, MarkerShape shape,
			String markerColor)
	{
		StringBuilder code = new StringBuilder();

		code.append("var " + DEFAULT_MARKER_ID + " = L.AwesomeMarkers.icon({");
		code.append("    markerColor: '" + markerColor + "',");
		code.append("    shape: '" + shape.getId() + "'");
		code.append("  });");

		Script script = content.ac(HTML.script());
		script.ac(new DataNode(code.toString()));
	}

	public static void addMarkerDef(Element<?> content, MarkerShape shape,
			String markerColor, String prefix, String icon)
	{
		addMarkerDef(content, DEFAULT_MARKER_ID, shape, markerColor, prefix,
				icon);
	}

	public static void addMarkerDef(Element<?> content, String id,
			MarkerShape shape, String markerColor, String prefix, String icon)
	{
		StringBuilder code = new StringBuilder();

		code.append("var " + id + " = L.ExtraMarkers.icon({");
		code.append("    icon: '" + icon + "',");
		code.append("    prefix: '" + prefix + "',");
		code.append("    markerColor: '" + markerColor + "',");
		code.append("    shape: '" + shape.getId() + "'");
		code.append("  });");

		Script script = content.ac(HTML.script());
		script.ac(new DataNode(code.toString()));
	}

	public static void addMarkerDef(Element<?> content, String mapId, String id,
			MarkerShape shape, String markerColor, String prefix, String icon)
	{
		StringBuilder code = new StringBuilder();

		code.append(mapId + ".set('" + id + "', L.ExtraMarkers.icon({");
		code.append("    icon: '" + icon + "',");
		code.append("    prefix: '" + prefix + "',");
		code.append("    markerColor: '" + markerColor + "',");
		code.append("    shape: '" + shape.getId() + "'");
		code.append("  }));");

		Script script = content.ac(HTML.script());
		script.ac(new DataNode(code.toString()));
	}

	public static void markerStart(StringBuilder code)
	{
		code.append(
				"var markers = L.markerClusterGroup({showCoverageOnHover: false, maxClusterRadius: 40});");
	}

	public static void markerEnd(StringBuilder code)
	{
		code.append("map.addLayer(markers);");
	}

	public static void markerStart(StringBuilder code, String markerId)
	{
		code.append(String.format(
				"markers.set('%s', L.markerClusterGroup({showCoverageOnHover: false, maxClusterRadius: 40}));",
				markerId));
	}

	public static void markerEnd(StringBuilder code, String markerId)
	{
		code.append(
				String.format("map.addLayer(markers.get('%s'));", markerId));
	}

	public static void marker(StringBuilder code)
	{
		code.append("var marker = L.marker([52.5, 13.4], {icon: "
				+ DEFAULT_MARKER_ID + "}).addTo(map);");
	}

	public static void addMarker(StringBuilder code, double latitude,
			double longitude, String text)
	{
		addMarker(code, latitude, longitude, text, DEFAULT_MARKER_ID,
				"markers");
	}

	public static void addMarker(StringBuilder code, double latitude,
			double longitude, String text, String markerId, String markers)
	{
		String coords = String.format(Locale.US, "%f, %f", latitude, longitude);
		code.append("var marker = L.marker([" + coords + "], {icon: " + markerId
				+ "});");
		code.append(markers + ".addLayer(marker);");
		code.append("marker.bindPopup('" + sane(text) + "');");
		code.append("\n");
	}

	public static String sane(String name)
	{
		if (name == null) {
			return null;
		}
		// replace "\" with "\\"
		// replace "'" with "\'"
		// replace "\n" with "<br />"
		return name.replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'")
				.replaceAll("\n", "<br />");
	}

}
