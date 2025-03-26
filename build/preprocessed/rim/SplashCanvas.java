package rim;

import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Welcome screen that display a logo.
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */
public class SplashCanvas extends Canvas implements Runnable {

    private Image logo = null;
    private Thread waitingThread;

    /**
     *
     */
    public SplashCanvas() {
        try {
            logo = Image.createImage("/res/logo.jpg");
        }
        catch(IOException e) {
            //throw new Error("Failed loading resource.");
        }
        setFullScreenMode(true);
        waitingThread = new Thread(this);
        waitingThread.start();
    }

    /**
     *
     * @param g
     */
    protected void paint(Graphics g) {
        setFullScreenMode(true);
        int w = getWidth();
        int h = getHeight();
        g.setColor(0xffffff);
        g.fillRect(0, 0, w, h);
        g.drawImage(logo, w/2, h/2, Graphics.HCENTER|Graphics.VCENTER);
    }

    /**
     *
     */
    public void run() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            //ex.printStackTrace();
        }
        RamadiansyahIM.switchUI(new MainCanvas());
    }

    /**
     *
     * @param keyCode
     */
    protected void keyPressed(int keyCode) {
        //waitingThread.interrupt();
        //remove this and check the S40
    }

    /**
     *
     * @param x
     * @param y
     */
    protected void pointerPressed(int x, int y) {
        //waitingThread.interrupt();
    }
}
