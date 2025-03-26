package rim.component;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import rim.MainCanvas;
import rim.util.Gradient;

/**
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */
public class Footer extends ComponentUI{

    private int fontheight;
	private String lblLeft;
	private String lblRight;
    private int topfont;
    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param font
     */
    public Footer(int x, int y, int width, int height, Font font){
        super(x, y, width, height, font);
        fontheight = font.getHeight();
        topfont = (height-fontheight)/2;
	}
    /**
     *
     * @return
     */
    public int getHeight() {
		return fontheight + 2;
	}
    /**
     *
     * @param lblLeft
     */
    public void setLeftLabel(String lblLeft){
		this.lblLeft = lblLeft;
	}
    /**
     *
     * @param lblRight
     */
    public void setRightLabel(String lblRight){
		this.lblRight = lblRight;
	}
    /**
     *
     * @param g
     */
    protected void paintInternal(Graphics g) {
        //g.fillRect(0, 0, width, height);
        Gradient.gradientBox(g, 0xffffff, MainCanvas.COLOR_BAR, 0, 0, width, height/2, Gradient.VERTICAL);
        Gradient.gradientBox(g, MainCanvas.COLOR_BAR, 0x000000, 0, height/2, width, (height/2)+2, Gradient.VERTICAL);//000000
        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BORDER);
        g.drawLine(0, 0, width, 0);
        Font bold = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        g.setFont(bold);
        g.setColor(0xffffff);
        if (lblLeft != null)
			g.drawString(lblLeft, 3, topfont, Graphics.TOP|Graphics.LEFT);
		if (lblRight != null)
			g.drawString(lblRight, width-(font.stringWidth(lblRight) + 3), topfont, Graphics.TOP|Graphics.LEFT);
    }
}
