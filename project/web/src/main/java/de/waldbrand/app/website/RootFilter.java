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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.cachebusting.CacheBusting;
import de.topobyte.servlet.utils.Servlets;

@WebFilter("/*")
public class RootFilter implements Filter
{

	final static Logger logger = LoggerFactory.getLogger(RootFilter.class);

	private static Set<String> staticFiles = new HashSet<>();
	private static List<String> staticPatterns = new ArrayList<>();

	static {
		staticPatterns.add("/client/");
		staticPatterns.add("/images/");
		for (String entry : CacheBusting.getValues()) {
			staticFiles.add("/" + entry);
		}
		staticFiles.add("/googleb4d9f938be253b7a.html");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		// nothing to do here
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		if (request instanceof HttpServletRequest) {
			HttpServletRequest hsr = (HttpServletRequest) request;

			String path = hsr.getRequestURI();

			if (staticFiles.contains(path)) {
				HttpServletResponse r = (HttpServletResponse) response;
				r.setHeader("Cache-Control", "public, max-age=31536000");
				Servlets.forwardToDefault(request, response);
				return;
			}
			for (String staticPrefix : staticPatterns) {
				if (path.startsWith(staticPrefix)) {
					Servlets.forwardToDefault(request, response);
					return;
				}
			}

			// Normalize multiple consecutive forward slashes to single slashes
			if (path.contains("//")) {
				String newUri = path.replaceAll("/+/", "/");
				((HttpServletResponse) response).sendRedirect(newUri);
				return;
			}

			request.setCharacterEncoding("UTF-8");
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{
	}

}
