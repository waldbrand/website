package de.waldbrand.app.website.api;

import java.util.List;

import lombok.Getter;

public class MarkerList
{

	@Getter
	private String iconId;
	@Getter
	private List<Marker> list;

	public MarkerList(String iconId, List<Marker> list)
	{
		this.iconId = iconId;
		this.list = list;
	}

}
