package de.waldbrand.app.website;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import de.topobyte.webpaths.WebPath;

public interface ApiEndpoint
{

	public void respond(WebPath path, HttpServletResponse response,
			Map<String, String[]> parameters) throws IOException;

}
