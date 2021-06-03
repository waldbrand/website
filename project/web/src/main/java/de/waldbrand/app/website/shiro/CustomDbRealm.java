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
