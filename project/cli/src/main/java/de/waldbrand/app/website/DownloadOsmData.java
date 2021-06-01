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
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DownloadOsmData
{

	private static final String URL = "https://download.geofabrik.de/europe/germany/brandenburg-latest.osm.pbf";

	private Path dir;

	public DownloadOsmData(Path dir)
	{
		this.dir = dir;
	}

	public void execute() throws IOException
	{
		Path fileOutput = dir.resolve("Brandenburg-Berlin.pbf");
		System.out.println("output: " + fileOutput);

		System.out.println("downloading data...");
		InputStream input = new URL(URL).openStream();
		Files.copy(input, fileOutput, StandardCopyOption.REPLACE_EXISTING);
	}

}
