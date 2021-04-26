package de.waldbrand.app.website.icons;

import lombok.Getter;

public enum Icon {

	FLACHBRUNNEN("flachbrunnen.svg", 100, 85),
	SAUGSTELLE("saugstelle.svg", 173, 48),
	HYDRANT("hydrant.svg", 100, 85),
	SPEICHER("speicher.svg", 100, 85),
	TIEFBRUNNEN_E("tiefbrunnen-e.svg", 100, 85),
	TIEFBRUNNEN_T("tiefbrunnen-t.svg", 100, 85);

	@Getter
	private String filename;
	@Getter
	private int width;
	@Getter
	private int height;

	private Icon(String filename, int width, int height)
	{
		this.filename = filename;
		this.width = width;
		this.height = height;
	}

	public String getName()
	{
		return name().toLowerCase();
	}

}
