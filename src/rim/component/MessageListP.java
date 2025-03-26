package rim.component;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import rim.MainCanvas;
import rim.util.MessageVector;

/**
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */

public class MessageListP extends ComponentUI {

    private int maxWidth;
    private int lineHeight;
    private int visibleLines;
    private Vector allMessages;
    private int start = (-1);
    private Scrollable scrollable;
    private MessageVector mvID;

    int leftPopUpSelector=0;
    private String[] contentLeftPopUp = {"Send Message",
                             "BUZZ!!!",
                             "Details",
                             "Recent Chat",
                             "FriendList",
                             "Close",
                             };
    private String[] footerLeftCmd = {"Menu","OK"};
    private String[] footerRightCmd = {"Hide","Cancel"};

    /*
     * Shortcut that must be implement
     - Messagelist Shortcut
		- 5/Fire : Send Message
		- 7 : Details
		- * /# Move one to another MessageListP

     */
    
    private Footer footer;
    private PopUpMenu leftPopUp;
    

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
    public MessageListP(int x, int y, int width, int height, Font font, Scrollable scrollable, Footer footer) {
        super(x, y, width, height, font);
        allMessages = new Vector();
        this.scrollable = scrollable;
        maxWidth = width - 4;
        this.footer= footer;
        lineHeight = font.getHeight() + 2;
        visibleLines = (height - 2) / lineHeight;
        leftPopUp = new PopUpMenu(x,y,width,height,font);
        leftPopUp.setContent(contentLeftPopUp);
        this.footer.setLeftLabel(footerLeftCmd[0]);
        this.footer.setRightLabel(footerRightCmd[0]);
    }

    /**
     *
     */
    public void clearAllMessages() {
        allMessages.removeAllElements();
    }

    /**
     *
     * @return
     */
    public String getSelectedMenu() {
        return leftPopUp.getSelectedMenu();
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
        if(!leftPopUp.getVisible()){
            footer.setLeftLabel(footerLeftCmd[0]);
            footer.setRightLabel(footerRightCmd[0]);
            this.leftPopUp.setVisible(false);
        }else if(leftPopUp.getVisible()){
            footer.setLeftLabel(footerLeftCmd[1]);
            footer.setRightLabel(footerRightCmd[1]);
            leftPopUp.setVisible(true);
            leftPopUp.paint(g);
        }
        footer.setVisible(true);
        footer.paint(g);
    }

    /**
     *
     * @param b
     */
    public void setStateCurrentMVID(boolean b) {
        this.mvID.setOpened(b);
    }

    //paint the message
    /**
     *
     * @param g
     */
    protected void paintInternal(Graphics g) {
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        // draw each message:
        if(this.mvID.size()==0)
            return;
        g.setColor(MainCanvas.COLOR_FOREGROUND);
        if(start==(-1))
            start = 0;
        synchronized(this) {
            int end = start + visibleLines;
            if(end>mvID.size()) {
                end = mvID.size();
                start = end - visibleLines;
                if(start<0)
                    start = 0;
            }
            for(int i=start; i<end; i++) 
                g.drawString((String)mvID.elementAt(i), 2, lineHeight * (i-start) + 2, Graphics.LEFT|Graphics.TOP);
        }
        removeUserMessage(mvID.getId());
        allMessages.addElement(mvID);
    }

    //Load And Add
    /**
     * @param yahooID
     * @param message
     */
     public synchronized void addReceivedMessage(String yahooID, String message) {
        Vector v = splitMessage(message);
        if(!searchUserMessage(yahooID)){
            mvID = new MessageVector(yahooID);//
        }
        synchronized(this) {
            if(!yahooID.equals(mvID.getLastEmail())) {
                Vector lines = splitLongString(yahooID + ":");
                addAll(mvID, lines);
                mvID.setLastEmail(yahooID);
            }
            addAll(mvID, v);
            mvID.removeOld();
            if(start==(-1))
                start = 0;
        }
        mvID.setOpened(false);
        removeUserMessage(mvID.getId());
        allMessages.addElement(mvID);
    }

    /**
     *
     * @param yahooID
     * @param message
     */
    /*Load and Add ; siapapun yang menggunakan mvId harus di add kan kemabali ke*/
    public synchronized void addSentMessage(String name, String text) {
        Vector v = splitMessage(text);
        if(this.searchUserMessage(name)==false){
            this.mvID = new MessageVector(name);
        }
        String t = "You :";
        synchronized(this) {
            if(!t.equals(mvID.getLastEmail())) {
                Vector lines = splitLongString(t);
                addAll(mvID, lines);
                mvID.setLastEmail(t);
            }
            addAll(mvID, v);
            mvID.removeOld();
            //Know and show the last line
            if(start==(-1))
                start = 0;
        }
        removeUserMessage(mvID.getId());
        allMessages.addElement(mvID);
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
        if(start<mvID.size()-1) {
            start++;
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    public boolean moveUpLC() {
        return leftPopUp.moveUp();
    }

    /**
     *
     * @return
     */
    public boolean moveDownLC() {
        return leftPopUp.moveDown();
    }
    private int getScrollMax() {
        int n = mvID.size() - visibleLines + 1;
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

    /*split string contain \n like messaage this contain splitLongString*/
    private Vector splitMessage(String text) {
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

    //split string and return result in vector
    private Vector splitLongString(String text) {
        Vector v = new Vector();
        split(text, v);
        return v;
    }

    private void split(String text, Vector toAdd) {
        //System.out.println("-- split: " + message);
        String leftPart = left(text, maxWidth);
        if(leftPart==null) {
            //System.out.println(">>" + message);
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

    /**
     *
     * @param id
     * @return
     */
    public boolean searchOnlyUserMessage(String id) {
        for(int i=0; i<allMessages.size();i++){
            if(((MessageVector)allMessages.elementAt(i)).getId().equals(id)){
                this.mvID = (MessageVector)allMessages.elementAt(i);
                mvID.setOpened(true);
                return true;
            }
        }
        return false;
    }
    /**
     *
     * @param yahooID
     * @return
     */
    public boolean searchUserMessage(String yahooID){
        for(int i=0; i<allMessages.size();i++){
            if(((MessageVector)allMessages.elementAt(i)).getId().equals(yahooID)){
                this.mvID = (MessageVector)allMessages.elementAt(i);
                allMessages.removeElementAt(i);
                return true;
            }
        }
        return false;
    }
    
    public void removeUserMessage(String yahooID){
        for(int i=0; i<allMessages.size();i++){
            if(((MessageVector)allMessages.elementAt(i)).getId().equals(yahooID)){
                this.mvID = (MessageVector)allMessages.elementAt(i);
                allMessages.removeElementAt(i);
            }
        }
    }
    /**
     *
     * @param yahooID
     */
    public void deleteMessage(String yahooID) {
        for(int i=0; i<allMessages.size();i++){
            if(((MessageVector)allMessages.elementAt(i)).getId().equals(yahooID)){
                allMessages.removeElementAt(i);
                return ;
            }
        }
   }
    /**
     *
     * @param id
     * @param message
     */
    public void addMessageToMVID(String id, String message){
        boolean found=false;
        MessageVector tmpMvId = null;
        for(int i=0; i<allMessages.size();i++){
            if(((MessageVector)allMessages.elementAt(i)).getId().equals(id)){
                tmpMvId = (MessageVector)allMessages.elementAt(i);
                allMessages.removeElementAt(i);
                found = true;
                break;
            }
        }
        Vector v = splitMessage(message);
        if(!found){
            tmpMvId = new MessageVector(id);
        }
        synchronized(this) {
            if(!id.equals(tmpMvId.getLastEmail())) {
                Vector lines = splitLongString(id + ":");
                addAll(tmpMvId, lines);
                tmpMvId.setLastEmail(id);
            }
            addAll(tmpMvId, v);
            tmpMvId.removeOld();
            if(start==(-1))
                start = 0;
        }
        tmpMvId.setOpened(false);
        removeUserMessage(mvID.getId());
        allMessages.addElement(tmpMvId);
   }

    /**
     *
     * @return
     */
    public Vector getRecentMessageUser(){
        return allMessages;
   }
   /**
    *
    * @param yahooID
    */
   public void createUserMessage(String yahooID){
        this.mvID = new MessageVector(yahooID);
   }
   /**
    *
    * @return
    */
   public String getMVIDUser(){
        return mvID.getId();
   }
   /**
    *
    * @return
    */
   public boolean getVisibleLC(){
       return leftPopUp.getVisible();
    }
   /**
    *
    * @param showLeftPopUp
    * @return
    */
   public boolean setVisibleLCMessageList(boolean showLeftPopUp){
       leftPopUp.setVisible(showLeftPopUp);
       return true;
    }
   /**
    *
    * @return
    */
   public String getCurrentFooterLeft(){
         if(!leftPopUp.getVisible())
            return footerLeftCmd[0];
        else
            return footerLeftCmd[1];
    }
   /**
    *
    * @return
    */
   public String getCurrentFooterRight(){
        if(!leftPopUp.getVisible())
            return footerRightCmd[0];
        else
            return footerRightCmd[1];
    }

    /**
     *
     */
    public void setPopUpSelectedToDefault(){
        this.leftPopUp.setSelectedToDefaultIndex();
    }

    /**
     *
     */
    public void unshowPopUp(){
        leftPopUp.setVisible(false);
    }


}
