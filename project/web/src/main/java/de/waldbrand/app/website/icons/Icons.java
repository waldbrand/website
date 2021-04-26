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
