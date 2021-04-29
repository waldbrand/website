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
import de.topobyte.webgun.resolving.smart.SmartPathSpecResolver;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.links.LinkDefs;
import de.waldbrand.app.website.pages.combined.WesDynamicMapAllGenerator;
import de.waldbrand.app.website.pages.login.LoginGenerator;
import de.waldbrand.app.website.pages.markdown.MarkdownResourceGenerator;
import de.waldbrand.app.website.pages.other.AboutGenerator;
import de.waldbrand.app.website.pages.other.ContactGenerator;
import de.waldbrand.app.website.pages.other.IndexGenerator;
import de.waldbrand.app.website.pages.other.RefreshGenerator;

public class MainPathResolver
		extends SmartPathSpecResolver<ContentGeneratable, Void>
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
		map(LinkDefs.CONTACT,
				(path, output, request, data) -> new ContactGenerator(path));

		map(LinkDefs.ABOUT,
				(path, output, request, data) -> new AboutGenerator(path));
		map(LinkDefs.IMPRINT,
				(path, output, request, data) -> new MarkdownResourceGenerator(
						path, "markdown/de/impressum.md"));
		map(LinkDefs.PRIVACY_POLICY,
				(path, output, request, data) -> new MarkdownResourceGenerator(
						path, "markdown/de/privacy-policy.md"));

		map(LinkDefs.LOGIN,
				(path, output, request, data) -> new LoginGenerator(path));

		map(LinkDefs.RELOAD_DATA,
				(path, output, request, data) -> new RefreshGenerator(path));

		map(LinkDefs.MAP, (path, output, request,
				data) -> new WesDynamicMapAllGenerator(path));
	}

}
