package de.waldbrand.app.website.icons.resize;

public class MaxVolumeResizer implements Resizer
{

	private int maxVolume;

	public MaxVolumeResizer(int maxVolume)
	{
		this.maxVolume = maxVolume;
	}

	@Override
	public Size resize(Size size)
	{
		double current = size.getWidth() * size.getHeight();
		double factor = Math.sqrt(maxVolume / current);
		double w = factor < 1 ? size.getWidth() * factor : size.getWidth();
		double h = factor < 1 ? size.getHeight() * factor : size.getHeight();
		return new Size(w, h);
	}

}
