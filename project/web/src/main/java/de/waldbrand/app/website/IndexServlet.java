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

package de.waldbrand.app.website;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.topobyte.jsoup.ContentGeneratable;
import de.topobyte.jsoup.JsoupServletUtil;
import de.topobyte.webgun.exceptions.WebStatusException;
import de.topobyte.webgun.resolving.PathResolver;
import de.topobyte.webgun.resolving.Redirecter;
import de.topobyte.webpaths.WebPath;
import de.topobyte.webpaths.WebPaths;
import de.waldbrand.app.website.resolving.ApiPathResolver;
import de.waldbrand.app.website.resolving.MainPathResolver;
import de.waldbrand.app.website.resolving.WesForstPathResolver;
import de.waldbrand.app.website.resolving.WesOsmPathResolver;
import de.waldbrand.app.website.util.ServletUtil;

@WebServlet("/*")
public class IndexServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	static List<PathResolver<ContentGeneratable, Void>> resolvers = new ArrayList<>();
	static {
		resolvers.add(new MainPathResolver());
		resolvers.add(new WesForstPathResolver());
		resolvers.add(new WesOsmPathResolver());
	}

	static List<PathResolver<ApiEndpoint, Void>> apiResolvers = new ArrayList<>();
	static {
		apiResolvers.add(new ApiPathResolver());
	}

	private interface Responder<T>
	{

		public void respond(int code, WebPath output,
				HttpServletResponse response, T data) throws IOException;

	}

	private <T> void tryGenerate(HttpServletResponse response, WebPath path,
			ContentGeneratable generator, Responder<T> responder, T data)
			throws IOException
	{
		if (generator != null) {
			try {
				JsoupServletUtil.respond(response, generator);
			} catch (WebStatusException e) {
				responder.respond(e.getCode(), path, response, data);
			}
		} else {
			responder.respond(404, path, response, data);
		}

	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		String uri = URLDecoder.decode(request.getRequestURI(), "UTF-8");
		WebPath path = WebPaths.get(uri);

		Map<String, String[]> parameters = request.getParameterMap();

		List<Redirecter> redirecters = new ArrayList<>();

		for (Redirecter redirecter : redirecters) {
			String location = redirecter.redirect(path, parameters);
			if (location != null) {
				response.sendRedirect(location);
				return;
			}
		}

		ApiEndpoint apiEndpoint = null;
		for (PathResolver<ApiEndpoint, Void> resolver : apiResolvers) {
			apiEndpoint = resolver.getGenerator(path, request, null);
			if (apiEndpoint != null) {
				break;
			}
		}

		if (apiEndpoint != null) {
			apiEndpoint.respond(path, response, parameters);
			return;
		}

		ContentGeneratable generator = null;

		for (PathResolver<ContentGeneratable, Void> resolver : resolvers) {
			generator = resolver.getGenerator(path, request, null);
			if (generator != null) {
				break;
			}
		}

		tryGenerate(response, path, generator, ServletUtil::respond,
				(Void) null);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		String uri = URLDecoder.decode(request.getRequestURI(), "UTF-8");
		WebPath path = WebPaths.get(uri);

		Map<String, String[]> parameters = request.getParameterMap();

		List<Redirecter> redirecters = new ArrayList<>();

		for (Redirecter redirecter : redirecters) {
			String location = redirecter.redirect(path, parameters);
			if (location != null) {
				response.sendRedirect(location);
				return;
			}
		}

		ContentGeneratable generator = null;

		for (PathResolver<ContentGeneratable, Void> resolver : resolvers) {
			generator = resolver.getGenerator(path, request, null);
			if (generator != null) {
				break;
			}
		}

		tryGenerate(response, path, generator, ServletUtil::respond,
				(Void) null);
	}

}
