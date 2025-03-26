package rim.component;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import rim.MainCanvas;
import rim.util.MessageVector;
import rim.util.QuickSort;
import ymsg.network.Session;
import ymsg.network.YahooUser;

/**
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */

public class FriendListP extends ComponentUI {
    private Session ss;
    private Image[] imgStatus;
    private int lineHeight;
    private int visibleLines;
    private int textWidth;
    private int selection = 0;
    private Scrollable scrollable;
    Vector vYahooUser ;
    private boolean bHideOffline = false;
    //Friendlist command
    private String[] contentLeftPopUp = {"IM",
                             "IM Other...",
                             "Recent Chat",
                             "Show/Hide Offline",
                             "Change Status",
                             "Details",
                             "Add Friend",
                             "Remove Friend",
                             "Settings",
                             "Logout",
                             "Exit"};
    private String[] footerLeftCmd = {"Menu","OK"};
    private String[] footerRightCmd = {null,"Cancel"};
    //boolean showLeftPopUp=false;
    /*- Friendlist Shortcut
		- 5/Fire : Go/Make Friend's MessageList
		- 7 : Friend Details
		- 8 : Show of Hide Offline
		- 9 : Recent FriendListP : ALL Status
		- * /# Move one to another MessageList*/
    private Footer footer;//komponen custom footer
    private PopUpMenu leftPopUp;
    private int topfont;//, topImg;

    private Vector vRecentUsers = new Vector();
    private Hashtable mHashtable, htStatus, htFriendDetails;
    private String countNewMail;
    private boolean newMailReceived;

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param font
     * @param scrollable
     * @param imgStatus
     * @param footer
     */
    public FriendListP(int x, int y, int width, int height, Font font, Scrollable scrollable,Image[] imgStatus, Footer footer) {
        super(x, y, width, height, font);
        this.scrollable = scrollable;
        this.imgStatus = imgStatus;
        this.textWidth = width - 14;
        this.footer = footer;

        lineHeight = font.getHeight()+2;
        if(lineHeight<15){
            lineHeight=15;
        }
        visibleLines = (height-2) / lineHeight;
        int usepixels= (height-(visibleLines*lineHeight));
        topfont = usepixels/2;
        //topImg = topfont+(lineHeight/2);
        leftPopUp = new PopUpMenu(x,y,width,height,font);
        leftPopUp.setContent(contentLeftPopUp);
        this.footer.setLeftLabel(footerLeftCmd[0]);
        this.footer.setRightLabel(footerRightCmd[0]);

        ss = new Session();
        
        vYahooUser = new Vector();
        Hashtable yus = ss.getUsers();
            for(Enumeration e=yus.keys();e.hasMoreElements();){
                    String  user = (String)e.nextElement();
                     if(bHideOffline==true) {
                         if(((YahooUser)yus.get(user)).getStatus()!=0x5a55aa56 )
                            vYahooUser.addElement(user);
                     }
                     else
                        vYahooUser.addElement(user);
            }
        //setToDefaultFakeUser();
        mHashtable=new Hashtable();
        htStatus =  new Hashtable();
        htFriendDetails =  new Hashtable();
    }

    public void newMailReceived(String countNewMail) {
        this.countNewMail = countNewMail;
        newMailReceived=true;
    }

    /**
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        // update scrollable:
        scrollable.setScroll(vYahooUser.size(), selection);
        scrollable.paint(g);
        if(!leftPopUp.getVisible()){
            footer.setLeftLabel(footerLeftCmd[0]);
            footer.setRightLabel(footerRightCmd[0]);
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
     * @param g
     */
    protected void paintInternal(Graphics g) {
        // draw:
        /*Be care full choose the color*/
        loadYahooUser();
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        if(vYahooUser.size()==0)
            return;
        // draw each friend:
        int yahooUserSize = vYahooUser.size();
        //Untuk representasi tampilan di layar
        synchronized(this) {
            int start = 0;
            int end = yahooUserSize;
            if(end>visibleLines) {
                start = selection - visibleLines / 2;
                if(start + visibleLines > yahooUserSize) {
                    // selection is near end:
                    start = yahooUserSize - visibleLines;
                }
                if(start<0){
                    start = 0;
                }
                end = start + visibleLines;
            }
            Hashtable yahooUsers = ss.getUsers();
            String f;
            YahooUser yus;
            htFriendDetails.clear();
            htStatus.clear();
            for(int i=start; i<end; i++) {
                f = (String)vYahooUser.elementAt(i);
                yus = ((YahooUser)yahooUsers.get(f));
                long s = yus.getStatus();
                int is = 0;
                Object state = mHashtable.get(f);
                if(state!=null){
                    boolean bState = ((Boolean)state).booleanValue();//new IM notification
                    if(!bState)
                        is = 5;
                }else{
                    if(s==0 || s==99){//online.png"); //0,99
                        is=0;
                    }else if(s==0x5a55aa56 || s==12){//offline.png"); //0x5a55aa56,12;
                        is=1;
                    }else if(s==2 || s==3 || s==4 || s==5 || s==6 || s==12 || s==13 || s==14){//busy.png"); //2,3,4,5,6,12,13,14
                        is=2;
                    }else if(s==0 || s==99 || s==1 || s==7 || s==8 || s==9 || s==999){//idle.png"); //1,7,8,9,999
                        is=3;
                    }else if(s==0x16){//typing.png"); //0x16
                        is=4;
                    }else{
                        System.out.println("Undefined Yahoo User Status : "+s+" ???");
                    }
                }
                htStatus.put(f, new Integer(is));
                // draw state icon:
                g.drawImage(imgStatus[is], 2, lineHeight*(i-start)+topfont, Graphics.LEFT|Graphics.TOP);
                if(i==selection) {
                    g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BACKGROUND);
                    g.fillRect(16, lineHeight*(i-start)+topfont, textWidth, lineHeight);
                    g.setColor(MainCanvas.COLOR_HIGHLIGHTED_FOREGROUND);
                }
                else {
                    g.setColor(MainCanvas.COLOR_FOREGROUND);
                }
                g.drawString(yus.getId(), 16, lineHeight*(i-start)+topfont, Graphics.LEFT|Graphics.TOP);
                String cStatus = yus.getCustomStatusMessage();
                if(cStatus!=null)
                    htFriendDetails.put(f, cStatus);
            }
        }
        if(newMailReceived){
            g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BACKGROUND);
            g.fillRect(8, height/2 - lineHeight, width-16, lineHeight*3);
            g.setColor(MainCanvas.COLOR_BORDER);
            g.drawRect(8, height/2 - lineHeight, width-16, lineHeight*3);
            g.drawImage(MainCanvas.imgStatus[8], width/2-font.stringWidth(countNewMail)/2-10, height/2, Graphics.HCENTER|Graphics.TOP);
            g.setColor(MainCanvas.COLOR_HIGHLIGHTED_FOREGROUND);
            g.drawString(countNewMail, width/2+10, height/2, Graphics.HCENTER|Graphics.TOP);
            //nanti diberi state lalu dihilangkan dengan tombol
            newMailReceived = false;
        }
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
     * @return
     */
    public String getSelectedFriend() {
        if(selection>=0 && selection<vYahooUser.size()){
            return (String) vYahooUser.elementAt(selection);
            }
        return null;
    }

    /**
     *
     * @param yahooID
     * @return
     */
    public synchronized boolean addFriend(String yahooID) {
        try {
            ss.addFriend(yahooID, "Buddies");//Jika YahooGroup ada seharunaya bisa detect group
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     *
     * @param yahooID
     * @return
     */
    public synchronized boolean removeFriend(String yahooID) {
        try {
            ss.removeFriend(yahooID, "");//seharusnya bisa tapi karena YahooGroup masih belum bisa digunakan 
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
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
    /**
     *
     * @return
     */
    public synchronized boolean moveUp() {
        if(selection>0) {
            selection--;
            return true;
        }
        return false;
    }
    /**
     *
     * @return
     */
    public synchronized boolean moveDown() {
        if(selection<vYahooUser.size()-1) {
            selection++;
            return true;
        }
        return false;
    }
    /**
     *
     * @param ss
     */
    public void setYahooSession(Session yahooMessengerSession){
        this.ss = yahooMessengerSession;
    }
    /**
     *
     */
    public void changeShowHideOffline() {
        bHideOffline=!bHideOffline;
        loadYahooUser();
        selection=vYahooUser.size()/2;
    }
    private void loadYahooUser() {
        vYahooUser = new Vector();
        Hashtable yus = ss.getUsers();
        Enumeration e=yus.keys();
        while(e.hasMoreElements()){
                    String  user = (String)e.nextElement();
                     if(bHideOffline==true) {
                         if(((YahooUser)yus.get(user)).getStatus()!=0x5a55aa56 )
                            vYahooUser.addElement(user);
                     }
                     else
                        vYahooUser.addElement(user);
            }
        vYahooUser = QuickSort.sort(vYahooUser);
    }

    /**
     *
     * @param id
     * @return
     */
    public String getFriendDetails(String id){
               Object oStatus = htFriendDetails.get(id);
        if(oStatus!=null){
            return (String)oStatus;
        }
        else
            return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public Image getFriendIcon(String id) {
        Object iImg = htStatus.get(id);
        if(iImg!=null){
            int i = ((Integer)iImg).intValue();
            return imgStatus[i];
        }
        else
            return null;
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
    public boolean setVisibleLCFriendList(boolean showLeftPopUp){
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
        leftPopUp.setSelectedToDefaultIndex();
    }
    /**
     *
     */
    public void unshowPopUp(){
        leftPopUp.setVisible(false);
    }

    /**
     * Use for new IM Notifiation
     * @param allMessages
     */
    public void setRecentMessageFromMessageListP(Vector allMessages) {
        this.vRecentUsers = new Vector();
        mHashtable.clear();
        MessageVector mv;
        for(int i=0;i<allMessages.size();i++){
            mv = ((MessageVector)allMessages.elementAt(i));
            vRecentUsers.addElement(mv.getId());
            mHashtable.put(mv.getId(), new Boolean(mv.getOpened()));
        }
    }
}
