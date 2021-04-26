package de.waldbrand.app.website.icons.resize;

public class MaxSidesResizer implements Resizer
{

	private int maxSize;

	public MaxSidesResizer(int maxSize)
	{
		this.maxSize = maxSize;
	}

	@Override
	public Size resize(Size size)
	{
		double max = Math.max(size.getWidth(), size.getHeight());
		double factor = maxSize / max;
		double w = factor < 1 ? size.getWidth() * factor : size.getWidth();
		double h = factor < 1 ? size.getHeight() * factor : size.getHeight();
		return new Size(w, h);
	}

}
