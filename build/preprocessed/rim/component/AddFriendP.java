package rim.component;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import rim.MainCanvas;

/**
 * Panel for Add Friends// Please add Buddy already exist ^^
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */
public class AddFriendP extends ComponentUI {

    private Image[] imgStatus;//online,offline,busy,idle
    private int lineHeight;
    private int visibleLines;
    private int textWidth;
    private int selection = (-1), sbSelection = (-1);
    private Scrollable scrollable;
    //NOTE: i
    private String[] listAddFriend = {"Add Yahoo!ID : ",//0//kosong
                                 "",//1//Textfield
                                 "Group:",//2
                                 "",//3//Textfield
                                };
    private String footerLeftCmd = "OK";
    private String footerRightCmd = "Cancel";
    
    private Footer footer;
    private int topFont, topImg;
    /**
     *
     */
    public static int iStatus = 0;
    
    public AddFriendP(int x, int y, int width, int height, Font font, Scrollable scrollable,Image[] status, Footer footer) {
        super(x, y, width, height, font);
        this.scrollable = scrollable;
        this.imgStatus = status;
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
    }

    public String getFriendName() {
        return listAddFriend[1];
    }
    public String getGroupName() {
        return listAddFriend[3];
    }
    public int getMenuIndexSelected() {
        if(selection>0 && selection<listAddFriend.length){
            return selection;
            }
        return (-1);
    }
    /**
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        // update scrollable:
        scrollable.setScroll(listAddFriend.length-3, sbSelection);
        scrollable.paint(g);
        if(selection==(-1)){
            footer.setLeftLabel(null);
            footer.setRightLabel(footerRightCmd);
        }else{
            footer.setLeftLabel(footerLeftCmd);
            footer.setRightLabel(footerRightCmd);
        }
        footer.setVisible(true);
        footer.paint(g);
    }

    public void setAddFriendName(String str) {
        listAddFriend[1]=str;
    }

    public void setGroupName(String str) {
        listAddFriend[3]=str;
    }
    /**
     *
     * @param g
     */
    protected void paintInternal(Graphics g) {
        // draw:
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        // draw each friend:
        synchronized(this) {
            int start = 0;
            int end = listAddFriend.length;
            if(end>visibleLines) {
                start = selection - visibleLines / 2;
                if(start + visibleLines > listAddFriend.length) {
                    // selection is near end:
                    start = listAddFriend.length - visibleLines;
                }
                if(start<0){
                    start = 0;
                }
                end = start + visibleLines;
            }
            //Font bold = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            int iconWidth = imgStatus[0].getWidth();
            for(int i=start; i<end; i++) {
                if(i==selection) {
                    g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BACKGROUND);
                    g.fillRect(iconWidth+4, lineHeight*(i-start)+topFont, textWidth, lineHeight);
                    g.setColor(MainCanvas.COLOR_HIGHLIGHTED_FOREGROUND);
                }
                else {
                    g.setColor(MainCanvas.COLOR_FOREGROUND);
                }
                if(i==1 || i==3){
                    if(i!=selection){
                        g.setColor(MainCanvas.COLOR_BACKGROUND);//
                        g.fillRect(lineHeight+2, lineHeight*(i-start)+topFont, textWidth, lineHeight);
                        g.setColor(255,255,255);//Putih background TextField
                        g.fillRect(10, lineHeight*(i-start)+topFont-2, width-20, lineHeight);
                        if(listAddFriend[i]!=null){
                            g.setFont(font);
                            g.setColor(MainCanvas.COLOR_FOREGROUND);
                            g.drawString(listAddFriend[i], 12, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                        }
                        g.setColor(MainCanvas.COLOR_BORDER);
                        g.drawRect(10, lineHeight*(i-start)+topFont-2, width-20, lineHeight);
                        g.setColor(MainCanvas.COLOR_BACKGROUND);
                        //Bersihkan sebelah kiri
                        g.fillRect(0, lineHeight*(i-start)+topFont, 9, lineHeight);
                        //bersihkan sebelah kanan
                        g.fillRect(width-9, lineHeight*(i-start)+topFont, width-(width-9), lineHeight);
                        //hanya garis
                        g.drawLine(width-11, lineHeight*(i-start)+topFont, width-11,lineHeight*(i-start)+topFont+lineHeight-3);
                    }else if(i==selection){
                        g.setColor(MainCanvas.COLOR_BACKGROUND);//
                        g.fillRect(lineHeight+2, lineHeight*(i-start)+topFont, textWidth, lineHeight);
                        g.setColor(255,255,255);//Hitam background TextField
                        g.fillRect(10, lineHeight*(i-start)+topFont-2, width-20, lineHeight);
                        if(listAddFriend[i]!=null){
                            g.setFont(font);
                            g.setColor(0,0,0);
                            g.drawString(listAddFriend[i], 12, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                        }
                        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BORDER);//BORDER
                        g.drawRect(9, lineHeight*(i-start)+topFont-1, width-18, lineHeight-2);
                        g.drawRect(10, lineHeight*(i-start)+topFont-2, width-20, lineHeight);
                        g.setColor(MainCanvas.COLOR_BACKGROUND);
                        //Bersihkan sebelah kiri
                        g.fillRect(0, lineHeight*(i-start)+topFont, 8, lineHeight);
                        //bersihkan sebelah kanan
                        g.fillRect(width-8, lineHeight*(i-start)+topFont, width-(width-9), lineHeight);
                    }
                }
                else
                    g.drawString(listAddFriend[i], 2, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
            }
        }
    }


    //OFFLINE DEVELOP
    /**
     *
     * @param iStatus
     * @param cStatus
     */
    public void setCurrentStatus(int iStatus, String cStatus){
        this.iStatus=iStatus;
        listAddFriend[1]=cStatus;
    }
    /**
     *
     * @return
     */
    public String getSelectedStatus() {
        if(selection>3 && selection<listAddFriend.length){
            return (String) listAddFriend[selection];
            }
        return null;
    }
    /**
     *
     * @return
     */
    public synchronized boolean moveUp() {
        if(selection==(-1))
            selection=listAddFriend.length;
        if(selection>1) {
            if(selection==3){
                selection -=2;
                sbSelection--;
            }
            return true;
        }
        return false;
    }
    /**
     *
     * @return
     */
    public synchronized boolean moveDown() {
        if(selection<listAddFriend.length-1) {
            if(selection==(-1)){
                selection=1;
                sbSelection++;
            }else if(selection==1){
                selection +=2;
                sbSelection++;
            }
            return true;
        }
        return false;
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
     */
    public void setSelectionToDefault() {
        selection=(-1);
    }
}
