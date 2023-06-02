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

package de.waldbrand.app.website;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import de.topobyte.luqe.iface.QueryException;

public class RunResetChangesetDatabase
{

	public static void main(String[] args)
			throws IOException, QueryException, SQLException
	{
		if (args.length != 1) {
			System.out
					.println("Usage: reset-changeset-database <database file>");
			System.exit(1);
		}

		Path file = Paths.get(args[0]);

		ResetChangesetDatabase task = new ResetChangesetDatabase(file);
		task.execute();
	}

}
