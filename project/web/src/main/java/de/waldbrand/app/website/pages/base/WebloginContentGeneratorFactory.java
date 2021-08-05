package de.waldbrand.app.website.pages.base;

import de.topobyte.pagegen.core.LinkResolverContentGeneratable;
import de.topobyte.weblogin.WebloginContentGeneratableFactory;
import de.topobyte.weblogin.WebloginContentGenerator;
import de.topobyte.webpaths.WebPath;

public class WebloginContentGeneratorFactory
		implements WebloginContentGeneratableFactory
{

	@Override
	public LinkResolverContentGeneratable get(WebPath path,
			WebloginContentGenerator generator)
	{
		return new WebloginContentGeneratorImpl(path, generator);
	}

}
