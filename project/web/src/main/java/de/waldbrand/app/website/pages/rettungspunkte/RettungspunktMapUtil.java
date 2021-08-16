package de.waldbrand.app.website.pages.rettungspunkte;

import de.waldbrand.app.website.lbforst.model.RettungspunktPoi;

public class RettungspunktMapUtil
{

	public static String content(RettungspunktPoi poi)
	{
		String number = Integer.toString(poi.getId());
		return number;
	}

}
