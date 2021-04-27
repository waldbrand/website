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
