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

import java.nio.file.Path;

import de.waldbrand.app.website.config.MapPosition;
import lombok.Getter;
import lombok.Setter;

public class Config
{

	public static final Config INSTANCE = new Config();

	@Getter
	@Setter
	private String mapTilesPattern;
	@Getter
	@Setter
	private MapPosition defaultMapPosition;
	@Getter
	@Setter
	private Path fileWesData;
	@Getter
	@Setter
	private Path fileRettungspunkteData;
	@Getter
	@Setter
	private Path fileOsmData;
	@Getter
	@Setter
	private Path fileOsmWaynodes;
	@Getter
	@Setter
	private String nullName = "Unbekannt";

	@Getter
	@Setter
	private Path database;

	@Getter
	@Setter
	private String osmChaToken;

}
