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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UniqueIdGenerator
{

	private Set<String> taken = new HashSet<>();
	private IdGenerator idGenerator;
	private int length;

	public UniqueIdGenerator(int length)
	{
		idGenerator = new IdGenerator();
		this.length = length;
	}

	public UniqueIdGenerator(int length, long seed)
	{
		idGenerator = new IdGenerator(seed);
		this.length = length;
	}

	public UniqueIdGenerator(int length, Random random)
	{
		idGenerator = new IdGenerator(random);
		this.length = length;
	}

	public String generate()
	{
		while (true) {
			String id = idGenerator.generate(length);
			if (!taken.contains(id)) {
				taken.add(id);
				return id;
			}
		}
	}

	public void setTaken(String id)
	{
		taken.add(id);
	}

}
