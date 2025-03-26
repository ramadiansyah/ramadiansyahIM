package rim;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStoreException;
import rim.util.Preferences;

/**
 * Main MIDlet.
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */
public class RamadiansyahIM extends MIDlet {

    /**
     *
     */
    public static RamadiansyahIM instance = null;

    static class getDisplay {
        public getDisplay() {
        }
    }

    private Display display = null;
    private Displayable current = null;

    /**
     *
     */
    public RamadiansyahIM() {
        instance = this;
    }

    /**
     *
     * @param unconditional
     */
    protected void destroyApp(boolean unconditional) {
    }

    /**
     *
     */
    protected void pauseApp() {
    }

    /**
     *
     */
    protected void startApp() {
        if(display==null) {
            display = Display.getDisplay(this);
            current = new SplashCanvas();
        }
        display.setCurrent(current);
    }

    /**
     *
     */
    public static void quitApp() {
        instance.destroyApp(true);
        instance.notifyDestroyed();
    }

    /**
     * Convenient method to switch ui.
     * @param d
     */
    public static void switchUI(Displayable d) {
        instance.current = d;
        instance.display.setCurrent(d);
    }

    /**
     * Convenient method to get a Display instance.
     * @return
     */
    public static Display getDisplay() {
        return instance.display;
    }

    

}

