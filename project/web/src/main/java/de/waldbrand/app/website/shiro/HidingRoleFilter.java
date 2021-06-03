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

package de.waldbrand.app.website.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import de.topobyte.webpaths.WebPath;
import de.topobyte.webpaths.WebPaths;
import de.waldbrand.app.website.util.ServletUtil;

public class HidingRoleFilter extends RolesAuthorizationFilter
{

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws IOException
	{
		Subject subject = getSubject(request, response);
		// If the subject isn't identified, redirect to login URL
		if (subject.getPrincipal() == null) {
			saveRequestAndRedirectToLogin(request, response);
		} else {
			String uri = WebUtils.toHttp(request).getRequestURI();
			WebPath path = WebPaths.get(uri);
			ServletUtil.respond(404, path, WebUtils.toHttp(response), null);
		}
		return false;
	}

}
