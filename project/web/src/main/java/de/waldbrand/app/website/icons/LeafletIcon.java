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

package de.waldbrand.app.website.icons;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import de.topobyte.cachebusting.CacheBusting;
import de.waldbrand.app.website.icons.resize.MaxVolumeResizer;
import de.waldbrand.app.website.icons.resize.Resizer;
import de.waldbrand.app.website.icons.resize.Size;

public class LeafletIcon
{

	final static Logger logger = LoggerFactory.getLogger(LeafletIcon.class);

	private String id;
	private String image;
	private String shadow;
	private double width;
	private double height;

	public LeafletIcon(String id, String image, String shadow, double width,
			double height)
	{
		this.id = id;
		this.image = image;
		this.shadow = shadow;
		this.width = width;
		this.height = height;
	}

	@Override
	public String toString()
	{
		try {
			return string();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String string() throws IOException
	{
		StringBuilder strb = new StringBuilder();
		strb.append(String.format("%s.set('%s', L.icon(", "icons", id));

		JsonObject json = new JsonObject();
		json.addProperty("iconUrl", "/" + CacheBusting.resolve(image));
		if (shadow != null) {
			json.addProperty("shadowUrl", "/" + CacheBusting.resolve(shadow));
		}

		Resizer resizer = new MaxVolumeResizer(1400);
		Size size = resizer.resize(new Size(width, height));

		json.add("iconSize", array(size.getWidth(), size.getHeight()));
		json.add("shadowSize", array(50, 64));
		json.add("iconAnchor", array(size.getWidth() / 2, size.getHeight()));
		if (shadow != null) {
			json.add("shadowAnchor", array(4, 62));
		}
		json.add("popupAnchor", array(0, -85 / 3.));

		strb.append(json);
		strb.append("));");

		return strb.toString();
	}

	private JsonArray array(double a, double b)
	{
		JsonArray array = new JsonArray(2);
		array.add(new JsonPrimitive(a));
		array.add(new JsonPrimitive(b));
		return array;
	}

}
