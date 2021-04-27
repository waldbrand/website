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

package de.waldbrand.app.website.lbforst;

import lombok.Getter;

public enum WesType {

	// Spezifiziert in Kleiner Anfrage 6/9805, Seite 6
	GEPLANT(400, "Wasserentnahmestelle geplant (ohne Spezifikation)"),
	SAUGSTELLE_UNENDLICH(410, "Saugstelle unendlich"),
	SAUGSTELLE_UNENDLICH_TKS(
			414,
			"Saugstelle unendlich, nur mit Tragkraftspritze"),
	SAUGSTELLE_ENDLICH(420, "Saugstelle endlich"),
	SAUGSTELLE_ENDLICH_TKS(424, "Saugstelle endlich, nur mit Tragkraftspritze"),
	GRUNDWASSERTIEFBRUNNEN(430, "Grundwassertiefbrunnen"),
	FLACHSPIEGELBRUNEN(440, "Flachspiegelbrunnen"),
	HYDRANT(450, "Hydrant"),
	STAUEINRICHTUNG(460, "Staueinrichtung, nutzbar"),
	STAUEINRICHTUNG_TKS(
			464,
			"Staueinrichtung, nutzbar nur mit Tragkraft Spritze"),
	LOESCHWASSERTEICH(470, "Löschwasserteich"),
	ZISTERNE(480, "Zisterne, Tank");

	@Getter
	private int id;
	@Getter
	private String description;

	WesType(int id, String description)
	{
		this.id = id;
		this.description = description;
	}

}
