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

package de.waldbrand.app.website.resolving;

import javax.servlet.http.HttpServletRequest;

import de.topobyte.jsoup.ContentGeneratable;
import de.topobyte.webgun.resolving.pathspec.PathSpec;
import de.topobyte.webgun.resolving.pathspec.PathSpecResolver;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.pages.markdown.MarkdownResourceGenerator;
import de.waldbrand.app.website.pages.other.AboutGenerator;
import de.waldbrand.app.website.pages.other.ContactGenerator;
import de.waldbrand.app.website.pages.other.IndexGenerator;
import de.waldbrand.app.website.pages.other.RefreshGenerator;
import de.waldbrand.app.website.pages.wes.WesDetailGenerator;
import de.waldbrand.app.website.pages.wes.WesMapGenerator;

public class MainPathResolver extends PathSpecResolver<ContentGeneratable, Void>
{

	@Override
	public ContentGeneratable getGenerator(WebPath path,
			HttpServletRequest request, Void data)
	{
		if (path.getNameCount() == 0) {
			return new IndexGenerator(path);
		}
		return super.getGenerator(path, request, data);
	}

	{
		map(new PathSpec("wes"), (path, output, request, data) -> {
			return new WesMapGenerator(path);
		});
		map(new PathSpec("poi", ":id:"), (path, output, request, data) -> {
			int id = Integer.parseInt(output.getParameter("id"));
			return new WesDetailGenerator(path, id);
		});

		map(new PathSpec("kontakt"), (path, output, request, data) -> {
			return new ContactGenerator(path);
		});

		map(new PathSpec("about"), (path, output, request, data) -> {
			return new AboutGenerator(path);
		});
		map(new PathSpec("impressum"), (path, output, request, data) -> {
			return new MarkdownResourceGenerator(path,
					"markdown/de/impressum.md");
		});
		map(new PathSpec("privacy-policy"), (path, output, request, data) -> {
			return new MarkdownResourceGenerator(path,
					"markdown/de/privacy-policy.md");
		});

		map(new PathSpec("reload-data"), (path, output, request, data) -> {
			return new RefreshGenerator(path);
		});
	}

}
