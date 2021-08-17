package de.waldbrand.app.mapfile;

import de.topobyte.mapocado.styles.classes.element.ObjectClassRef;
import lombok.Getter;

public class WaldbrandMapfile
{

	public final static String RETTUNGSPUNKT = "rettungspunkt";
	public final static String RETTUNGSPUNKT_ID = "rettungspunkt-id";

	@Getter
	private ObjectClassRef classRettungspunkte;

	public WaldbrandMapfile(ObjectClassRef classRettungspunkte)
	{
		this.classRettungspunkte = classRettungspunkte;
	}

}
