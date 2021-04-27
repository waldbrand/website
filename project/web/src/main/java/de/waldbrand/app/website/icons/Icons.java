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

import static de.waldbrand.app.website.icons.Icon.FLACHBRUNNEN;
import static de.waldbrand.app.website.icons.Icon.HYDRANT;
import static de.waldbrand.app.website.icons.Icon.SAUGSTELLE;
import static de.waldbrand.app.website.icons.Icon.SPEICHER;
import static de.waldbrand.app.website.icons.Icon.TIEFBRUNNEN_E;
import static de.waldbrand.app.website.icons.Icon.TIEFBRUNNEN_T;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Icons
{

	private static List<Icon> icons = new ArrayList<>();
	static {
		icons.add(FLACHBRUNNEN);
		icons.add(SAUGSTELLE);
		icons.add(HYDRANT);
		icons.add(SPEICHER);
		icons.add(TIEFBRUNNEN_E);
		icons.add(TIEFBRUNNEN_T);
	}

	private static Map<String, Icon> nameToIcon = new HashMap<>();
	static {
		for (Icon icon : icons) {
			nameToIcon.put(icon.getName(), icon);
		}
	}

	public static List<Icon> getAll()
	{
		return Collections.unmodifiableList(icons);
	}

}
