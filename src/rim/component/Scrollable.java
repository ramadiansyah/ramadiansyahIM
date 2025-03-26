package rim.component;

import javax.microedition.lcdui.Graphics;

/**
 * Interface for ScrollBar.
 */
public interface Scrollable {

    /**
     * When scroll range or current value changed, invoke this method to notify scroll bar.
     * @param max If max is 0, value will be ignored.
     * @param value Value will automatically set between 0 to max.
     */
    void setScroll(int max, int value);
    /**
     *
     * @param g
     */
    void paint(Graphics g);
}
