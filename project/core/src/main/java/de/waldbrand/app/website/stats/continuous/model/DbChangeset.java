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

package de.waldbrand.app.website.stats.continuous.model;

import lombok.Getter;

public class DbChangeset
{

	@Getter
	private long id;
	@Getter
	private long createdAt;
	@Getter
	private long closedAt;
	@Getter
	private boolean open;
	@Getter
	private String user;
	@Getter
	private long userId;
	@Getter
	private int numChanges;
	@Getter
	private int numComments;

	public DbChangeset(long id, long createdAt, long closedAt, boolean open,
			String user, long userId, int numChanges, int numComments)
	{
		this.id = id;
		this.open = open;
		this.createdAt = createdAt;
		this.closedAt = closedAt;
		this.user = user;
		this.userId = userId;
		this.numChanges = numChanges;
		this.numComments = numComments;
	}

}
