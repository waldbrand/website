package de.waldbrand.app.website.pages.login;

import java.sql.SQLException;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.components.P;
import de.topobyte.luqe.iface.QueryException;
import de.topobyte.webpaths.WebPath;
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
		p.ac(HTML.a("mailto:team@waldbrand-app.de", "team@waldbrand-app.de"));
		p.at(" und wir werden es für dich zurücksetzen.");
	}

}
