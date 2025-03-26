package rim;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.rms.RecordStoreException;
import rim.component.AboutP;
import rim.component.AddFriendP;
import rim.component.Footer;
import rim.component.PopUpMenu;
import rim.component.FriendDetailsP;
import rim.component.FriendListP;
import rim.component.HASC;
import rim.component.HelpP;
import rim.component.LoginP;
import rim.component.MessageListP;
import rim.component.RecentListP;
import rim.component.ScrollBar;
import rim.component.SetStatusP;
import rim.component.SettingsP;
import rim.util.Preferences;
import ymsg.network.LoginRefusedException;
import ymsg.network.StatusConstants;
import ymsg.network.Session;
import ymsg.network.YahooGroup;
import ymsg.network.YahooUser;
import ymsg.network.event.SessionErrorEvent;
import ymsg.network.event.SessionEvent;
import ymsg.network.event.SessionExceptionEvent;
import ymsg.network.event.SessionFriendEvent;
import ymsg.network.event.SessionListener;
import ymsg.network.event.SessionNewMailEvent;
import ymsg.network.event.SessionNotifyEvent;

/**
 * Kelas canvas utama sebagai inisialisasi dan mengatur canvas panel yang akan digambarkan ke layar
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */

public class MainCanvas extends Canvas implements CommandListener, SessionListener {
    //class variable
    private Session ss;
    private final Font font;
    private final int width;
    private final int height;
    public static int COLOR_BACKGROUND;
    public static int COLOR_FOREGROUND;
    public static int COLOR_BORDER;
    public static int COLOR_HIGHLIGHTED_BACKGROUND;
    public static int COLOR_HIGHLIGHTED_FOREGROUND;
    public static int COLOR_HIGHLIGHTED_BORDER;
    public static int COLOR_BAR;
    public static int BORDER_STYLE;
    public static int BORDER_STYLE_HIGHLIGHTED;

    //the thread
    private ConnectingThread ct;
    private EventThread et;
    
    //ui components: //private???
    private HASC hasc;
    private ScrollBar scroll;
    private FriendListP flp;
    private MessageListP mlp;
    private RecentListP rlp;
    private FriendDetailsP fdp;
    private SetStatusP ssp;
    private SettingsP sp;
    private LoginP lp;
    private AboutP ap;
    private HelpP hp;
    private AddFriendP afp;

    //sharing object but different implementation each panel canvas ui
    private Footer footer;
    private PopUpMenu lpu;

    //command and textbox
    private TextBox tb;
    private Command cmdOKSetUserPassword;
    private Command cmdCancelSetUserPassword;
    private Command cmdOKIMOther;
    private Command cmdCancelIMOther;
    private Command cmdOKSendMessage;
    private Command cmdCancelSendMessage;
    private Command cmdOKCustomStatus;
    private Command cmdCancelCustomStatus;
    private Command cmdOKAddFriend;
    private Command cmdCancelAddFriend;

    //the boolean
    private boolean loggedIn = false, insertUsername, customBusy;
    private boolean[] bLogin, bSettings;

    private String pm;// = "Signing in...";
    private String username, password, sendIMTo;
    
    private boolean signInIng;
    private boolean showErrorMsg;
    private boolean isBuzz;
    private boolean newMailReceived;

    public static Image[] imgStatus;
    private String countNewMail;
    private int lineHeight;
    private boolean insertFriendName;

    private int friendListID = 0;
    private int messageListID = 1;
    private int recentListID = 2;
    private int loginPanelID = 6;

    MainCanvas(){
        setCommandListener(this);
        setFullScreenMode(true);
        width = getWidth();
        height = getHeight();
        COLOR_BAR = 0x4656a4;
        COLOR_BACKGROUND = 0xdbe7f5;
        COLOR_FOREGROUND = 0x000000;
        COLOR_BORDER = 0x000000;
        COLOR_HIGHLIGHTED_BACKGROUND = 0x496992;
        COLOR_HIGHLIGHTED_FOREGROUND = 0x000000;
        COLOR_HIGHLIGHTED_BORDER = 0x496992;
        // init font:
        font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);

        lineHeight = font.getHeight()+2;
        if(lineHeight<15){
            lineHeight=15;
        }

        BORDER_STYLE = 0x496992;
        BORDER_STYLE_HIGHLIGHTED = 496992;

        // init ui components:
        int BASE_TOP = font.getHeight() + 2;//24;
        if(BASE_TOP<20){
            BASE_TOP=20;
        }
        //final int BASE_TOP = HEADER_HEIGHT;
        final int BASE_BOTTOM = height-BASE_TOP;
        final int SCROLL_WIDTH = 5;
        final int SCALE_WIDTH = width - SCROLL_WIDTH;
        final int SCALE_HEIGHT = height - BASE_TOP - BASE_TOP;

        loadPreferences();

        // init image
        imgStatus = new Image[11];
        try {
            imgStatus[0] = Image.createImage("/res/online.png"); //0,99
            imgStatus[1] = Image.createImage("/res/offline.png"); //0x5a55aa56,12;
            imgStatus[2] = Image.createImage("/res/busy.png"); //2,3,4,5,6,12,13,14
            imgStatus[3] = Image.createImage("/res/idle.png"); //1,7,8,9,999
            imgStatus[4] = Image.createImage("/res/typing.png"); //0x16 //not implemented yet
            imgStatus[5] = Image.createImage("/res/newmessage.png"); //alert someone send msg
            imgStatus[6] = Image.createImage("/res/friendlist.png");
            imgStatus[7] = Image.createImage("/res/info.png");
            imgStatus[8] = Image.createImage("/res/newmail.png");
            imgStatus[9] = Image.createImage("/res/progress.png");//error.png
            imgStatus[10] = Image.createImage("/res/error.png");
        }
        catch(Exception e) {
            throw new Error("Failed loading resource.");
        }
        scroll = new ScrollBar(SCALE_WIDTH, BASE_TOP, SCALE_HEIGHT, font);
        scroll.setVisible(true);
        footer = new Footer(0,BASE_BOTTOM,this.width,BASE_TOP,font);
        footer.setVisible(true);
        //scroll and footer always visible, so, that can be shared
        //friendlist, meessagelist, and popup not like that, so that must be different state.
        flp = new FriendListP(0, BASE_TOP, SCALE_WIDTH, SCALE_HEIGHT, font, scroll, imgStatus, footer);
        mlp = new MessageListP(0, BASE_TOP, SCALE_WIDTH, SCALE_HEIGHT, font, scroll, footer);
        rlp = new RecentListP(0, BASE_TOP, SCALE_WIDTH, SCALE_HEIGHT, font, scroll, imgStatus, footer);
        fdp = new FriendDetailsP(0, BASE_TOP, SCALE_WIDTH, SCALE_HEIGHT, font, scroll, footer);
        ssp = new SetStatusP(0, BASE_TOP, SCALE_WIDTH, SCALE_HEIGHT, font, scroll, imgStatus, footer);
        sp = new SettingsP(0, BASE_TOP, SCALE_WIDTH, SCALE_HEIGHT, font, scroll, bSettings, footer);
        lp = new LoginP(0, BASE_TOP, SCALE_WIDTH, SCALE_HEIGHT, scroll, bLogin, footer);
        ap = new AboutP(0, BASE_TOP, SCALE_WIDTH, SCALE_HEIGHT, font, scroll, footer);
        hp = new HelpP(0, BASE_TOP, SCALE_WIDTH, SCALE_HEIGHT, font, scroll, footer, imgStatus);
        afp = new AddFriendP(0, BASE_TOP, SCALE_WIDTH, SCALE_HEIGHT, font, scroll, imgStatus, footer);
        
        hasc = new HASC(
                0, 1, width, BASE_TOP, font,
                flp, mlp, rlp, fdp, ssp, sp, lp, ap, hp, afp, imgStatus
        );
        hasc.setVisible(true);//friendlist as the first component so, it's visible as default
        hasc.setVisibleLoginPanel();
        signInIng = false;
        loadUsernamePassword();
        if(username!=null || password!=null){
            if(bLogin[1])
            connect();
        }
        else{
            pm = "Please fill username and password";
            setToErrorMode();
        }
        repaint();
        
        friendListID = 0;
        messageListID = 1;
        recentListID = 2;
        loginPanelID = 6;
    }

    private void getFriendDetails(int fromScreen) {
        String friend = null;
        if(fromScreen==friendListID)
            friend = flp.getSelectedFriend();
        else if(fromScreen==recentListID)
            friend = rlp.getSelectedFriend();
        else if(fromScreen==messageListID)
            friend = mlp.getMVIDUser();
        if(friend!=null){
            fdp.setFriendID(friend);
            fdp.setFriendIcon(flp.getFriendIcon(friend));
            fdp.setCustomMessageStatus(flp.getFriendDetails(friend));
            hasc.setCurrentVisibleBefore(fromScreen);
            hasc.setVisibleFriendDetails();
            repaint();
        }
    }

    private void openMessageList(int windowID) {
        String sendTo = null;
        if(windowID==friendListID){
            hasc.setCurrentVisibleBefore(friendListID);
            sendTo = flp.getSelectedFriend();
        }else if(windowID==recentListID){
            hasc.setCurrentVisibleBefore(recentListID);
            sendTo = rlp.getSelectedFriend();
        }   
        if(sendTo!=null){
            if(!mlp.searchOnlyUserMessage(sendTo)){
                mlp.createUserMessage(sendTo);
            }   
            hasc.setVisibleMessageList();
            repaint();
        }
    }
    /**
     *
     * @param to
     * @param msg
     */
    private void sendMessage(String to, String message) {
        try {
            ss.sendMessage(to, message);
        } catch (IllegalStateException ex) {
        } catch (IOException ex) {
        }
        mlp.addSentMessage(to, message);
        mlp.setStateCurrentMVID(true);
    }

    /**
     *
     * @param to
     */
    private void sendBuzz(String yahooID) {
        try {
            ss.sendBuzz(yahooID);
        } catch (IllegalStateException ex) {
        } catch (IOException ex) {
        }
        mlp.addSentMessage(yahooID, "BUZZ!!!");
        mlp.setStateCurrentMVID(true);
    }
    /**
     *
     * @param key
     */
    protected void keyPressed(int key) {
        boolean needRepaint = false;
        if(!signInIng){
        switch (key) {
			case -1: // up
                if(flp.getVisible()){
                    if(flp.getVisibleLC())
                        needRepaint = flp.moveUpLC();
                    else
                        needRepaint = flp.moveUp();
                }else if(mlp.getVisible()){
                    if(mlp.getVisibleLC())
                        needRepaint = mlp.moveUpLC();
                    else
                        needRepaint = mlp.moveUp();
                }else if(rlp.getVisible()){
                    if(rlp.getVisibleLC())
                        needRepaint = rlp.moveUpLC();
                    else
                        needRepaint = rlp.moveUp();
                }else if(lp.getVisible()){
                    if(lp.getVisibleLCLoginPanel())
                        needRepaint = lp.moveUpLC();
                    else
                        needRepaint = lp.moveUp();
                }else if(ssp.getVisible()){
                    needRepaint = ssp.moveUp();
                }else if(sp.getVisible()){
                    needRepaint = sp.moveUp();
                }else if(ap.getVisible()){
                    needRepaint = ap.moveUp();
                }else if(hp.getVisible()){
                    needRepaint = hp.moveUp();
                }else if(ap.getVisible()){
                    needRepaint = ap.moveUp();
                }else if(afp.getVisible()){
                    needRepaint = afp.moveUp();
                }
				break;
            case -2: // down
                if(flp.getVisible()){
                    if(flp.getVisibleLC())
                        needRepaint = flp.moveDownLC();
                    else
                        needRepaint = flp.moveDown();
                }else if(mlp.getVisible()){
                    if(mlp.getVisibleLC())
                        needRepaint = mlp.moveDownLC();
                    else
                        needRepaint = mlp.moveDown();
                }else if(rlp.getVisible()){
                    if(rlp.getVisibleLC())
                        needRepaint = rlp.moveDownLC();
                    else
                        needRepaint = rlp.moveDown();
                }else if(lp.getVisible()){
                    if(lp.getVisibleLCLoginPanel())
                        needRepaint = lp.moveDownLC();
                    else
                        needRepaint = lp.moveDown();
                }else if(ssp.getVisible())
                    needRepaint = ssp.moveDown();
                else if(sp.getVisible())
                    needRepaint = sp.moveDown();
                else if(ap.getVisible())
                    needRepaint = ap.moveDown();
                else if(hp.getVisible())
                    needRepaint = hp.moveDown();
                else if(ap.getVisible())
                    needRepaint = ap.moveDown();
                else if(afp.getVisible())
                    needRepaint = afp.moveDown();
                break;
        case -5: //OK
                if(sp.getVisible()){
                    String changeTo = sp.getSelectedSettings();
                    if(changeTo!=null){
                        if(changeTo.equals("Play sound")){
                            sp.settingsChanged(0);
                        }else if(changeTo.equals("Vibrate")){
                            sp.settingsChanged(1);
                        }else if(changeTo.equals("Flash backlight")){
                            sp.settingsChanged(2);
                        }
                    }
                    needRepaint = hasc.setVisibleSettingsPanel();
                }else if(lp.getVisible()){
                    if(!lp.getVisibleLCLoginPanel()){
                        int iSelected = lp.getMenuIndexSelected();
                        if(iSelected!=(-1)){
                            cmdOKSetUserPassword = new Command("OK", Command.OK, 1);
                            cmdCancelSetUserPassword = new Command("Cancel", Command.CANCEL, 1);
                            if(iSelected==1){
                                tb = new TextBox("Yahoo! ID:", lp.getUsername(),160, TextField.ANY);
                                tb.addCommand(cmdOKSetUserPassword);
                                tb.addCommand(cmdCancelSetUserPassword);
                                tb.setCommandListener(this);
                                RamadiansyahIM.switchUI(tb);
                                insertUsername=true;
                            }else if(iSelected==3){
                                tb = new TextBox("Password:", lp.getPassword(),160, TextField.PASSWORD);
                                tb.addCommand(cmdOKSetUserPassword);
                                tb.addCommand(cmdCancelSetUserPassword);
                                tb.setCommandListener(this);
                                RamadiansyahIM.switchUI(tb);
                                insertUsername=false;
                            }else if(iSelected==5){
                                lp.settingsChanged(0);
                            }else if(iSelected==6){
                                lp.settingsChanged(1);
                            }else if(iSelected==7){
                                lp.settingsChanged(2);
                            }else if(iSelected==9){//connect
                                //savePreferences();
                                connect();
                            }
                        }
                        if(iSelected!=9)
                            needRepaint = hasc.setVisibleLoginPanel();
                    }
                }else if(afp.getVisible()){
                    int iSelected = afp.getMenuIndexSelected();
                    if(iSelected!=(-1)){
                        cmdOKAddFriend = new Command("OK", Command.OK, 1);
                        cmdCancelAddFriend = new Command("Cancel", Command.CANCEL, 1);
                        if(iSelected==1){
                            tb = new TextBox("Yahoo! ID:", afp.getFriendName(),160, TextField.ANY);
                            tb.addCommand(cmdOKAddFriend);
                            tb.addCommand(cmdCancelAddFriend);
                            tb.setCommandListener(this);
                            RamadiansyahIM.switchUI(tb);
                            insertFriendName=true;
                        }else if(iSelected==3){
                            tb = new TextBox("Add To Group:", afp.getGroupName(),160, TextField.ANY);
                            tb.addCommand(cmdOKAddFriend);
                            tb.addCommand(cmdCancelAddFriend);
                            tb.setCommandListener(this);
                            RamadiansyahIM.switchUI(tb);
                            insertFriendName=false;
                        }
                    }
                    needRepaint = true;
                }
                break;
			case -6: // Left Soft Key
                if(flp.getVisible()){
                    String cfl = flp.getCurrentFooterLeft();
                    if(cfl.equals("Menu")){
                        flp.setPopUpSelectedToDefault();
                        needRepaint = flp.setVisibleLCFriendList(true);
                    }
                    else if(cfl.equals("OK")){
                        String selectMenu = flp.getSelectedMenu();
                        if(selectMenu.equals("IM")){
                            openMessageList(friendListID);
                        }else if(selectMenu.equals("IM Other...")){
                            openMessagListOther(friendListID);
                        }else if(selectMenu.equals("Recent Chat")){
                            hasc.setCurrentVisibleBefore(friendListID);
                            flp.setRecentMessageFromMessageListP(mlp.getRecentMessageUser());
                            needRepaint = hasc.setVisibleRecentList();
                        }else if(selectMenu.equals("Show/Hide Offline")){
                            showHideOffline();
                        }else if(selectMenu.equals("Change Status")){
                            hasc.setCurrentVisibleBefore(friendListID);
                            needRepaint = hasc.setVisibleSetStatusPanel();
                        }else if(selectMenu.equals("Details")){
                            getFriendDetails(friendListID);
                        }
                        else if(selectMenu.equals("Add Friend")){
                            hasc.setCurrentVisibleBefore(friendListID);
                        needRepaint = hasc.setVisibleAddFriendPanel();
                        }
                        else if(selectMenu.equals("Remove Friend")){
                            removeFriend();
                        }
                        else if(selectMenu.equals("Settings")){
                            sp.setCurrentSettings(bSettings);
                            hasc.setCurrentVisibleBefore(friendListID);
                            needRepaint = hasc.setVisibleSettingsPanel();
                        }else if(selectMenu.equals("Logout")){
                            mlp.clearAllMessages();
                            logout();
                            hasc.setCurrentVisibleBefore(0);
                            needRepaint = hasc.setVisibleLoginPanel();
                        }else if(selectMenu.equals("Exit")){
                            saveAllPreferences();
                            logout();
                            RamadiansyahIM.switchUI(null);
                            RamadiansyahIM.quitApp();
                        }
                        flp.setVisibleLCFriendList(false);
                    }
                }else if(mlp.getVisible()){
                    String cfl = mlp.getCurrentFooterLeft();
                    if(cfl.equals("Menu")){
                        mlp.setPopUpSelectedToDefault();
                        needRepaint = mlp.setVisibleLCMessageList(true);
                    }
                    else if(cfl.equals("OK")){
                        String selectMenu = mlp.getSelectedMenu();
                        if(selectMenu.equals("Send Message")){
                            String sendTo = mlp.getMVIDUser();
                            if(sendTo!=null){
                                tb = new TextBox("IM to "+sendTo , "",160, TextField.ANY);
                                cmdOKSendMessage= new Command("OK", Command.OK, 1);
                                cmdCancelSendMessage = new Command("Cancel", Command.CANCEL, 1);
                                tb.addCommand(cmdOKSendMessage);
                                tb.addCommand(cmdCancelSendMessage);
                                tb.setCommandListener(this);
                                sendIMTo = sendTo;
                                RamadiansyahIM.switchUI(tb);
                            }
                        }else if(selectMenu.equals("BUZZ!!!")){
                            String sendTo = mlp.getMVIDUser();
                            if(sendTo!=null){
                                tb = new TextBox("IM to "+sendTo , "",160, TextField.ANY);
                                sendBuzz(sendTo);
                                needRepaint=true;
                            }
                        }else if(selectMenu.equals("Details")){
                            getFriendDetails(messageListID);
                        }else if(selectMenu.equals("Recent Chat")){
                            hasc.setCurrentVisibleBefore(messageListID);
                            flp.setRecentMessageFromMessageListP(mlp.getRecentMessageUser());
                            needRepaint = hasc.setVisibleRecentList();
                        }else if(selectMenu.equals("FriendList")){
                            flp.setRecentMessageFromMessageListP(mlp.getRecentMessageUser());
                            needRepaint = hasc.setVisibleFriendList();
                        }else if(selectMenu.equals("Close")){
                            mlp.deleteMessage(mlp.getMVIDUser());
                            needRepaint =hasc.setVisibleFriendList();
                        }
                        mlp.setVisibleLCMessageList(false);
                    }
                }else if(rlp.getVisible()){
                    String crl = rlp.getCurrentFooterLeft();
                    if(crl.equals("Menu")){
                        rlp.setPopUpSelectedToDefault();
                        needRepaint = rlp.setVisibleLCRecentList(true);
                    }
                    else if(crl.equals("OK")){
                        String selectMenu = rlp.getSelectedMenu();
                        if(selectMenu.equals("IM")){ 
                            openMessageList(recentListID);
                        }else if(selectMenu.equals("IM Other...")){
                            openMessagListOther(recentListID);
                        }else if(selectMenu.equals("FriendList")){
                            flp.setRecentMessageFromMessageListP(mlp.getRecentMessageUser());
                            needRepaint = hasc.setVisibleFriendList();
                        }else if(selectMenu.equals("Change Status")){
                            hasc.setCurrentVisibleBefore(recentListID);
                            needRepaint = hasc.setVisibleSetStatusPanel();
                        }else if(selectMenu.equals("Details")){
                            getFriendDetails(recentListID);
                        }else if(selectMenu.equals("LogOut")){
                            mlp.clearAllMessages();
                            logout();
                            hasc.setCurrentVisibleBefore(0);
                            needRepaint = hasc.setVisibleLoginPanel();
                        }else if(selectMenu.equals("Exit")){
                            saveAllPreferences();
                            logout();
                            RamadiansyahIM.switchUI(null);
                            RamadiansyahIM.quitApp();
                        }
                        rlp.setVisibleLCRecentList(false);
                    }
                }else if(ssp.getVisible()){
                    setStatus();
                }else if(sp.getVisible()){
                    String clsp = sp.getCurrentFooterLeft();
                    if(clsp!=null){
                        if(clsp.equals("OK")){
                            bSettings = sp.getSettingsChanges();
                            needRepaint = hasc.setVisibleScreen(hasc.getScreenVisibleBefore());
                        }
                    }
                }else if(lp.getVisible()){
                    String css = lp.getCurrentFooterLeft();
                    if(css!=null){
                        if(css.equals("Menu")){
                            lp.setPopUpSelectedToDefault();
                            needRepaint = lp.setVisibleLCLoginPanel(true);
                        }else if(css.equals("OK")){
                            String selectedMenu = lp.getSelectedMenu();
                            if(selectedMenu!=null){
                                if(selectedMenu.equals("Settings")){
                                    sp.setCurrentSettings(bSettings);
                                    hasc.setCurrentVisibleBefore(loginPanelID);
                                    needRepaint = hasc.setVisibleSettingsPanel();
                                }else if(selectedMenu.equals("Help")){
                                    needRepaint = hasc.setVisibleHelpPanel();
                                }else if(selectedMenu.equals("About")){
                                    needRepaint = hasc.setVisibleAboutPanel();
                                }else if(selectedMenu.equals("Exit")){
                                    saveAllPreferences();
                                    logout();
                                    RamadiansyahIM.switchUI(null);
                                    RamadiansyahIM.quitApp();
                                }
                            }
                            lp.setVisibleLCLoginPanel(false);
                        }
                    }
                }else if(afp.getVisible()){
                    addFriend();
                }
                break;
			case -7: // right soft key
                if(showErrorMsg){
                    hasc.setVisibleLoginPanel();
                    showErrorMsg=false;
                    needRepaint=true;
                }
                else if(flp.getVisible()){
                    String cfl = flp.getCurrentFooterRight();
                    if(cfl!=null){
                        if(cfl.equals("Cancel")){
                            needRepaint = flp.setVisibleLCFriendList(false);
                        }
                    }
                    flp.setVisibleLCFriendList(false);
                }
                else if(mlp.getVisible()){
                    String cfl = mlp.getCurrentFooterRight();
                    if(cfl.equals("Hide")){
                        needRepaint = hasc.setVisibleScreen(hasc.getScreenVisibleBefore());
                    }
                    else if(cfl.equals("Cancel")){
                        needRepaint = mlp.setVisibleLCMessageList(false);
                    }
                }else if(rlp.getVisible()){
                    String cfl = rlp.getCurrentFooterRight();
                    if(cfl.equals("Hide")){
                        hasc.setCurrentVisibleBefore(0);
                        needRepaint = hasc.setVisibleScreen(0);
                    }
                    else if(cfl.equals("Cancel")){
                        needRepaint = rlp.setVisibleLCRecentList(false);
                    }                
                }else if(fdp.getVisible()){
                    needRepaint = hasc.setVisibleFriendList();
                }else if(ssp.getVisible()){
                    String cfl = ssp.getCurrentFooterRight();
                    if(cfl.equals("Cancel")){
                        needRepaint = hasc.setVisibleScreen(hasc.getScreenVisibleBefore());
                    }
                }else if(sp.getVisible()){
                    String cfd = sp.getCurrentFooterRight();
                    if(cfd!=null){
                        if(cfd.equals("Cancel")){
                            bSettings = sp.getCurrentSettings();
                            needRepaint = hasc.setVisibleScreen(hasc.getScreenVisibleBefore());
                        }
                    }
                }else if(lp.getVisible()){
                    String cfd = lp.getCurrentFooterRight();
                    if(cfd!=null){
                        if(cfd.equals("Cancel")){
                            needRepaint=lp.setVisibleLCLoginPanel(false);
                        }
                    }
                }else if(ap.getVisible()){
                    String cfd = ap.getCurrentFooterRight();
                    if(cfd!=null){
                        if(cfd.equals("Back")){
                            needRepaint = hasc.setVisibleLoginPanel();
                        }
                    }
                }else if(hp.getVisible()){
                    String cfd = hp.getCurrentFooterRight();
                    if(cfd!=null){
                        if(cfd.equals("Close")){
                            needRepaint = hasc.setVisibleLoginPanel();
                        }
                    }
                }else if(afp.getVisible()){
                    String cfd = afp.getCurrentFooterRight();
                    if(cfd!=null){
                        if(cfd.equals("Cancel")){
                            needRepaint = hasc.setVisibleScreen(hasc.getScreenVisibleBefore());
                        }
                    }
                }
                break;
        }
        }
        if(needRepaint) {
            repaint();
        }
    }
    /**
     *
     * @param g
     */
    protected void paint(Graphics g) {
        g.setFont(font);
        g.setColor(COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        if(newMailReceived){
            flp.newMailReceived(countNewMail);
            newMailReceived=false;
        }
        if(signInIng) {
            // show log in...
            //g.setColor(COLOR_FOREGROUND);
            pm = "Signing in...";
            lp.showLoginProcess(pm);
            //signInIng=false;
        }else if(showErrorMsg) {
            // show log in...
            //g.setColor(COLOR_FOREGROUND);
            //g.drawString(pm, width/2, height/2, Graphics.BASELINE|Graphics.HCENTER);
            lp.showErrorMessage(pm);
            showErrorMsg=false;
        }
        hasc.paint(g);
            
    }

    private void addFriend() {
        String newFriend, group, lc = afp.getCurrentFooterLeft();
        if(lc!=null){
            if(lc.equals("OK")){
                newFriend = afp.getFriendName();
                group = afp.getGroupName();
                if(newFriend!=null && group!=null){
                    try {
                        ss.addFriend(newFriend, group);
                    } catch (IllegalStateException ex) {
                    } catch (IOException ex) {
                    }
                }
                hasc.setVisibleScreen(hasc.getScreenVisibleBefore());
                repaint();
            }
        }
    }

    /*private void addFriend() {
        hasc.setCurrentVisibleBefore(friendListID);
        needRepaint = hasc.setVisibleAddFriendPanel();
    }*/
    /*private void printYahooUser() {
        //------------- Start : Just Print FriendListP -----------------------//
        Hashtable yus = ss.getUsers();
            for(Enumeration e=yus.keys();e.hasMoreElements();){
                Hashtable h = ss.getUsers();
                    String  user = (String)e.nextElement();
                        System.out.println(user+" ==> "+((YahooUser)h.get(user)).toString());
                        //System.out.println(user+" ==> "+((YahooUser)h.get(user)).getId() +", status = "+ ((YahooUser)h.get(user)).getStatus() +", customMessage = "+ ((YahooUser)h.get(user)).getCustomStatusMessage());
            }
        System.out.println("FriendSize :" + ss.getUsers().size() + "\nEnd : Print the Yahoo! user");
        //------------- End : Just Print FriendListP -----------------------//
    }*/

    private void removeFriend() {
        String rFriend = flp.getSelectedFriend();
        YahooGroup[] yg = ss.getGroups();
        YahooUser yu;
        String fGroup;
        for(int i=0;i<yg.length;i++)
        {   fGroup = yg[i].getName();
            for(int j=0;j<yg[i].getMembers().size();j++){
                yu = yg[i].getUserAt(j);
                if(yu.getId().equals(rFriend)){
                    try {
                        ss.removeFriend(rFriend, fGroup);
                    } catch (IllegalStateException ex) {
                    } catch (IOException ex) {
                    }
                    flp.setVisibleLCFriendList(false);
                    break;
                }
            }
        }
        repaint();
    }

    private void openMessagListOther(int windowID) {
        if(windowID==friendListID){
            hasc.setCurrentVisibleBefore(friendListID);
        }else if(windowID==recentListID){
            hasc.setCurrentVisibleBefore(recentListID);
        }
        tb = new TextBox("Send IM to:", "",160, TextField.ANY);
        cmdOKIMOther = new Command("OK", Command.OK, 1);
        cmdCancelIMOther = new Command("Cancel", Command.CANCEL, 1);
        tb.addCommand(cmdOKIMOther);
        tb.addCommand(cmdCancelIMOther);
        tb.setCommandListener(this);
        RamadiansyahIM.switchUI(tb);
    }

    private void setStatus() {
        String lc = ssp.getCurrentFooterLeft();
        if(lc!=null){
            if(lc.equals("OK")){
                String changeTo = ssp.getSelectedStatus();
                if(changeTo!=null){
                    if(changeTo.equals("Available")){
                        ssp.setCurrentStatus(0,"Available");
                        setStatusTo(StatusConstants.STATUS_AVAILABLE);
                    }else if(changeTo.equals("I'm using ramadiansyahIM")){
                        ssp.setCurrentStatus(0,"I'm using ramadiansyahIM");
                        setCustomStatus("I\'m using ramadiansyahIM", false);
                    }else if(changeTo.equals("I'm mobile")){
                        ssp.setCurrentStatus(0,"I'm mobile");
                        setCustomStatus("I\'m mobile", false);
                    }else if(changeTo.equals("Busy")){
                        ssp.setCurrentStatus(2,"Busy");
                        setStatusTo(StatusConstants.STATUS_BUSY);
                    }else if(changeTo.equals("Not at My Desk")){
                        ssp.setCurrentStatus(3,"Not at My Desk");
                        setStatusTo(StatusConstants.STATUS_NOTATDESK);
                    }else if(changeTo.equals("On the Phone")){
                        ssp.setCurrentStatus(2,"On The Phone");
                        setStatusTo(StatusConstants.STATUS_ONPHONE);
                    }else if(changeTo.equals("Invisible")){
                        ssp.setCurrentStatus(1,"Invisble");
                        setStatusTo(StatusConstants.STATUS_INVISIBLE);
                    }else if(changeTo.equals("Custom")){
                        tb = new TextBox("Custom Status:", "",160, TextField.ANY);
                        cmdOKCustomStatus = new Command("OK", Command.OK, 1);
                        cmdCancelCustomStatus = new Command("Cancel", Command.CANCEL, 1);
                        tb.addCommand(cmdOKCustomStatus);
                        tb.addCommand(cmdCancelCustomStatus);
                        tb.setCommandListener(this);
                        customBusy=false;
                        RamadiansyahIM.switchUI(tb);
                    }else if(changeTo.equals("Custom Busy")){
                        tb = new TextBox("Custom Status Busy:", "",160, TextField.ANY);
                        cmdOKCustomStatus = new Command("OK", Command.OK, 1);
                        cmdCancelCustomStatus = new Command("Cancel", Command.CANCEL, 1);
                        tb.addCommand(cmdOKCustomStatus);
                        tb.addCommand(cmdCancelCustomStatus);
                        tb.setCommandListener(this);
                        customBusy=true;
                        RamadiansyahIM.switchUI(tb);
                    }
                }
                ssp.setSelectionToDefault();
                hasc.setVisibleScreen(hasc.getScreenVisibleBefore());
                repaint();
            }
        }
    }

    /*private void setGroupForAddFriend() {
        YahooGroup[] yg = ss.getGroups();
        String[] groups = new String[yg.length];
        for(int i=0;i<yg.length;i++)
            groups[i]=yg[i].getName();
        afp.addList(groups);
    }*/
    /**
     *
     * @param ss
     */
    private void setYahooSession(Session ss){
        flp.setYahooSession(ss);
        rlp.setYahooSession(ss);
    }

    private void connect() {
        signInIng=true;
        repaint();
        if(ct==null) {
            ct = new ConnectingThread(this);
            ct.start();
        }
    }

    private void playEvent() {
        if(et==null) {
            et = new EventThread();
            et.start();
        }
    }
    

    private void logout() {
        if(ss!=null){
            if(ss.getSessionStatus()==StatusConstants.MESSAGING){
                try {
                    ss.logout();
                } catch (IllegalStateException ex) {
                } catch (IOException ex) {
                }
            }
        }
    }

    private void loadUsernamePassword() {
        Preferences pref = new Preferences();
        try {
            pref.loadUsernameAndPassword();
            this.username = pref.get("username");
            this.password = pref.get("password");
        }catch (RecordStoreException ex) {
                ex.printStackTrace();
        }
        if(username!=null)
            lp.setUsername(username);
        else
            this.username = "";
        if(password!=null)
            lp.setPassword(password);
        else
            this.password = "";

        //Next current status dari session saja..
        if(!bLogin[2])
            ssp.setCurrentStatus(0,"Available");
        else
            ssp.setCurrentStatus(1,"Invisible");
    }

    private void loadPreferences() {
        Preferences pref = new Preferences();
        try {
            pref.loadUsernameAndPassword();
            this.username = pref.get("username");
            this.password = pref.get("password");
            pref.open("rim_bLogin");
            bLogin = pref.readStream();
            pref.open("rim_bSettings");
            bSettings = pref.readStream();
        }catch (RecordStoreException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        if(bLogin==null)
            bLogin = new boolean[] {false, false, false};
        if(bSettings==null)
            bSettings = new boolean[] {false, false, false};
    }

    private void saveAllPreferences() {
        Preferences pref = new Preferences();
        if(bLogin[0]){
            try {
                pref.saveUsernameAndPassword(username, password);
                pref.open("rim_bLogin");
                pref.writeStream(bLogin);
                pref.open("rim_bSettings");
                pref.writeStream(bSettings);
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
        }else{
            try {
                pref.open("rim_upp");
                pref.saveUsernameAndPassword("", "");
                pref.open("rim_bLogin");
                pref.writeStream(new boolean[]{false, false, false});
                pref.open("rim_bSettings");
                pref.writeStream(new boolean[]{false, false, false});
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
        }
    }
    //set status
    private void setCustomStatus(String customMessage, boolean isBusy) {
        try {
            ss.setStatus(customMessage, isBusy);
        } catch (IllegalArgumentException ex) {
        } catch (IOException ex) {
        }
    }
    private void setStatusTo(long lStatusCons) {
        try {
            ss.setStatus(lStatusCons);
        } catch (IllegalArgumentException ex) {
        } catch (IOException ex) {
        }
    }

    /**
     * 
     * @param to
     * @param msg
     */
    private void messageReceived(String from, String msg) {
        mlp.addReceivedMessage(from, msg);//set false here
        if(mlp.getVisible()){
            if(mlp.getMVIDUser().equals(from)){
                mlp.searchOnlyUserMessage(from);
                mlp.setStateCurrentMVID(true);
                playEvent();
            }
        }
        else if(rlp.getVisible())
            rlp.setRecentMessageFromMessageListP(mlp.getRecentMessageUser());
        else if(flp.getVisible())
            flp.setRecentMessageFromMessageListP(mlp.getRecentMessageUser());
        repaint();
    }
    /**
     *
     * @param c
     * @param d
     */
    public void commandAction(Command c, Displayable d) {
        String str;
        if(c == cmdOKSetUserPassword){
            str = tb.getString();
            if(insertUsername){
                username=str;
                lp.setUsername(str);
            }else{
                password=str;
                lp.setPassword(str);
            }
        }
        else if(c == cmdOKSendMessage){
                str = tb.getString();
                if(!str.equals(""))
                sendMessage(sendIMTo, str);
                hasc.setVisibleMessageList();
        }else if(c == cmdOKIMOther){
            str = tb.getString();
            if(!str.equals("")){
                if(!mlp.searchUserMessage(str))
                    mlp.createUserMessage(str);
            }
            hasc.setVisibleMessageList();
        }
        else if(c == cmdOKCustomStatus){
            str = tb.getString();
            if(!str.equals("")){
                if(!customBusy){
                    ssp.setCurrentStatus(0,str);
                    setCustomStatus(str, false);
                }
                else{
                    ssp.setCurrentStatus(2,str);
                    setCustomStatus(str, true);
                }
            }
            hasc.setVisibleScreen(hasc.getScreenVisibleBefore());
        } 
        else if(c == cmdOKAddFriend){
            str = tb.getString();
            if(insertFriendName)
                afp.setAddFriendName(str);
            else
                afp.setGroupName(str);
        }
        RamadiansyahIM.switchUI(this);
    }    

    /**
     * method yang mengatur perilaku aplikasi ketika koneksi berhasil dilakukan.
     */
    /*public void connectionEstablished() {
        setToChatMode();
        //System.out.println("connectionEstablished.");
    }*/

    private void setToChatMode() {
        signInIng=false;
        hasc.setVisibleFriendList();
        repaint();
    }
    private void setToErrorMode() {
        signInIng=false;
        showErrorMsg=true;
        hasc.setVisibleLoginPanel();
        repaint();
    }

    //Session Listener
    /**
     *
     * @param ev
     */
    public void buzzReceived(SessionEvent ev){
        isBuzz=true;
        messageReceived(ev.getFrom(), "BUZZ!!!");
    }

    /**
     *
     * @param ev
     */
    public void messageReceived(SessionEvent ev){
        messageReceived(ev.getFrom(), ev.getMessage());
    }


    /**
     * method yang mengatur perilaku aplikasi ketika terjadi error pada koneksi.
     * ex : - tidak diijikan melakukan akses jaringan oleh user.
     *      - koneksi jaringan tidak tersedia
     *      - username/password yang dimasukkan salah. //refused login..
     *      - Untuk mengtahui login dari client lain 
     * @param ev
     */
    public void connectionClosed(SessionEvent ev) {
        //System.out.println("connectionClosed");
        //pm = ev.getMessage();
        if(pm==null)
                pm = "Connection Closed";
        if(ev.getMessage()!=null){
            pm = ev.getMessage();
        }
        setToErrorMode();
    }

    private void connectionError(String processMessage) {
        pm = processMessage;
        if(pm==null)
            pm = "Connection Error";
        setToErrorMode();
    }

    /**
     *
     * @param ev
     */
    public void listReceived(SessionEvent ev) {
        System.out.println("listReceived");
        System.out.println(ev.toString());
    }

    /**
     *
     * @param ev
     */
    public void offlineMessageReceived(SessionEvent ev) {
        System.out.println("offlineMessageReceived");
        System.out.println(ev.toString());
    }

    /**
     *
     * @param ev
     */
    public void errorPacketReceived(SessionErrorEvent ev) {
        System.out.println("errorPacketReceived");
        System.out.println(ev.toString());
    }

    /**
     *
     * @param ev
     */
    public void inputExceptionThrown(SessionExceptionEvent ev) {
        System.out.println("inputExceptionThrown");
        System.out.println(ev.toString());
    }

    /**
     *
     * @param ev
     */
    public void newMailReceived(SessionNewMailEvent ev) {
        if(ev.getMailCount() != 0){
            countNewMail = "You have " + ev.getMailCount() + " new mail(s)";
            newMailReceived = true;
            repaint();
        }
    }

    /**
     *
     * @param ev
     */
    public void notifyReceived(SessionNotifyEvent ev) {
        System.out.println("notifyReceived");
        System.out.println(ev.toString());
    }

    /**
     *
     * @param ev
     */
    public void contactRequestReceived(SessionEvent ev) {
        System.out.println("contactRequestReceived");
        //klopun kita tidak beraksi apa2 terhadap ini maka sama dengan diiijonkan..
        System.out.println(ev.toString());
    }

    /**
     *
     * @param ev
     */
    public void contactRejectionReceived(SessionEvent ev) {
        System.out.println("contactRejectionReceived");
        System.out.println(ev.toString());
    }

    /**
     *
     * @param ev
     */
    public void friendsUpdateReceived(SessionFriendEvent ev) {
        System.out.println("friendsUpdateReceived");
        YahooUser[] l = ev.getFriends();
			for(int i=0;i<l.length;i++)
				System.out.println("Updated: "+l[i].toString());//Bahkan friend update salah merepresentasikan...
        if(flp.getVisible()||rlp.getVisible())
            repaint();
    }

    /**
     *
     * @param ev
     */
    public void friendAddedReceived(SessionFriendEvent ev) {
        System.out.println("friendAddedReceived");
        System.out.println(ev.toString());
    }

    /**
     *
     * @param ev
     */
    public void friendRemovedReceived(SessionFriendEvent ev) {
        System.out.println("friendRemovedReceived");
        //mc.setYahooSession(ss);
    }

    private void showHideOffline() {
        flp.changeShowHideOffline();
        repaint();
    }


    /**
     * Login Task
     */
   private class ConnectingThread extends Thread {
       private MainCanvas mc;
       public ConnectingThread(MainCanvas mc){
            this.mc=mc;
       }
        public void run() {
            try {
                while(true) {
                    // Create a session
                    ss = new Session();
                    ss.addSessionListener(mc);
                    if(bLogin[2])
                        ss.setStatus(StatusConstants.STATUS_INVISIBLE);
                    else
                        ss.setStatus(StatusConstants.STATUS_AVAILABLE);
                    ss.login(username, password);
                    if (ss.getSessionStatus()==StatusConstants.MESSAGING){
                        setYahooSession(ss);
                        break;
                        //printYahooUser();
                        //setGroupForAddFriend();
                    }else{
                       ss.reset(); // Failed to log in - reset so we can re-use the same object
                    }
                }
            } catch (IllegalArgumentException ex) {
                connectionError(ex.getMessage());
                return;
            } catch (IOException ex) {
                connectionError(ex.getMessage());
                return;
            } catch (IllegalStateException ex) {
                connectionError(ex.getMessage());
                return;
            } catch (LoginRefusedException ex) {
                connectionError("Login Refused. Please check your username and password");
                return;
            } catch (SecurityException ex) {
                connectionError("Security Error: " + ex.getMessage() + " There may be a problem on your network settings.");
                return;
            }
            finally{
                ct = null;
            }
            // notify that connection is established:
            setToChatMode();
        }
    }

   /**
     * Event Task
     */
   private class EventThread extends Thread {
       private InputStream in;
       private Player player;

       public void run() {
            playEvent();
            et = null;
        }
       private void playEvent(){
            if(bSettings[0]){
                playFromResource();
            }
            if(bSettings[1])
                RamadiansyahIM.getDisplay().vibrate(1000);
            if(bSettings[2]){
                RamadiansyahIM.getDisplay().flashBacklight(1000);
            }
        }
        private void playFromResource() {
            try {
                if(!isBuzz)
                    in = getClass().getResourceAsStream("/res/message.wav");
                else{
                    in = getClass().getResourceAsStream("/res/buzz.wav");
                    isBuzz=false;
                }
                player = Manager.createPlayer(in, "audio/x-wav");
                player.start();
            }
            catch (Exception e) {
            }
        }
    }

}