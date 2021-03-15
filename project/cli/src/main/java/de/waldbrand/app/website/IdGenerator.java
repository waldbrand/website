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

import java.util.Random;

public class IdGenerator
{

	public static final String ALPHABET = "123456789abcdefghjklmnpqrstuvwxyz";

	private Random random;

	public IdGenerator()
	{
		random = new Random();
	}

	public IdGenerator(long seed)
	{
		random = new Random(seed);
	}

	public IdGenerator(Random random)
	{
		this.random = random;
	}

	public String generate(int len)
	{
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int pos = random.nextInt(ALPHABET.length());
			buffer.append(ALPHABET.charAt(pos));
		}
		return buffer.toString();
	}

}
