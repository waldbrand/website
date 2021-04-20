package de.waldbrand.app.website.util;

import lombok.Getter;

public enum MarkerShape {

	CIRCLE("circle"),
	SQUARE("square"),
	STAR("star"),
	PENTA("penta");

	@Getter
	private String id;

	MarkerShape(String id)
	{
		this.id = id;
	}

}
