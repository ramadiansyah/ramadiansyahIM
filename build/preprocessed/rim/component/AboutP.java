package rim.component;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import rim.MainCanvas;

/**
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */
public class AboutP extends ComponentUI {
    private int maxWidth;
    private int lineHeight;
    private int visibleLines;
    private int start = (-1);
    private Scrollable scrollable;
    private String footerLeftCmdAP = null;
    private String footerRightCmdAP = "Back";
    private Footer customFooter;
    private Vector vAbout;
    
    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param font
     * @param scrollable
     * @param footer
     */
    public AboutP(int x, int y, int width, int height, Font font, Scrollable scrollable, Footer footer) {
        super(x, y, width, height, font);
        this.scrollable = scrollable;
        maxWidth = width - 4;
        this.customFooter= footer;
        lineHeight = font.getHeight() + 2;
        visibleLines = (height - 2) / lineHeight;
        vAbout = new Vector();
        vAbout.addElement("ramadiansyahIM-Alpha1");
        vAbout.addElement("Copyright(c)2009");
        Vector v = splitLongString("Rizki Eka Saputra Ramadiansyah");
        addAll(vAbout, v);
        vAbout.addElement("All right reserved");
        vAbout.addElement("http://ramadiansyah.net");
        vAbout.addElement("");
        v.removeAllElements();
        v = splitLongString("ramadiansyahIM is not a spyware. It is free to install and use. Your service provider may charge for network traffic.");
        addAll(vAbout, v);
        vAbout.addElement("");
        vAbout.addElement("Use at your own risk.");
        vAbout.addElement("");
        vAbout.addElement("ramadiansyahIM uses jYMSG API by FISH.");
        vAbout.addElement("");
        //The platform detection please
    }
    /**
     *
     * @return
     */
    public String getCurrentFooterLeft(){
        return footerLeftCmdAP;
    }
    /**
     *
     * @return
     */
    public String getCurrentFooterRight(){
        return footerRightCmdAP;
    }
    /**
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        // update scrollable:
        scrollable.setScroll(getScrollMax(), start);
        scrollable.paint(g);
        customFooter.setVisible(true);
        customFooter.setLeftLabel(footerLeftCmdAP);
        customFooter.setRightLabel(footerRightCmdAP);
        customFooter.paint(g);
    }
    /**
     *
     * @param g
     */
    protected void paintInternal(Graphics g) {
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        g.setColor(MainCanvas.COLOR_FOREGROUND);
        if(start==(-1))
            start = 0;
        if(this.vAbout.size()==0)
            return;
        synchronized(this) {
            int end = start + visibleLines;
            if(end>vAbout.size()) {
                end = vAbout.size();
                start = end - visibleLines;
                if(start<0)
                    start = 0;
            }
            for(int i=start; i<end; i++) {
                g.drawString((String)vAbout.elementAt(i), 2, lineHeight * (i-start) + 2, Graphics.LEFT|Graphics.TOP);
            }
        }
    }
    /**
     *
     * @return
     */
    public synchronized boolean moveUp() {
        if(start>0) {
            start--;
            return true;
        }
        return false;
    }
    /**
     *
     * @return
     */
    public synchronized boolean moveDown() {
        if(start<vAbout.size()-1) {
            start++;
            return true;
        }
        return false;
    }
    private int getScrollMax() {
        int n = vAbout.size() - visibleLines + 1;
        return (n<0 ? 0 : n);
    }
    /**
     *
     * @return
     */
    public boolean shouldScrollDown() {
        if(getScrollMax()==0)
            return true;
        return getScrollMax()==(start+1);
    }
    private void addAll(Vector dest, Vector src) {
        if(dest==null)
            System.out.println("dest null");
        else if(src==null)
            System.out.println("dest null");

        for(int i=0; i<src.size(); i++)
            dest.addElement(src.elementAt(i));
    }
    /*split string and return result in vector */
    private Vector splitLongString(String text) {
        Vector v = new Vector();
        split(text, v);
        return v;
    }
    private void split(String text, Vector toAdd) {
        //System.out.println("-- split: " + text);
        String leftPart = left(text, maxWidth);
        if(leftPart==null) {
            //System.out.println(">>" + text);
            //System.out.println("Done!");
            toAdd.addElement(text);
            return;
        }
        // add left:
        //System.out.println(">>" + leftPart);
        toAdd.addElement(leftPart);
        // call recursively:
        String rightPart = text.substring(leftPart.length()).trim();
        if(!rightPart.equals(""))
            split(rightPart, toAdd);
    }
    private Vector splitTheString(String text) {
        // split to lines by '\n':
        int start = 0;
        text = text.replace('\r', '\n');
        Vector lines = new Vector();
        for(;;) {
            int n = text.indexOf('\n', start);
            if(n==(-1)) {
                // last line:
                String s = text.substring(start).trim();
                if(!s.equals("")) {
                    //System.out.println("LINE>>" + s);
                    lines.addElement(text.substring(start));
                }
                break;
            }
            if(n==0) {
                // empty line!
                start++;
                continue;
            }
            String s = text.substring(start, n).trim();
            if(!s.equals("")) {
                //System.out.println("LINE>>" + s);
                lines.addElement(s);
            }
            start = n + 1;
        }
        // now append each lines:
        Vector moreLines = new Vector();
        for(int i=0; i<lines.size(); i++) {
            Vector v = splitLongString((String)lines.elementAt(i));
            addAll(moreLines, v);
        }
        return moreLines;
    }
}
