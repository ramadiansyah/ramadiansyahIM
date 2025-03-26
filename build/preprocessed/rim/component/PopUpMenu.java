package rim.component;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import rim.MainCanvas;

/**
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */

public class PopUpMenu extends ComponentUI{

    private String content[];
    private int fontHeight, ymenu, index, selected, textlength, xPopUp, yPopUp, heightPopUp ;
    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param font
     */
    public PopUpMenu(int x, int y, int width, int height, Font font){
        super(x, y, width, height, font);
        this.xPopUp = x;
		this.yPopUp = y;
        this.heightPopUp=height;
        fontHeight = font.getHeight();
        textlength=0;
	}
    /**
     *
     * @return
     */
    public int getHeight() {
		return fontHeight + 2;
	}
    /**
     *
     * @param contentMenu
     */
    public void setContent(String[] contentMenu){
		this.content = contentMenu;
        ymenu = heightPopUp-(fontHeight*contentMenu.length)-2;
        textlength=0;
        for(int i=0;i<contentMenu.length;i++){
			if(textlength < contentMenu[i].length()){
				textlength = contentMenu[i].length();
				index = i;
			}
		}
	}
    /**
     *
     * @return
     */
    public String getSelectedMenu(){
        return content[selected];
    }

    /**
     *
     * @return
     */
    public boolean moveUp() {
        if(selected == 0){
            selected = content.length-1;
        }else{
            selected--;
        }
        return true;
    }
    /**
     *
     * @return
     */
    public boolean moveDown() {
        if(selected == content.length-1){
            selected = 0;
        }else{
            selected++;
        }
        return true;
    }

    /**
     *
     */
    public void setSelectedToDefaultIndex(){
        selected=0;
    }

    /**
     *
     * @param g
     */
    protected void paintInternal(Graphics g) {
		g.setColor(MainCanvas.COLOR_BORDER);
        g.fillRect(xPopUp, ymenu, font.stringWidth(content[index])+7, (fontHeight*content.length)+2);
		g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(xPopUp+1, ymenu+1, font.stringWidth(content[index])+5, fontHeight*content.length);
        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BACKGROUND);
		g.fillRect(xPopUp+1, ymenu+(fontHeight*selected)+1, font.stringWidth(content[index])+5, fontHeight);
		g.setColor(MainCanvas.COLOR_FOREGROUND);
		for(int i=0;i<content.length;i++){
			g.drawString(content[i], xPopUp+3, ymenu+1 +(i * fontHeight), Graphics.TOP|Graphics.LEFT);
		}
    }
}
