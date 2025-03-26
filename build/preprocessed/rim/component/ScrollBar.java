package rim.component;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import rim.MainCanvas;


/**
 * Paint a scroll bar.
 */
public class ScrollBar extends ComponentUI implements Scrollable {

    private static final int MIN_BAR_HEIGHT = 0;

    //private Image up;
    //private Image down;
    private int value;
    private int max;
    private int inner_height;

    /**
     *
     * @param x
     * @param y
     * @param height
     * @param font
     */
    public ScrollBar(int x, int y, int height, Font font) {
        super(x, y, 15, height, font);
        inner_height = height;
        setScroll(10, 0);
    }

    public void setScroll(int max, int value) {
        if(max<0)
            max = 0;
        this.max = max;
        if(value<0)
            value = 0;
        if(value>=max)
            value = max-1;
        this.value = value;
    }

    /**
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @param g
     */
    protected void paintInternal(Graphics g) {
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        g.setColor(MainCanvas.COLOR_BORDER);
        g.drawRect(0, 0, width-1, height-1);
        // paint bar:
        if(max==0)
            return;
        int bar_height = inner_height / max;
        if(bar_height<MIN_BAR_HEIGHT)
            bar_height = MIN_BAR_HEIGHT;
        // now calculate position:
        int pos = (max==1) ? (inner_height-bar_height) : ((inner_height-bar_height) * value / (max-1));
        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BORDER);
        g.fillRect(2, pos, width-4, bar_height-2);
    }

}
