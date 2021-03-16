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

import java.util.HashMap;
import java.util.Map;

import de.waldbrand.app.website.Config;
import de.waldbrand.app.website.model.Poi;

public class NameUtil
{

	public static String getName(Poi poi)
	{
		String name = String.format("%s %d", typeName(poi.getOart()),
				poi.getId());
		if (name == null) {
			name = Config.INSTANCE.getNullName();
		}
		return name;
	}

	private static Map<Integer, String> oartToName = new HashMap<>();

	// Spezifiert in Kleiner Anfrage 6/9805, Seite 6
	static {
		oartToName.put(400,
				"Wasserentnahmestelle geplant (ohne Spezifikation)");
		oartToName.put(410, "Saugstelle unendlich");
		oartToName.put(414, "Saugstelle unendlich, nur mit Tragkraftspritze");
		oartToName.put(420, "Saugstelle endlich");
		oartToName.put(424, "Saugstelle endlich, nur mit Tragkraftspritze");
		oartToName.put(430, "Grundwassertiefbrunnen");
		oartToName.put(440, "Flachspiegelbrunnen");
		oartToName.put(450, "Hydrant");
		oartToName.put(460, "Staueinrichtung, nutzbar");
		oartToName.put(464,
				"Staueinrichtung, nutzbar nur mit Tragkraft Spritze");
		oartToName.put(470, "Löschwasserteich");
		oartToName.put(480, "Zisterne, Tank");
	}

	public static String typeName(int oart)
	{
		String name = oartToName.get(oart);
		return name != null ? name : Integer.toString(oart);
	}

}
