package rim.component;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import rim.MainCanvas;
import rim.util.Gradient;

/**
 * Header and Selector Screen
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */

public class HASC extends ComponentUI {

    private int selection = 0;
    private FriendListP friendListPanel;
    private MessageListP messageListPanel;
    private RecentListP recentListPanel;
    private FriendDetailsP friendDetailsPanel;
    private SetStatusP setStatusPanel;
    private SettingsP settingsPanel;
    private LoginP loginPanel;
    private AboutP aboutPanel;
    private HelpP helpPanel;
    private AddFriendP addFriendPanel;

    private int headerHeight;
    private int topFont;
    private int topImg;
    private int iCurrentVisibleBefore;

    private Image[] imgStatus;
    /**
     *
     * @param x
     * @param y
     * @param width
     * @param headerHeight
     * @param font
     * @param friendListPanel
     * @param messageListPanel
     * @param recentlistPanel
     * @param friendDetailsPanel
     * @param setStatusPanel
     * @param settingsPanel
     * @param loginPanel
     * @param aboutPanel
     * @param helpPanel
     * @param imgStatus
     */
    public HASC(int x, int y, int width, int headerHeight, Font font, FriendListP friendListPanel, MessageListP messageListPanel, RecentListP recentlistPanel, FriendDetailsP friendDetailsPanel, SetStatusP setStatusPanel, SettingsP settingsPanel, LoginP loginPanel, AboutP aboutPanel, HelpP helpPanel, AddFriendP addFriendPanel, Image[] imgStatus) {
        super(x, y, width, headerHeight+2, font);
        this.friendListPanel = friendListPanel;
        this.messageListPanel = messageListPanel;
        this.recentListPanel = recentlistPanel;
        this.friendDetailsPanel = friendDetailsPanel;
        this.setStatusPanel = setStatusPanel;
        this.settingsPanel = settingsPanel;
        this.loginPanel = loginPanel;
        this.aboutPanel = aboutPanel;
        this.helpPanel = helpPanel;
        this.addFriendPanel = addFriendPanel;
        
        this.imgStatus = imgStatus;

        this.headerHeight = headerHeight;
        topFont = (height-font.getHeight())/2;
        topImg = height/2;

    }
    /**
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        if(friendListPanel.getVisible())
            friendListPanel.paint(g);
        else if(messageListPanel.getVisible())
            messageListPanel.paint(g);
        else if(recentListPanel.getVisible())
            recentListPanel.paint(g);
        else if(friendDetailsPanel.getVisible())
            friendDetailsPanel.paint(g);
        else if(setStatusPanel.getVisible()){
            setStatusPanel.paint(g);
        }else if(settingsPanel.getVisible()){
            settingsPanel.paint(g);
        }else if(loginPanel.getVisible()){
            loginPanel.paint(g);
        }else if(aboutPanel.getVisible()){
            aboutPanel.paint(g);
        }else if(helpPanel.getVisible()){
            helpPanel.paint(g);
        }else if(addFriendPanel.getVisible()){
            addFriendPanel.paint(g);
        }
    }

    /**
     *
     * @param g
     */
    protected void paintInternal(Graphics g) {
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        //g.fillRect(0, 0, width, height);
        Gradient.gradientBox(g, 0xffffff, MainCanvas.COLOR_BAR, 0, 0, width, height/2, Gradient.VERTICAL);
        Gradient.gradientBox(g, MainCanvas.COLOR_BAR, 0x000000, 0, height/2, width, height/2, Gradient.VERTICAL);//000000

        Font bold = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        int x = 1;
        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BORDER);
        g.drawLine(0, headerHeight, width, headerHeight); // 21 tab height
        g.setFont(bold);
        g.setColor(0xffffff);
        if(friendListPanel.getVisible()){
            g.drawImage(imgStatus[6], x+2, topImg, Graphics.LEFT|Graphics.VCENTER);
            g.drawString("Friend List", x+19, topFont, Graphics.LEFT|Graphics.TOP);
        }else if(messageListPanel.getVisible()){
            //g.drawImage(imgStatus[4], x+2, topImg, Graphics.LEFT|Graphics.VCENTER);
            //System.out.println("mvIDUser : " + messageListPanel.getMVIDUser());
            g.drawImage(imgStatus[0], x+2, topImg, Graphics.LEFT|Graphics.VCENTER);
            g.drawString(messageListPanel.getMVIDUser(), x+19, topFont, Graphics.LEFT|Graphics.TOP);
        }else if(recentListPanel.getVisible()){
            g.drawImage(imgStatus[6], x+2, topImg, Graphics.LEFT|Graphics.VCENTER);
            g.drawString("Recent List", x+19, topFont, Graphics.LEFT|Graphics.TOP);
        }else if(friendDetailsPanel.getVisible()){
            g.drawImage(imgStatus[7], x+2, topImg, Graphics.LEFT|Graphics.VCENTER);
            g.drawString("Friend Details", x+19, topFont, Graphics.LEFT|Graphics.TOP);
        }else if(setStatusPanel.getVisible()){
            g.drawImage(imgStatus[SetStatusP.iStatus], x+2, topImg, Graphics.LEFT|Graphics.VCENTER);
            g.drawString("Change Status", x+19, topFont, Graphics.LEFT|Graphics.TOP);
        }else if(settingsPanel.getVisible()){
            g.drawString("Settings", x+2, topFont, Graphics.LEFT|Graphics.TOP);
        }else if(loginPanel.getVisible()){
            g.drawString("Login", x+2, topFont, Graphics.LEFT|Graphics.TOP);
        }else if(aboutPanel.getVisible()){
            g.drawImage(imgStatus[7], x+2, topImg, Graphics.LEFT|Graphics.VCENTER);
            g.drawString("About", x+19, topFont, Graphics.LEFT|Graphics.TOP);
        }else if(helpPanel.getVisible()){
            g.drawImage(imgStatus[7], x+2, topImg, Graphics.LEFT|Graphics.VCENTER);
            g.drawString("Help", x+19, topFont, Graphics.LEFT|Graphics.TOP);
        }else if(addFriendPanel.getVisible()){
            g.drawString("Add Friend", x+2, topFont, Graphics.LEFT|Graphics.TOP);
        }
        x += 71;
    }

    //stateChange
    /**
     *
     * @return
     */
    public boolean setVisibleFriendList(){
        friendListPanel.setRecentMessageFromMessageListP(messageListPanel.getRecentMessageUser());
        friendListPanel.setVisible(true);
        messageListPanel.setVisible(false);
        recentListPanel.setVisible(false);
        friendDetailsPanel.setVisible(false);
        friendDetailsPanel.setVisible(false);
        setStatusPanel.setVisible(false);
        settingsPanel.setVisible(false);
        loginPanel.setVisible(false);
        aboutPanel.setVisible(false);
        helpPanel.setVisible(false);
        addFriendPanel.setVisible(false);
        return true;
    }
    /**
     *
     * @return
     */
    public boolean setVisibleRecentList() {
        recentListPanel.setRecentMessageFromMessageListP(messageListPanel.getRecentMessageUser());
        friendListPanel.setVisible(false);
        messageListPanel.setVisible(false);
        recentListPanel.setVisible(true);
        friendDetailsPanel.setVisible(false);
        setStatusPanel.setVisible(false);
        settingsPanel.setVisible(false);
        loginPanel.setVisible(false);
        aboutPanel.setVisible(false);
        helpPanel.setVisible(false);
        addFriendPanel.setVisible(false);
        return true;
    }
    /**
     *
     * @return
     */
    public boolean setVisibleMessageList(){
        friendListPanel.setVisible(false);
        messageListPanel.setVisible(true);
        recentListPanel.setVisible(false);
        friendDetailsPanel.setVisible(false);
        setStatusPanel.setVisible(false);
        settingsPanel.setVisible(false);
        loginPanel.setVisible(false);
        aboutPanel.setVisible(false);
        helpPanel.setVisible(false);
        addFriendPanel.setVisible(false);
        return true;
    }
    /**
     *
     * @return
     */
    public boolean setVisibleFriendDetails() {
        friendListPanel.setVisible(false);
        messageListPanel.setVisible(false);
        recentListPanel.setVisible(false);
        friendDetailsPanel.setVisible(true);
        setStatusPanel.setVisible(false);
        settingsPanel.setVisible(false);
        loginPanel.setVisible(false);
        aboutPanel.setVisible(false);
        helpPanel.setVisible(false);
        addFriendPanel.setVisible(false);
        return true;
    }
    /**
     *
     * @return
     */
    public boolean setVisibleSetStatusPanel(){
        friendListPanel.setVisible(false);
        messageListPanel.setVisible(false);
        recentListPanel.setVisible(false);
        friendDetailsPanel.setVisible(false);
        setStatusPanel.setVisible(true);
        settingsPanel.setVisible(false);
        loginPanel.setVisible(false);
        aboutPanel.setVisible(false);
        helpPanel.setVisible(false);
        addFriendPanel.setVisible(false);
        return true;
    }
    /**
     *
     * @return
     */
    public boolean setVisibleSettingsPanel() {
        friendListPanel.setVisible(false);
        messageListPanel.setVisible(false);
        recentListPanel.setVisible(false);
        friendDetailsPanel.setVisible(false);
        setStatusPanel.setVisible(false);
        settingsPanel.setVisible(true);
        loginPanel.setVisible(false);
        aboutPanel.setVisible(false);
        helpPanel.setVisible(false);
        addFriendPanel.setVisible(false);
        return true;
    }
    /**
     *
     * @return
     */
    public boolean setVisibleLoginPanel() {
        friendListPanel.setVisible(false);
        messageListPanel.setVisible(false);
        recentListPanel.setVisible(false);
        friendDetailsPanel.setVisible(false);
        setStatusPanel.setVisible(false);
        settingsPanel.setVisible(false);
        loginPanel.setVisible(true);
        aboutPanel.setVisible(false);
        helpPanel.setVisible(false);
        addFriendPanel.setVisible(false);
        return true;
    }
    /**
     *
     * @return
     */
    public boolean setVisibleAboutPanel() {
        friendListPanel.setVisible(false);
        messageListPanel.setVisible(false);
        recentListPanel.setVisible(false);
        friendDetailsPanel.setVisible(false);
        setStatusPanel.setVisible(false);
        settingsPanel.setVisible(false);
        loginPanel.setVisible(false);
        aboutPanel.setVisible(true);
        helpPanel.setVisible(false);
        addFriendPanel.setVisible(false);
        return true;
    }
    /**
     *
     * @return
     */
    public boolean setVisibleHelpPanel() {
        friendListPanel.setVisible(false);
        messageListPanel.setVisible(false);
        recentListPanel.setVisible(false);
        friendDetailsPanel.setVisible(false);
        setStatusPanel.setVisible(false);
        settingsPanel.setVisible(false);
        loginPanel.setVisible(false);
        aboutPanel.setVisible(false);
        helpPanel.setVisible(true);
        addFriendPanel.setVisible(false);
        return true;
    }

    public boolean setVisibleAddFriendPanel() {
        friendListPanel.setVisible(false);
        messageListPanel.setVisible(false);
        recentListPanel.setVisible(false);
        friendDetailsPanel.setVisible(false);
        setStatusPanel.setVisible(false);
        settingsPanel.setVisible(false);
        loginPanel.setVisible(false);
        aboutPanel.setVisible(false);
        helpPanel.setVisible(false);
        addFriendPanel.setVisible(true);
        return true;
    }
    /**
     *
     * @return
     */
    public int getSelection() {
        return selection;
    }
    /**
     *
     * @return
     */
    public int getScreenVisibleBefore() {
        return iCurrentVisibleBefore;
    }
    /**
     *
     * @param screenID
     */
    public void setCurrentVisibleBefore(int screenID) {
        iCurrentVisibleBefore = screenID;
    }
    /**
     *
     * @param screenID
     * @return
     */
    public boolean setVisibleScreen(int screenID){
        if(screenID==0)
            setVisibleFriendList();
        else if(screenID==1)
            setVisibleMessageList();
        else if(screenID==2)
            setVisibleRecentList();
        else if(screenID==3)
            setVisibleFriendDetails();
        else if(screenID==4)
            setVisibleSetStatusPanel();
        else if(screenID==5)
            setVisibleSettingsPanel();
        else if(screenID==6)
            setVisibleLoginPanel();
        else if(screenID==7)
            setVisibleAboutPanel();
        else if(screenID==8)
            setVisibleHelpPanel();
        return true;
    }
}

