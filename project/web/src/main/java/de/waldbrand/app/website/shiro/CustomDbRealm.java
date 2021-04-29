package de.waldbrand.app.website.shiro;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import de.topobyte.weblogin.realm.DbRealm;
import de.topobyte.weblogin.realm.SystemUser;

public class CustomDbRealm extends DbRealm
{

	public CustomDbRealm() throws ClassNotFoundException, SQLException
	{
		super();
	}

	private static Set<String> adminUsers = new HashSet<>();
	static {
		adminUsers.add("root");
	}

	@Override
	protected void assignRoles(Set<String> roles, SystemUser user)
	{
		roles.add("user");
		if (adminUsers.contains(user.getUsername())) {
			roles.add("admin");
		}
	}

}
