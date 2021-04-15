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
	SAUGSTELLE_TKS(424, "Saugstelle endlich, nur mit Tragkraftspritze"),
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
