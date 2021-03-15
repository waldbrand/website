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

package de.waldbrand.app.website.model;

import org.locationtech.jts.geom.Coordinate;

import lombok.Getter;

public class Poi
{

	@Getter
	private int id;
	@Getter
	private long oart;
	@Getter
	private String bemerkung;
	@Getter
	private long hochW;
	@Getter
	private long rechtsW;

	public Poi(int id, long oart, String bemerkung, long hochW, long rechtsW)
	{
		this.id = id;
		this.oart = oart;
		this.bemerkung = bemerkung;
		this.hochW = hochW;
		this.rechtsW = rechtsW;
	}

	public Coordinate getCoordinate()
	{
		// TODO implement
		return null;
	}

}
