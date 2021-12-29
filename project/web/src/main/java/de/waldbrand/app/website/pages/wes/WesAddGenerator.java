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

package de.waldbrand.app.website.pages.wes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import org.jsoup.nodes.DataNode;

import de.topobyte.jsoup.HTML;
import de.topobyte.jsoup.bootstrap4.BootstrapForms;
import de.topobyte.jsoup.bootstrap4.forms.InputGroup;
import de.topobyte.jsoup.bootstrap4.forms.SelectGroup;
import de.topobyte.jsoup.components.Div;
import de.topobyte.jsoup.components.Form;
import de.topobyte.jsoup.components.Form.Method;
import de.topobyte.jsoup.components.Head;
import de.topobyte.jsoup.components.P;
import de.topobyte.jsoup.components.Script;
import de.topobyte.melon.commons.io.Resources;
import de.topobyte.webpaths.WebPath;
import de.waldbrand.app.website.Website;
import de.waldbrand.app.website.lbforst.NameUtil;
import de.waldbrand.app.website.lbforst.model.WesPoi;
import de.waldbrand.app.website.pages.base.SimpleBaseGenerator;
import de.waldbrand.app.website.util.MapUtil;
import de.waldbrand.app.website.util.MarkerShape;

public class WesAddGenerator extends SimpleBaseGenerator
{

	public WesAddGenerator(WebPath path)
	{
		super(path);
	}

	@Override
	protected void content() throws IOException
	{
		Head head = builder.getHead();
		MapUtil.head(head);

		content.ac(HTML.h2("Wasserentnahmestelle hinzufügen"));

		Div row = content.ac(HTML.div("row"));

		Div left = row.ac(HTML.div("col-12 col-md-6"));
		Div right = row.ac(HTML.div("col-12 col-md-6"));

		// left hand side

		P p = left.ac(HTML.p());
		p.at(
				"Bitte wähle hier durch Verschieben der Karte den Standort aus:");

		MapUtil.addMap(left);

		MapUtil.addMarkerDef(left, MarkerShape.CIRCLE, "red", "fa", "fa-tint");

		Script script = left.ac(HTML.script());
		StringBuilder code = new StringBuilder();

		MapUtil.marker(code);
		script.ac(new DataNode(code.toString()));

		script = left.ac(HTML.script());
		script.ac(new DataNode(Resources.loadString("js/map-pick.js")));

		// right hand side

		p = right.ac(HTML.p());
		p.at("Bitte ergänze hier die Infos zu Wasserentnahmestelle:");

		BootstrapForms forms = new BootstrapForms();

		Form form = right.ac(HTML.form());
		form.setMethod(Method.POST);
		form.setAction("eintragen-submit");
		form.addClass("form-horizontal");

		selectOart(forms, form);
		selectFStatus(forms, form);
		selectFktFaehig(forms, form);
		selectMenge(forms, form);

		InputGroup inputBaujahr = forms.addInput(form, "baujahr", "Baujahr");
		inputBaujahr.getInput().setPlaceholder("2021");

		InputGroup inputKommentar = forms.addInput(form, "kommentar",
				"Kommentar");
		inputKommentar.getInput().setPlaceholder("Dein Kommentar");

		forms.addSubmit(form, "speichern");
	}

	private SelectGroup selectOart(BootstrapForms forms, Form form)
	{
		List<String> names = new ArrayList<>();
		List<String> values = new ArrayList<>();
		for (int oart : NameUtil.getOarts()) {
			names.add(String.format("%s (%d)", NameUtil.typeName(oart), oart));
			values.add(Integer.toString(oart));
		}
		return forms.addSelect(form, "oart", "oart", names, values);
	}

	private SelectGroup selectFStatus(BootstrapForms forms, Form form)
	{
		List<String> names = new ArrayList<>();
		List<String> values = new ArrayList<>();
		addValues(names, values, WesPoi::getFstatus);
		return forms.addSelect(form, "fstatus", "fstatus", names, values);
	}

	private SelectGroup selectFktFaehig(BootstrapForms forms, Form form)
	{
		List<String> names = new ArrayList<>();
		List<String> values = new ArrayList<>();
		addValues(names, values, WesPoi::getFktFaehig);
		return forms.addSelect(form, "fkt_faehig", "fkt_faehig", names, values);
	}

	private SelectGroup selectMenge(BootstrapForms forms, Form form)
	{
		List<String> names = new ArrayList<>();
		List<String> values = new ArrayList<>();
		addValues(names, values, WesPoi::getMenge);
		return forms.addSelect(form, "menge", "Menge", names, values);
	}

	private void addValues(List<String> names, List<String> values,
			Function<WesPoi, Integer> dataGetter)
	{
		Set<Integer> possible = new TreeSet<>();
		for (WesPoi poi : Website.INSTANCE.getData().getWesPois()) {
			possible.add(dataGetter.apply(poi));
		}

		for (int oart : possible) {
			names.add(Integer.toString(oart));
			values.add(Integer.toString(oart));
		}
	}

}
