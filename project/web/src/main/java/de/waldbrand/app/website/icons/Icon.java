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

package de.waldbrand.app.website.icons;

import lombok.Getter;

public enum Icon {

	FLACHBRUNNEN("flachbrunnen.svg", 100, 85),
	SAUGSTELLE("saugstelle.svg", 173, 48),
	HYDRANT("hydrant.svg", 100, 85),
	HYDRANT_UNSPEZIFIZIERT("hydrant-unklar.svg", 100, 85),
	SPEICHER("speicher.svg", 100, 85),
	TIEFBRUNNEN("tiefbrunnen.svg", 100, 85),
	TIEFBRUNNEN_E("tiefbrunnen-e.svg", 100, 85),
	TIEFBRUNNEN_T("tiefbrunnen-t.svg", 100, 85),
	RETTUNGSPUNKT("rettungspunkt.svg", 100, 100);

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

	public String getPath()
	{
		return "marker/" + filename;
	}

}
