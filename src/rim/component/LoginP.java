package rim.component;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import rim.MainCanvas;
import rim.util.TextWrap;
/**
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */
public class LoginP extends ComponentUI {
    //private static Session yahooMessengerSession;
    private int lineHeight;
    private int visibleLines;
    private int textWidth;
    private int selection = (-1);
    private int scrollbarSelection = (-1);
    private Scrollable scrollable;
    private String[] contentLeftPopUp = {"Settings",
                             "Help",
                             "About",
                             "Exit"};
    private String[] listLogin = {"Yahoo! ID:",//0
                             "", //1//untuk textfield
                             "Password:",//2
                             "",//3// 
                             "",// =>4
                             "Remember me",//5
                             "Sign in automatically",//6
                             "Sign in as invisible",//7
                             "",//8//jarak untuk tombol login
                             "Sign In"};//9
    private String[] footerlclp = {"Menu","OK"};
    private String[] footerrclp = {null,"Cancel"};
    private Footer footer;//komponen custom footer
    private PopUpMenu leftPopUp;
    private int topFont;//, centerDraw, maxWidth;
    private boolean[] bLogin;
    private String password;
    private String[] errorMessage;
    private boolean showErrorMessage;
    private boolean showLoginProcess;
    private String processMessage;

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param font
     * @param scrollable
     * @param bLogin
     * @param footer
     */
    public LoginP(int x, int y, int width, int height, Scrollable scrollable,boolean[] bLogin, Footer footer) {
        super(x, y, width, height, Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        this.scrollable = scrollable;
        this.bLogin = bLogin;
        this.textWidth = width - 14;
        this.footer = footer;
        //maxWidth = width - 6;
        lineHeight = font.getHeight()+2;
        if(lineHeight<15){//15 di dapat dari ukuran icon ??
            lineHeight=15;
        }
        visibleLines = (height-2) / lineHeight;
        int usepixels= (height-(visibleLines*lineHeight));
        topFont = usepixels/2;
        //centerDraw = topFont+(lineHeight/2);
        leftPopUp = new PopUpMenu(x,y,width,height,font);
        leftPopUp.setContent(contentLeftPopUp);
        this.footer.setLeftLabel(footerlclp[0]);
        this.footer.setRightLabel(footerrclp[0]);

    }

    public void showLoginProcess(String processMessage) {
        this.processMessage = processMessage;
        showLoginProcess=true;
    }

    public void showErrorMessage(String processMessage) {
        TextWrap tw = new TextWrap();
        errorMessage = tw.wrapText(width-16-20-10, height, processMessage, font);
        showErrorMessage=true;
    }
    /**
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        // update scrollable:
        scrollable.setScroll(listLogin.length-4, scrollbarSelection);//0,2,7
        scrollable.paint(g);
        if(!leftPopUp.getVisible()){
            footer.setLeftLabel(footerlclp[0]);
            footer.setRightLabel(footerrclp[0]);
        }else if(leftPopUp.getVisible()){
            footer.setLeftLabel(footerlclp[1]);
            footer.setRightLabel(footerrclp[1]);
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
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        // draw each friend:
        //int yahooUserSize = vYahooUser.size();
        synchronized(this) {
            int start = 0;
            int end = listLogin.length;
            if(end>visibleLines) {
                start = selection - visibleLines / 2;
                if(start + visibleLines > listLogin.length) {
                    // selection is near end:
                    start = listLogin.length - visibleLines;
                }
                if(start<0){
                    start = 0;
                }
                end = start + visibleLines;
            }
            //Font bold = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            for(int i=start; i<end; i++) {
                /*Khusus untuk login panel dirubah highlightnya*/
                if(i==selection) {
                    //if selection == TextField special FX dunk ^^
                    /*g.setColor(COLOR_HIGHLIGHTED_BACKGROUND);
                    g.fillRect(lineHeight+2, lineHeight*(i-start)+topFont, textWidth, lineHeight);
                    g.setColor(COLOR_FOREGROUND);*/
                    g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BACKGROUND);
                    if(i==1 || i==3){//Tipe Highlight untuk frame dibuat beda
                    }
                    else if(i==listLogin.length-1){
                        g.fillRect(width/2-(font.stringWidth(listLogin[i])/2), lineHeight*(i-start)+topFont, font.stringWidth(listLogin[i]), lineHeight);
                    }else
                        g.fillRect(lineHeight+2, lineHeight*(i-start)+topFont, font.stringWidth(listLogin[i]), lineHeight);
                    g.setColor(MainCanvas.COLOR_FOREGROUND);
                }
                else {
                    g.setColor(MainCanvas.COLOR_FOREGROUND);
                }
                if(i==0 || i==2){//Draw String Saja
                    g.drawString(listLogin[i], 2, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                }else if(i==1 || i==3){//untuk penggambaran Text Field
                    //menggambarkan textfield ke layar
                    if(i!=selection){
                        g.setColor(MainCanvas.COLOR_BACKGROUND);//
                        g.fillRect(lineHeight+2, lineHeight*(i-start)+topFont, textWidth, lineHeight);
                        g.setColor(255,255,255);//Putih background TextField
                        g.fillRect(10, lineHeight*(i-start)+topFont-2, width-20, lineHeight);
                        if(listLogin[i]!=null){
                            g.setFont(font);
                            g.setColor(MainCanvas.COLOR_FOREGROUND);
                            g.drawString(listLogin[i], 12, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
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
                        if(listLogin[i]!=null){
                            g.setFont(font);
                            g.setColor(0,0,0);
                            g.drawString(listLogin[i], 12, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                        }
                        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BORDER);//BORDER
                        g.drawRect(9, lineHeight*(i-start)+topFont-1, width-18, lineHeight-2);
                        g.drawRect(10, lineHeight*(i-start)+topFont-2, width-20, lineHeight);
                        g.setColor(MainCanvas.COLOR_BACKGROUND);
                        //Bersihkan sebelah kiri
                        g.fillRect(0, lineHeight*(i-start)+topFont, 8, lineHeight);
                        //bersihkan sebelah kanan
                        g.fillRect(width-8, lineHeight*(i-start)+topFont, width-(width-9), lineHeight);
                        //hanya garis
                        //g.setColor(255,255,255);//hitam
                       // g.drawLine(width-11, lineHeight*(i-start)+topFont, width-11,lineHeight*(i-start)+topFont+lineHeight-3);
                    }
                }else if(i==5 || i==6 ||i==7 ){//login settings
                    /*Tolong nanti dirubah antara drawRec*/
                    g.setColor(MainCanvas.COLOR_BORDER);
                    g.drawRect(2, lineHeight*(i-start)+topFont, lineHeight-2, lineHeight-2);
                    g.setColor(MainCanvas.COLOR_BORDER);
                    g.drawRect(3, lineHeight*(i-start)+topFont+1, lineHeight-4, lineHeight-4);
                    g.setColor(MainCanvas.COLOR_FOREGROUND);
                    if(bLogin[i-5]){
                        g.drawLine(3, lineHeight*(i-start)+topFont+1, 3+lineHeight-4, lineHeight*(i-start)+topFont+1+lineHeight-4);
                        g.drawLine(3, lineHeight*(i-start)+topFont+1+lineHeight-4, 3+lineHeight-4, lineHeight*(i-start)+topFont+1);
                    }
                    if(i==selection){
                        //line heighit itu kotaknya.. ^^
                        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_FOREGROUND);
                        g.drawString(listLogin[i], lineHeight+2, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                    }else
                        g.drawString(listLogin[i], lineHeight+2, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                }else if(i==4 || i==8){
                    g.drawString(listLogin[i], 2, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                }else if(i==9){
                    if(i==selection){
                        //line heighit itu kotaknya.. ^^
                        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_FOREGROUND);
                        g.drawString(listLogin[i], this.width/2, lineHeight*(i-start)+topFont, Graphics.HCENTER|Graphics.TOP);
                    }else
                        g.drawString(listLogin[i], this.width/2, lineHeight*(i-start)+topFont, Graphics.HCENTER|Graphics.TOP);
                }
            }
        }
        if(showLoginProcess){//dipaint tapi, pada waktu meminta network access show login process menjadi false lagi maka daripada itu yang kedua ditampilkan normalll
            g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BACKGROUND);
            g.fillRect(8, height/2 - lineHeight, width-16, lineHeight*3);
            g.setColor(MainCanvas.COLOR_BORDER);
            g.drawRect(8, height/2 - lineHeight, width-16, lineHeight*3);
            g.drawImage(MainCanvas.imgStatus[9], width/2-font.stringWidth(processMessage)/2-10, height/2, Graphics.HCENTER|Graphics.TOP);
            g.setColor(MainCanvas.COLOR_HIGHLIGHTED_FOREGROUND);
            g.drawString(processMessage, width/2+10, height/2, Graphics.HCENTER|Graphics.TOP);
            showLoginProcess=false;
        }
        else if(showErrorMessage){//perbaiki cara paintnnya...
            //pm = "Connection Closed";
            int popUpHeight, topPopUp;
            g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BACKGROUND);
            if(errorMessage.length==1){
                popUpHeight = 3 * lineHeight;
                g.fillRect(8, height/2 - lineHeight, width-16, lineHeight*3);
                g.setColor(MainCanvas.COLOR_BORDER);
                g.drawRect(8, height/2 - lineHeight, width-16, lineHeight*3);
                g.drawImage(MainCanvas.imgStatus[10], width/2-font.stringWidth(errorMessage[0])/2-10, height/2, Graphics.HCENTER|Graphics.TOP);
                g.setColor(MainCanvas.COLOR_HIGHLIGHTED_FOREGROUND);
                g.drawString(errorMessage[0], width/2+10, height/2, Graphics.HCENTER|Graphics.TOP);
            }
            else{
                popUpHeight = errorMessage.length * lineHeight;
                topPopUp = height/2 - popUpHeight/2;
                g.fillRect(8, topPopUp, width-16, popUpHeight);
                g.setColor(MainCanvas.COLOR_BORDER);
                g.drawRect(8, topPopUp, width-16, popUpHeight);
                g.drawImage(MainCanvas.imgStatus[10], 18, height/2, Graphics.HCENTER|Graphics.VCENTER);
                g.setColor(MainCanvas.COLOR_HIGHLIGHTED_FOREGROUND);
                for(int i=0; i<errorMessage.length;i++)
                   g.drawString(errorMessage[i], 28, lineHeight*i + topPopUp, Graphics.LEFT|Graphics.TOP);
            }
            showErrorMessage=false;//seharusnya ini di false di tombol
        }
    }
    /**
     *
     * @return
     */
    public int getMenuIndexSelected() {
        if(selection>0 && selection<listLogin.length){
            return selection;
            }
        return (-1);
    }
    /**
     *
     * @param iContent
     * @return
     */
    public boolean settingsChanged(int iContent) {
        bLogin[iContent]=!bLogin[iContent];        
        return true;
    }
    /**
     *
     * @return
     */
    public synchronized boolean moveUp() {
        if(selection==(-1))
            selection=listLogin.length;
        if(selection>1) {
            if(selection==3){
                selection -=2;
                scrollbarSelection--;
            }else if(selection==5){
                selection -=2;
                scrollbarSelection--;
            }else if(selection==9){
                selection -=2;
                scrollbarSelection--;
            }else{
                selection--;
                scrollbarSelection--;
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
        if(selection<listLogin.length-1) {
            if(selection==(-1)){
                selection=1;
                scrollbarSelection++;
            }else if(selection==1){
                selection +=2;
                scrollbarSelection++;
            }else if(selection==3){
                selection +=2;
                scrollbarSelection++;
            }else if(selection==7){
                selection +=2;
                scrollbarSelection++;
            }else{
                selection++;
                scrollbarSelection++;
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
        if(!leftPopUp.getVisible())
            return footerlclp[0];
        else
            return footerlclp[1];
    }
    /**
     *
     * @return
     */
    public String getCurrentFooterRight(){
        if(!leftPopUp.getVisible())
            return footerrclp[0];
        else
            return footerrclp[1];
    }
    /**
     *
     */
    public void setPopUpSelectedToDefault(){
        this.leftPopUp.setSelectedToDefaultIndex();
    }
    /**
     *
     * @return
     */
    public boolean getVisibleLCLoginPanel(){
       return leftPopUp.getVisible();
    }
    /**
     *
     * @param showLeftPopUp
     * @return
     */
    public boolean setVisibleLCLoginPanel(boolean showLeftPopUp){
       leftPopUp.setVisible(showLeftPopUp);
       return true;
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
    public String getUsername(){
        return listLogin[1];
    }
    /**
     *
     * @return
     */
    public String getPassword(){
        return listLogin[3];
    }
    /**
     *
     * @param username
     */
    public void setUsername(String username){
        listLogin[1] = username;
    }
    /**
     *
     * @param password
     */
    public void setPassword(String password){
        this.password = password;
        int pLength = password.length();
        char[] pTmp = new char[pLength];
        for(int i=0;i<pLength;i++){
            pTmp[i]='*';
        }
        listLogin[3] = new String(pTmp);
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
}
