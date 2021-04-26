package de.waldbrand.app.website.icons.resize;

public class MaxCircumferenceResizer implements Resizer
{

	private int maxSize;

	public MaxCircumferenceResizer(int maxSize)
	{
		this.maxSize = maxSize;
	}

	@Override
	public Size resize(Size size)
	{
		double current = 2 * (size.getWidth() + size.getHeight());
		double factor = maxSize / current;
		double w = factor < 1 ? size.getWidth() * factor : size.getWidth();
		double h = factor < 1 ? size.getHeight() * factor : size.getHeight();
		return new Size(w, h);
	}

}
