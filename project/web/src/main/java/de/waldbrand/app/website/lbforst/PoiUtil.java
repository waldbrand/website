package de.waldbrand.app.website.lbforst;

import com.google.common.collect.Iterables;

import de.waldbrand.app.website.lbforst.model.Poi;

public class PoiUtil
{

	public static Iterable<Poi> not(Iterable<Poi> pois, int oart)
	{
		return Iterables.filter(pois, p -> p.getOart() != oart);
	}

	public static Iterable<Poi> only(Iterable<Poi> pois, int oart)
	{
		return Iterables.filter(pois, p -> p.getOart() == oart);
	}

}
