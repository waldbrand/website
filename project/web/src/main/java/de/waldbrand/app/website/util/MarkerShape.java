package de.waldbrand.app.website.util;

import lombok.Getter;

public enum MarkerShape {

	CIRCLE("circle"),
	SQUARE("square");

	@Getter
	private String id;

	MarkerShape(String id)
	{
		this.id = id;
	}

}
