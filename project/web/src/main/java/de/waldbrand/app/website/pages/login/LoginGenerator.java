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

		content.ac(HTML.h2("Neu hier?"));
		P p = content.ac(HTML.p());
		p.at("Du hast noch keinen Account? Jetzt ");
		p.ac(HTML.a("/signup", "registrieren"));
		p.at(".");
	}

}
