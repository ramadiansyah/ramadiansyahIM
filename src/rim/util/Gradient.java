/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rim.util;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author 7406030021
 */
public class Gradient{
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;

	public static void gradientBox(Graphics g, int color1, int color2, int left, int top, int width, int height, int orientation)
	{
		int max = orientation == VERTICAL ? height : width;

		for(int i = 0; i < max; i++)
		{
			int color = midColor(color1, color2, max * (max - 1 - i) / (max - 1), max);

			g.setColor(color);

			if(orientation == VERTICAL)
				g.drawLine(left, top + i, left + width - 1, top + i);
			else
				g.drawLine(left + i, top, left + i, top + height - 1);
		}
	}

	static int midColor(int color1, int color2, int prop, int max){
		int red =
			(((color1 >> 16) & 0xff) * prop +
			((color2 >> 16) & 0xff) * (max - prop)) / max;

		int green =
			(((color1 >> 8) & 0xff) * prop +
			((color2 >> 8) & 0xff) * (max - prop)) / max;

		int blue =
			(((color1 >> 0) & 0xff) * prop +
			((color2 >> 0) & 0xff) * (max - prop)) / max;

		int color = red << 16 | green << 8 | blue;

		return color;
	}
}
