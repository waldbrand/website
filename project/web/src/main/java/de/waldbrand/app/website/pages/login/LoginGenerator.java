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
import de.topobyte.jsoup.bootstrap4.BootstrapFormsHorizontal;
import de.topobyte.jsoup.bootstrap4.forms.InputGroup;
import de.topobyte.jsoup.components.Form;
import de.topobyte.jsoup.components.Form.Method;
import de.topobyte.jsoup.components.Input.Type;
import de.topobyte.jsoup.components.P;
import de.topobyte.jsoup.jquery.JQuery;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.pages.base.DatabaseBaseGenerator;

public class LoginGenerator extends DatabaseBaseGenerator
{

	public LoginGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content() throws QueryException, SQLException
	{
		content.ac(HTML.h1(String.format("Bei %s einloggen", Website.TITLE)));

		P p = content.ac(HTML.p());
		p.at("Der Login dient zur Administration und zum Abruf interner Informationen und ist"
				+ " nur für Mitglieder des Teams hinter der App und engagierte Mitstreiter gedacht.");

		content.ac(JQuery.focusById("username"));

		Form form = content.ac(HTML.form());
		form.setMethod(Method.POST);
		form.addClass("form-horizontal");

		BootstrapFormsHorizontal forms = new BootstrapFormsHorizontal();

		InputGroup inputUsername = forms.addInput(form, "user", "Nutzername");
		inputUsername.getInput().setPlaceholder("Nutzername");
		inputUsername.getInput().setId("username");

		InputGroup inputPassword = forms.addInput(form, "pass", "Passwort");
		inputPassword.getInput().setPlaceholder("Passwort");
		inputPassword.getInput().setType(Type.PASSWORD);

		forms.addSubmit(form, "einloggen");
	}

}
