package rim.component;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import rim.MainCanvas;

/**
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */
public class HelpP extends ComponentUI {
    private int maxWidth;
    private int lineHeight;
    private int visibleLines;
    private int start = (-1);
    private Scrollable scrollable;
    private String footerLeftCmd = null;
    private String footerRightCmd = "Close";
    private Footer footer;
    private Vector vHelp;
    private int topFont, topImg;
    private Image[] imgStatus;
    
    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param font
     * @param scrollable
     * @param footer
     * @param imgStatus
     */
    public HelpP(int x, int y, int width, int height, Font font, Scrollable scrollable, Footer footer, Image[] imgStatus) {
        super(x, y, width, height, font);
        this.scrollable = scrollable;
        maxWidth = width - 4;
        this.footer= footer;
        lineHeight = font.getHeight() + 2;
        visibleLines = (height - 2) / lineHeight;

        int usepixels= (height-(visibleLines*lineHeight));
        topFont = usepixels/2;
        topImg = topFont+(lineHeight/2);

        this.imgStatus = imgStatus;
        vHelp = new Vector();
        /*Vector v = splitLongString("To scroll up and down, and to select items, press joystick up and down (2 and 8). To scroll and entire screen height up or down, press the joystick left or right (4 or 8). To show selected friend's status, press 7. Press * or # to switch among opened IM windows.");
        addAll(vHelp, v);
        vHelp.addElement("");
        v.removeAllElements();
        v = splitLongString("Key on the friend list window:");
        addAll(vHelp, v);
        v.removeAllElements();
        v = splitLongString("To open IM window for the selected friend, push the joystick or press 5. To show or hide offline friends. press 0.");
        addAll(vHelp, v);
        vHelp.addElement("");
        v.removeAllElements();
        v = splitLongString("Key on the IM window:");
        addAll(vHelp, v);
        v.removeAllElements();
        v = splitLongString("To send a new message, push the joystick or press 5.");
        addAll(vHelp, v);
        vHelp.addElement("");*/
        
        vHelp.addElement("Status icons:");
        vHelp.addElement("Online");
        vHelp.addElement("Offline/Invisible");
        vHelp.addElement("Busy");
        vHelp.addElement("Idle");
        vHelp.addElement("New IM message");

    }
    /**
     *
     * @return
     */
    public String getCurrentFooterLeft(){
        return footerLeftCmd;
    }
    /**
     *
     * @return
     */
    public String getCurrentFooterRight(){
        return footerRightCmd;
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
        footer.setVisible(true);
        footer.setLeftLabel(footerLeftCmd);
        footer.setRightLabel(footerRightCmd);
        footer.paint(g);
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
        if(this.vHelp.size()==0)
            return;
        synchronized(this) {
            int end = start + visibleLines;
            if(end>vHelp.size()) {
                end = vHelp.size();
                start = end - visibleLines;
                if(start<0)
                    start = 0;
            }
            for(int i=start; i<end; i++) {
                if(vHelp.elementAt(i).equals("Online")){
                    g.drawString((String)vHelp.elementAt(i), 16, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                    g.drawImage(imgStatus[0], 2, lineHeight*(i-start)+topImg, Graphics.LEFT|Graphics.VCENTER);
                }else if(vHelp.elementAt(i).equals("Offline/Invisible")){
                    g.drawString((String)vHelp.elementAt(i), 16, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                    g.drawImage(imgStatus[1], 2, lineHeight*(i-start)+topImg, Graphics.LEFT|Graphics.VCENTER);
                }else if(vHelp.elementAt(i).equals("Busy")){
                    g.drawString((String)vHelp.elementAt(i), 16, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                    g.drawImage(imgStatus[2], 2, lineHeight*(i-start)+topImg, Graphics.LEFT|Graphics.VCENTER);
                }else if(vHelp.elementAt(i).equals("Idle")){
                    g.drawString((String)vHelp.elementAt(i), 16, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                    g.drawImage(imgStatus[3], 2, lineHeight*(i-start)+topImg, Graphics.LEFT|Graphics.VCENTER);
                }else if(vHelp.elementAt(i).equals("New IM message")){
                    g.drawString((String)vHelp.elementAt(i), 16, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                    g.drawImage(imgStatus[5], 2, lineHeight*(i-start)+topImg, Graphics.LEFT|Graphics.VCENTER);
                }else
                    g.drawString((String)vHelp.elementAt(i), 2, lineHeight * (i-start)+topFont, Graphics.LEFT|Graphics.TOP);
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
        if(start<vHelp.size()-1) {
            start++;
            return true;
        }
        return false;
    }
    private int getScrollMax() {
        int n = vHelp.size() - visibleLines + 1;
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
}
