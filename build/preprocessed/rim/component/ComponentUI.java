package rim.component;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * Super class for all low-level components paint on Canvas.
 */
public abstract class ComponentUI {

    /**
     *
     */
    public final int x;
    /**
     *
     */
    public final int y;
    /**
     *
     */
    public final int width;
    /**
     *
     */
    public final int height;
    /**
     *
     */
    public final Font font;
    private static int THREE_DOTS_WIDTH = 0;

    private boolean visible = false;

    //private int store_x;
    //private int store_y;

    /**
     * Create UI component at point (x, y) and specify width and height.
     * @param x 
     * @param y
     * @param width
     * @param height
     * @param font
     */
    public ComponentUI(int x, int y, int width, int height, Font font) {
            this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.font = font;
        if(THREE_DOTS_WIDTH==0)
            THREE_DOTS_WIDTH = font.charWidth('.') * 3;
    }

    /**
     *
     * @param g
     */
    public void paint(Graphics g) {
        if(visible) {
            g.translate(x-g.getTranslateX(), y-g.getTranslateY());
            paintInternal(g);
        }
        }

    /**
     *
     * @param g
     */
    protected abstract void paintInternal(Graphics g);

    /**
     *
     * @return
     */
    public final boolean getVisible() {
        return visible;
    }

    /**
     *
     * @param visible
     */
    public final void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     *
     * @param s
     * @param max
     * @return
     */
    protected String left(String s, int max) {
        int s_width = font.stringWidth(s);
        if(s_width<=max)
            return null;
        char[] cs = s.toCharArray();
        int estimate = cs.length;
        int ax = 0;
        do {
            estimate = estimate / 2;
            ax = font.charsWidth(cs, 0, estimate);
        }
        while(ax>max);
        // now [0-estimate) is less than s_max:
        for(int i=estimate; i<cs.length; i++) {
            ax += font.charWidth(cs[i]);
            if(ax>max) {
                // found!
                return new String(cs, 0, i-1);
            }
        }
        // there must be logic error in this method:
        return "?";
    }

}
