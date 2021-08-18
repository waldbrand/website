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

package de.waldbrand.app.website.lbforst.model;

import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import lombok.Getter;

public class RettungspunktPoi
{

	@Getter
	private int id;
	@Getter
	private int nr;
	@Getter
	private int hochW;
	@Getter
	private int rechtsW;

	public RettungspunktPoi(int id, int nr, int hochW, int rechtsW)
	{
		this.id = id;
		this.nr = nr;
		this.hochW = hochW;
		this.rechtsW = rechtsW;
	}

	private static Projection projection;
	static {
		try {
			projection = new Projection();
		} catch (FactoryException e) {
			System.out.println("Error while initializing projection: " + e);
			e.printStackTrace();
		}
	}

	public Coordinate getCoordinate()
	{
		try {
			return projection.transform(hochW, rechtsW);
		} catch (TransformException e) {
			e.printStackTrace();
			return new Coordinate();
		}
	}

}
