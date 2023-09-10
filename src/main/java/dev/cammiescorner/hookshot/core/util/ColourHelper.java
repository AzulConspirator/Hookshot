package dev.cammiescorner.hookshot.core.util;

public class ColourHelper {
	public static int rgbToDecimal(float[] rgb) {
		return (((int) (rgb[0] * 255F) << 16) | ((int) (rgb[1] * 255F) << 8) | (int) (rgb[2] * 255F));
	}
}
