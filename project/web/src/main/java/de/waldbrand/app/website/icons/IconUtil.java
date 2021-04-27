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

import de.topobyte.cachebusting.CacheBusting;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.Img;
import de.topobyte.jsoup.nodes.Element;
import de.waldbrand.app.website.icons.resize.MaxVolumeResizer;
import de.waldbrand.app.website.icons.resize.Resizer;
import de.waldbrand.app.website.icons.resize.Size;

public class IconUtil
{

	public static void icon(Element<?> e, Icon icon)
	{
		Img img = e.ac(HTML.img("/" + CacheBusting.resolve(icon.getPath())));
		Size size = getResizer()
				.resize(new Size(icon.getWidth(), icon.getHeight()));
		img.attr("width", Double.toString(size.getWidth()));
		img.attr("height", Double.toString(size.getHeight()));
	}

	public static Resizer getResizer()
	{
		return new MaxVolumeResizer(1400);
	}

}
