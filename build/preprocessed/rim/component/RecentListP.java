package rim.component;

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

public class RecentListP extends ComponentUI {

    private static Session yahooMessengerSession;
    private Image[] imgStatus;
    private int lineHeight;
    private int visibleLines;
    private int textWidth;
    private int selection = 0;
    private Scrollable scrollable;
    private String[] contentLeftPopUp = {"IM",
                             "IM Other...",
                             "FriendList",
                             "Change Status",
                             "Details",
                             "LogOut",
                             "Exit"};
    private String[] footerLeftCmd = {"Menu","OK"};
    private String[] footerRightCmd = {"Hide","Cancel"};

    /*- Friendlist Shortcut
		- 5/Fire : Go/Make Friend's MessageList
		- 7 : Friend Details
		- 8 : Show of Hide Ofrline
		- 9 : Recent FriendList : ALL Status
		- * /# Move one to another MessageList*/

    private Footer footer;//komponen custom footer
    private PopUpMenu leftPopUp;
    private int topFont, topImg;
    private Hashtable mHashtable;

    //Offline Develop
    //private Vector vRecentUsersStatus;
    private Vector vRecentUsers = new Vector();

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
    public RecentListP(int x, int y, int width, int height, Font font, Scrollable scrollable,Image[] imgStatus, Footer footer) {
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
        topFont = usepixels/2;
        topImg = topFont+(lineHeight/2);
        leftPopUp = new PopUpMenu(x,y,width,height,font);
        leftPopUp.setContent(contentLeftPopUp);
        this.footer.setLeftLabel(footerLeftCmd[0]);
        this.footer.setRightLabel(footerRightCmd[0]);

        yahooMessengerSession=new Session();
    }

    /**
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        // update scrollable:
        scrollable.setScroll(vRecentUsers.size(), selection);
        scrollable.paint(g);
        //leftPopUp.setVisible(false);
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
     * @param ss
     */
    public void setYahooSession(Session ss) {
        System.out.println("Set Session");
        yahooMessengerSession=ss;
    }
    
    /**
     *
     * @param g
     */
    protected void paintInternal(Graphics g) {
        // draw:
        /*Be care full choose the color*/
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        if(vRecentUsers.size()==0)
            return;
        // draw each friend:
        int recentUserSize = vRecentUsers.size();
        System.out.println("recentUserSize " + recentUserSize );
        /*Untuk representasi tampilan di layar*/
        synchronized(this) {
            int start = 0;
            int end = recentUserSize;
            if(end>visibleLines) {
                start = selection - visibleLines / 2;
                if(start + visibleLines > recentUserSize) {
                    // selection is near end:
                    start = recentUserSize - visibleLines;
                }
                if(start<0){
                    start = 0;
                }
                end = start + visibleLines;
            }
            Hashtable yahooUsers = yahooMessengerSession.getUsers();
            String f;
            YahooUser yu;
            for(int i=start; i<end; i++) {
                f = (String)vRecentUsers.elementAt(i);
                yu = (YahooUser)yahooUsers.get(f);
                long s = 0;
                if(yu!=null)
                    s = yu.getStatus();
                else 
                    s = 12;
                int is = 0;
                if(!((Boolean)mHashtable.get(f)).booleanValue())
                    is = 5;
                else
                    if(s==0 || s==99){//online.png"); //0,99
                        is=0;
                    }else if(s==0x5a55aa56 || s==12){//ofrline.png"); //0x5a55aa56,12;
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
                // draw state icon:
                    g.drawImage(imgStatus[is], 2, lineHeight*(i-start)+topImg, Graphics.LEFT|Graphics.VCENTER);
                    if(i==selection) {
                        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BACKGROUND);
                        g.fillRect(16, lineHeight*(i-start)+topFont, textWidth, lineHeight);
                        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_FOREGROUND);
                    }
                    else {
                        g.setColor(MainCanvas.COLOR_FOREGROUND);
                    }
                    g.drawString(f, 16, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
            }
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
        if(selection>=0 && selection<vRecentUsers.size()){
            return (String) vRecentUsers.elementAt(selection);
            }
        return null;
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

    /*Selection untuk item yang dipilih dimana berdasarkan dari vRecentUser*/
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
        if(selection<vRecentUsers.size()-1) {
            selection++;
            return true;
        }
        return false;
    }

    /**
     *
     * @param allMessages
     */
    /*public void setRecentMessageFromMessageListP(Vector allMessages) {
        this.vRecentUsers = new Vector();
        mHashtable=new Hashtable();
        MessageVector mv;
        for(int i=0;i<allMessages.size();i++){
            mv = ((MessageVector)allMessages.elementAt(i));
            vRecentUsers.addElement(mv.getId());
            mHashtable.put(mv.getId(), new Boolean(mv.getOpened()));
        }
        vRecentUsers = QuickSort.sort(vRecentUsers);
    }*/
    
    public void setRecentMessageFromMessageListP(Vector allMessages) {
        vRecentUsers = new Vector();
        mHashtable=new Hashtable();
        MessageVector mv;
        for(int i=0;i<allMessages.size();i++){
            mv = ((MessageVector)allMessages.elementAt(i));
            vRecentUsers.addElement(mv.getId());
            mHashtable.put(mv.getId(), new Boolean(mv.getOpened()));
        }
        vRecentUsers = QuickSort.sort(vRecentUsers);
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
    public boolean setVisibleLCRecentList(boolean showLeftPopUp){
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


}
