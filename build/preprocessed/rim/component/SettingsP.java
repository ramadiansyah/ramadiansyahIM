package rim.component;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import rim.MainCanvas;
/**
 *
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */
public class SettingsP extends ComponentUI {
    //private static Session yahooMessengerSession;
    private int lineHeight;
    private int visibleLines;
    private int textWidth;
    private int selection = (-1);
    private int scrollbarSelection = (-1);
    private Scrollable scrollable;
    private String[] listSettings = {"Alert when receive settings :",
                                "Play sound",
                                "Vibrate",
                                "Flash backlight"
                                };
    private String footerLeftCmd = "OK";
    private String footerRightCmd = "Cancel";
    private Footer footer;
    private int topFont;//, centerDraw;
    private boolean[] bSettings;
    private boolean[] currentSettings;
        
    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param font
     * @param scrollable
     * @param settings
     * @param footer
     */
    public SettingsP(int x, int y, int width, int height, Font font, Scrollable scrollable,boolean[] settings, Footer footer) {
        super(x, y, width, height, font);
        this.scrollable = scrollable;
        this.bSettings = settings;
        this.currentSettings = settings;
        this.textWidth = width - 14;
        this.footer = footer;
        lineHeight = font.getHeight()+2;
        if(lineHeight<15){
            lineHeight=15;
        }
        visibleLines = (height-2) / lineHeight;
        int usepixels= (height-(visibleLines*lineHeight));
        topFont = usepixels/2;
        //centerDraw = topFont+(lineHeight/2);

        
    }
    
    /**
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        // update scrollable:
        scrollable.setScroll(listSettings.length-1, scrollbarSelection);
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
            int end = listSettings.length;
            if(end>visibleLines) {
                start = selection - visibleLines / 2;
                if(start + visibleLines > listSettings.length) {
                    // selection is near end:
                    start = listSettings.length - visibleLines;
                }
                if(start<0){
                    start = 0;
                }
                end = start + visibleLines;
            }
            Font bold = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            for(int i=start; i<end; i++) {
                if(i==selection) {
                    g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BACKGROUND);
                    g.fillRect(lineHeight+2, lineHeight*(i-start)+topFont, textWidth, lineHeight);
                    g.setColor(MainCanvas.COLOR_FOREGROUND);
                }
                else {
                    g.setColor(MainCanvas.COLOR_FOREGROUND);
                }
                if(i==0){
                    g.drawString(listSettings[i], 2, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                }else{
                    g.setColor(MainCanvas.COLOR_BORDER);
                    g.drawRect(2, lineHeight*(i-start)+topFont, lineHeight-2, lineHeight-2);
                    g.setColor(MainCanvas.COLOR_BORDER);
                    g.drawRect(3, lineHeight*(i-start)+topFont+1, lineHeight-4, lineHeight-4);
                    g.setColor(MainCanvas.COLOR_FOREGROUND);
                    if(bSettings[i-1]){
                        g.drawLine(3, lineHeight*(i-start)+topFont+1, 3+lineHeight-4, lineHeight*(i-start)+topFont+1+lineHeight-4);
                        g.drawLine(3, lineHeight*(i-start)+topFont+1+lineHeight-4, 3+lineHeight-4, lineHeight*(i-start)+topFont+1);
                    }
                    if(i==selection){
                        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_FOREGROUND);
                        g.drawString(listSettings[i], lineHeight+2, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                    }else
                        g.drawString(listSettings[i], lineHeight+2, lineHeight*(i-start)+topFont, Graphics.LEFT|Graphics.TOP);
                }
            }
        }
    }
    /**
     *
     * @return
     */
    public String getSelectedSettings() {
        if(selection>0 && selection<listSettings.length){
            return (String) listSettings[selection];
            }
        return null;
    }
    /**
     *
     * @param iContent
     * @return
     */
    public boolean settingsChanged(int iContent) {
        bSettings[iContent]=!bSettings[iContent];
        return true;
    }
    /**
     *
     * @param bSettings
     */
    public void setCurrentSettings(boolean[] bSettings) {
        currentSettings = new boolean[] {bSettings[0], bSettings[1], bSettings[2]};
        this.bSettings = bSettings;
    }
    
    /**
     *
     * @return
     */
    public synchronized boolean moveUp() {
        if(selection==(-1))
            selection=listSettings.length;
        if(selection>1) {
            selection--;
            scrollbarSelection--;
            return true;
        }
        return false;
    }
    /**
     *
     * @return
     */
    public synchronized boolean moveDown() {
        if(selection==(-1))
            selection=0;
        if(selection<listSettings.length-1) {
            selection++;
            scrollbarSelection++;
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
     * @return
     */
    public boolean[] getSettingsChanges() {
        return bSettings;
    }
    /**
     *
     * @return
     */
    public boolean[] getCurrentSettings(){
        bSettings = currentSettings;
        return bSettings;
    }
}
