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
