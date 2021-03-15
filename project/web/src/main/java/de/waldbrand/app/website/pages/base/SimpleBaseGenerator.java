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

package de.waldbrand.app.website.pages.base;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.jsoup.Exceptions;
import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.P;
import de.topobyte.webgun.exceptions.PageNotFoundException;
import de.topobyte.webgun.exceptions.WebStatusException;
import de.topobyte.webpaths.WebPath;

public abstract class SimpleBaseGenerator extends BaseGenerator
{

	final static Logger logger = LoggerFactory
			.getLogger(SimpleBaseGenerator.class);

	public SimpleBaseGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	public void generate() throws IOException, PageNotFoundException
	{
		super.generate();

		menu();

		try {
			content();
		} catch (WebStatusException e) {
			throw e;
		} catch (Throwable e) {
			P p = content.ac(HTML.p());
			p.appendText("Error while generating content:");
			p.ac(HTML.br());
			Exceptions.appendStackTrace(p, e);
		}

		footer();
	}

	protected abstract void content() throws Throwable;

}
