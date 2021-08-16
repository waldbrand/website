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

package de.waldbrand.app.website.resolving;

import de.topobyte.webgun.resolving.smart.SmartPathSpecResolver;
import de.waldbrand.app.website.ApiEndpoint;
import de.waldbrand.app.website.api.RettungspunktePoisGenerator;
import de.waldbrand.app.website.api.WesPoisGenerator;
import de.waldbrand.app.website.links.LinkDefs;

public class ApiPathResolver extends SmartPathSpecResolver<ApiEndpoint, Void>
{

	{
		map(LinkDefs.API_POIS_WES,
				(path, output, request, data) -> new WesPoisGenerator(request));
		map(LinkDefs.API_POIS_RETTUNGSPUNKTE, (path, output, request,
				data) -> new RettungspunktePoisGenerator(request));
	}

}
