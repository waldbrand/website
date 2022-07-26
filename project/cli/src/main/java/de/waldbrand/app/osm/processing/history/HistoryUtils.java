// Copyright 2022 Sebastian Kuerten
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

package de.waldbrand.app.osm.processing.history;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.iface.OsmMetadata;

public class HistoryUtils
{

	public static OsmEntity findLatestVersionUntil(List<OsmEntity> versions,
			LocalDate d)
	{
		for (int i = versions.size() - 1; i >= 0; i--) {
			OsmEntity entity = versions.get(i);
			OsmMetadata metadata = entity.getMetadata();
			LocalDate date = Instant.ofEpochMilli(metadata.getTimestamp())
					.atOffset(ZoneOffset.UTC).toLocalDate();
			if (date.isBefore(d)) {
				return entity;
			}
		}
		return null;
	}

}
