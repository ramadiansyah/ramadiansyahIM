package rim.component;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import rim.MainCanvas;
/**
 * BUG: custom imgStatus not display all, that must be change to vector
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */
public class SetStatusP extends ComponentUI {

    //private static Session yahooMessengerSession;
    private Image[] imgStatus;//online,offline,busy,idle
    private int lineHeight;
    private int visibleLines;
    private int textWidth;
    private int selection = (-1), sbSelection = (-1);
    private Scrollable scrollable;
    //NOTE: i
    private String[] listSetStatus = {"Current status is : ",//0//kosong
                                 "CurrentStatus",//1//diatur
                                 "",//2//kosong 
                                 "Change to :",//3//kosong
                                 "Available",//4//online
                                 "I'm using ramadiansyahIM",//5//online
                                 "I'm mobile",//6//online
                                 "Busy",//7//busy
                                 "Not at My Desk",//8//idle
                                 "On the Phone",//9//busy
                                 "Invisible",//10//offline
                                 "",//11//kosong
                                 "Custom",//12//online
                                 "Custom Busy"//13//busy
                                };
    private String footerLeftCmd = "OK";
    private String footerRightCmd = "Cancel";
    private Footer footer;//komponen custom footer
    private int topFont, topImg;
    /**
     *
     */
    public static int iStatus = 0;
    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param font
     * @param scrollable
     * @param status
     * @param footer
     */
    public SetStatusP(int x, int y, int width, int height, Font font, Scrollable scrollable,Image[] status, Footer footer) {
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
    /*Ini bakal ada berapa baris ???*/
    /**
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        // update scrollable:
        scrollable.setScroll(listSetStatus.length-5, sbSelection);
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
            int end = listSetStatus.length;
            if(end>visibleLines) {
                start = selection - visibleLines / 2;
                if(start + visibleLines > listSetStatus.length) {
                    // selection is near end:
                    start = listSetStatus.length - visibleLines;
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
                if(i==0 || i==2 || i==3 || i==11){
                    g.drawString(listSetStatus[i], 2, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                }else{
                    //Coba di tulis ulang yang ini, ko keliatannya jelek banget y ^^
                    if(i==1){
                        g.drawImage(imgStatus[iStatus], 2, lineHeight*(i-start)+topImg, Graphics.LEFT|Graphics.VCENTER);
                    }else if(i==4 || i==5 || i==6 || i==12){//imgStatus[0]=online.png
                        g.drawImage(imgStatus[0], 2, lineHeight*(i-start)+topImg, Graphics.LEFT|Graphics.VCENTER);
                    }else if(i==10){//imgStatus[1]=offline.png
                        g.drawImage(imgStatus[1], 2, lineHeight*(i-start)+topImg, Graphics.LEFT|Graphics.VCENTER);
                    }else if(i==7 || i==9 || i==13){//imgStatus[2]=busy.png
                        g.drawImage(imgStatus[2], 2, lineHeight*(i-start)+topImg, Graphics.LEFT|Graphics.VCENTER);
                    }else if(i==8){//imgStatus[4]=idle.png
                        g.drawImage(imgStatus[3], 2, lineHeight*(i-start)+topImg, Graphics.LEFT|Graphics.VCENTER);
                    }
                    g.drawString(listSetStatus[i], iconWidth+4, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                }
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
        listSetStatus[1]=cStatus;
    }
    /**
     *
     * @return
     */
    public String getSelectedStatus() {
        if(selection>3 && selection<listSetStatus.length){
            return (String) listSetStatus[selection];
            }
        return null;
    }
    /**
     *
     * @return
     */
    public synchronized boolean moveUp() {
        //Yang diloncati berarti 0,1,2,3,11
        if(selection==(-1))
            selection=listSetStatus.length;
        if(selection>4) {
            if(selection==12){
                selection -=2;
                sbSelection--;
            }
            else{
                selection--;
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
        //Yang diloncati berarti 0,1,2,3,11
        if(selection==(-1))
            selection=3;
        if(selection<listSetStatus.length-1) {
            if(selection==10){
                selection +=2;
                sbSelection++;
            }else{
                selection++;
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
