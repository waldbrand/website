package de.waldbrand.app.website.pages.base;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import de.topobyte.luqe.iface.QueryException;
import de.topobyte.pagegen.core.LinkResolverContentGeneratable;
import de.topobyte.weblogin.WebloginContentGenerator;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;

public class WebloginContentGeneratorImpl extends DatabaseBaseGenerator
		implements LinkResolverContentGeneratable
{

	private WebloginContentGenerator delegate;

	public WebloginContentGeneratorImpl(WebPath path,
			WebloginContentGenerator delegate)
	{
		super(path);
		this.delegate = delegate;
	}

	@Override
	protected void content()
			throws IOException, QueryException, SQLException, ServletException
	{
		delegate.content(Website.INSTANCE, this, content, db, loginDao);
	}

}
