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

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class Projection
{

	private CoordinateReferenceSystem source;
	private CoordinateReferenceSystem target;
	private MathTransform transform;

	public Projection() throws NoSuchAuthorityCodeException, FactoryException
	{
		source = CRS.decode("EPSG:25833");
		target = CRS.decode("EPSG:4326", true);
		transform = CRS.findMathTransform(source, target, true);
	}

	public Coordinate transform(int hochW, int rechtsW)
			throws TransformException
	{
		return JTS.transform(new Coordinate(rechtsW, hochW), null, transform);
	}

}
