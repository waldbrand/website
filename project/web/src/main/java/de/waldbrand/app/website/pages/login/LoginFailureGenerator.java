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

package de.waldbrand.app.website.pages.login;

import java.sql.SQLException;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.P;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.pages.base.DatabaseBaseGenerator;

public class LoginFailureGenerator extends DatabaseBaseGenerator
{

	public LoginFailureGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content() throws QueryException, SQLException
	{
		content.ac(HTML.h1("Einloggen fehlgeschlagen"));

		P p = content.ac(HTML.p());
		p.at("Irgendwas ist schiefgelaufen. Überprüfe Nutzername und Passwort.");

		p = content.ac(HTML.p());
		p.at("Falls du dein Passwort vergessen hast, schreibe bitte eine Email an ");
		p.ac(HTML.a("mailto:" + Website.CONTACT, Website.CONTACT));
		p.at(" und wir werden es für dich zurücksetzen.");
	}

}
