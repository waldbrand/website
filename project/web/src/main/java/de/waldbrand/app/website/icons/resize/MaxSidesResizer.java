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

package de.waldbrand.app.website.icons.resize;

public class MaxSidesResizer implements Resizer
{

	private int maxSize;

	public MaxSidesResizer(int maxSize)
	{
		this.maxSize = maxSize;
	}

	@Override
	public Size resize(Size size)
	{
		double max = Math.max(size.getWidth(), size.getHeight());
		double factor = maxSize / max;
		double w = factor < 1 ? size.getWidth() * factor : size.getWidth();
		double h = factor < 1 ? size.getHeight() * factor : size.getHeight();
		return new Size(w, h);
	}

}
