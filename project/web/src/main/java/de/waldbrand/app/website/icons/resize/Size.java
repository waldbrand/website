package de.waldbrand.app.website.icons.resize;

import lombok.Getter;

public class Size
{

	@Getter
	private double width;
	@Getter
	private double height;

	public Size(double width, double height)
	{
		this.width = width;
		this.height = height;
	}

}
