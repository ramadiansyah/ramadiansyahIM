package rim.component;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import rim.MainCanvas;

/**
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */
public class FriendDetailsP extends ComponentUI {
    private int maxWidth;
    private int lineHeight;
    private int visibleLines;
    private int start = (-1);
    private Scrollable scrollable;
    private String footerlcml = null;
    private String footerrcml = "Close";
    private Footer footer;
    private Vector customMessageDetails;

    private String friend;
    private Image icon;
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
    public FriendDetailsP(int x, int y, int width, int height, Font font, Scrollable scrollable, Footer footer) {
        super(x, y, width, height, font);
        this.scrollable = scrollable;
        maxWidth = width - 4;
        this.footer= footer;
        lineHeight = font.getHeight() + 2;
        visibleLines = (height - 2) / lineHeight;
    }
    /**
     *
     * @return
     */
    public String getCurrentFooterLeft(){
        return footerlcml;
    }
    /**
     *
     * @return
     */
    public String getCurrentFooterRight(){
        return footerrcml;
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
        footer.setLeftLabel(footerlcml);
        footer.setRightLabel(footerrcml);
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
        if(this.customMessageDetails.size()==0)
            return;
        synchronized(this) {
            int end = start + visibleLines;
            if(end>customMessageDetails.size()) {
                end = customMessageDetails.size();
                start = end - visibleLines;
                if(start<0)
                    start = 0;
            }
            Font bold = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            for(int i=start; i<end; i++) {
                if(customMessageDetails.elementAt(i).equals("Friend ID : ")){
                    g.setFont(bold);
                    g.drawString((String)customMessageDetails.elementAt(i), 2, lineHeight * (i-start) + 2, Graphics.LEFT|Graphics.TOP);
                    g.setFont(font);
                }else if(customMessageDetails.elementAt(i).equals("Status : ")){
                    g.setFont(bold);
                    g.drawString((String)customMessageDetails.elementAt(i), 2, lineHeight * (i-start) + 2, Graphics.LEFT|Graphics.TOP);
                    if(icon!=null)
                        g.drawImage(icon, font.stringWidth((String)customMessageDetails.elementAt(i))+2, lineHeight * (i-start) + 2, Graphics.LEFT|Graphics.TOP);
                    g.setFont(font);
                }else if(customMessageDetails.elementAt(i).equals("Custom Message : ")){
                    g.setFont(bold);
                    g.drawString((String)customMessageDetails.elementAt(i), 2, lineHeight * (i-start) + 2, Graphics.LEFT|Graphics.TOP);
                    g.setFont(font);
                }else
                    g.drawString((String)customMessageDetails.elementAt(i), 2, lineHeight * (i-start) + 2, Graphics.LEFT|Graphics.TOP);
            }
        }
    }
    /**
     *
     * @param friend
     */
    public void setFriendID(String friend) {
        this.friend = friend;
    }
    /**
     *
     * @param icon
     */
    public void setFriendIcon(Image icon) {
        this.icon = icon;
    }
    /**
     *
     * @param statusMessage
     */
    public synchronized void setCustomMessageStatus(String statusMessage) {
        customMessageDetails = new Vector();
        customMessageDetails.addElement("Friend ID : ");
        customMessageDetails.addElement(this.friend);
        customMessageDetails.addElement("Status : ");
        customMessageDetails.addElement("Custom Message : ");
        if(statusMessage!=null){
            Vector v = splitStatusMessage(statusMessage);
            synchronized(this) {
                addAll(customMessageDetails, v);
                //Know and show the last line
                if(start==(-1))
                    start = 0;
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
        if(start<customMessageDetails.size()-1) {
            start++;
            return true;
        }
        return false;
    }
    private int getScrollMax() {
        //ketamabahan ??? custom baris
        int n = (customMessageDetails.size() + 4) - visibleLines + 1;
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
        for(int i=0; i<src.size(); i++)
            dest.addElement(src.elementAt(i));
    }
    //split string and return result in vector 
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
    private Vector splitStatusMessage(String text) {
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
