package de.waldbrand.app.website.pages.internal;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.P;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.pages.base.DatabaseBaseGenerator;
import de.westnordost.osmapi.OsmConnection;
import de.westnordost.osmapi.user.UserApi;
import de.westnordost.osmapi.user.UserInfo;

public class OsmUserForwardPage extends DatabaseBaseGenerator
{

	private long id;

	public OsmUserForwardPage(WebPath path, long id)
	{
		super(path);
		this.id = id;
	}

	@Override
	protected void content()
			throws IOException, QueryException, SQLException, ServletException
	{
		OsmConnection osm = new OsmConnection(
				"https://api.openstreetmap.org/api/0.6/", "Waldbrand-Website",
				null);
		UserApi api = new UserApi(osm);
		UserInfo info = api.get(id);

		content.ac(HTML.h1("User " + info.displayName));

		P p = content.ac(HTML.p());
		p.at("Profil von ");
		p.ac(HTML.a(String.format("https://www.openstreetmap.org/user/%s",
				info.displayName), info.displayName));
		p.at(" bei OpenStreetMap.");
	}

}
