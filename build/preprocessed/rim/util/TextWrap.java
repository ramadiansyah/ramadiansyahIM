package rim.util;

import java.util.Vector;
import javax.microedition.lcdui.Font;




//membaca file yang ada di direktori res
public class TextWrap{
	private int page = 0;// halaman
	private int npage = 0; //jumlah halaman
	private int length = 0; //panjang huruf dalam teks
    private Font font;
    private int maxWidth;
	public TextWrap() {
		//kosntruktor
	}

	//kelemahannya di sini adalah belum bisa memecah text yang terlalu panjang tanpa spasi
	public String[] wrapText(int width, int height, String inputText, Font font){
		//memproses teks di dalam file agar ditampilkan dengan enter di midlet dimana width dan height adalah lebar dan tinggi layar untuk menampilkan tulisan
		length = 0;
		npage = 0;
		page = 0;
        maxWidth = width;
        this.font = font;
		String[] result = null;
			String text = inputText;
			int textheight = font.getHeight()+2;
			//memisahkan teks berdasarkan spasi
			String[] textsplit = split(text, " ");
			//memproses teks
			java.util.Vector nodes = new java.util.Vector();
			int i = 0;
			String str = "";
			int len = 0;
			int index = 0;
			while(i < textsplit.length){
				//System.out.println(str);
				if(textsplit[i].length() > 0){
					if((textsplit[i].charAt(textsplit[i].length()-1) == '\n')
					||(textsplit[i].charAt(textsplit[i].length()-1) == '\r')){
						if(i == (textsplit.length-1)){
							nodes.addElement(str);
							index++;
						}else{
							nodes.addElement(str);
							index++;
							str = textsplit[i].substring(0, textsplit[i].length()-1);
							len = font.stringWidth(str);
						}
					}else if((textsplit[i].charAt(0) == '\n')||(textsplit[i].charAt(0) == '\r')){
						if(i == (textsplit.length-1)){
							nodes.addElement(str);
							index++;
							str = textsplit[i].substring(1, textsplit[i].length());
							nodes.addElement(str);
							index++;
						}else{
							nodes.addElement(str);
							index++;
							str = textsplit[i].substring(1, textsplit[i].length());
							len = font.stringWidth(str);
						}
					}else if(textsplit[i].charAt(0) == '#'){
						if(i == (textsplit.length-1)){
							nodes.addElement(str);
							index++;
							str = textsplit[i].substring(1, textsplit[i].length());
							nodes.addElement(str);
							index++;
						}else{
							nodes.addElement(str);
							str = textsplit[i].substring(1, textsplit[i].length());
							len = font.stringWidth(str);
							index++;
						}
					}else if(len <= width){
						// jika ada di tengah
						if(i == 0){
							len = font.stringWidth(textsplit[i]);
							str = textsplit[i];
							if(i == (textsplit.length-1)){
								nodes.addElement(str);
								index++;
							}
						}else if(i == (textsplit.length-1)){						
							len = len + 1 + font.stringWidth(textsplit[i]);
							if(len > width){
								nodes.addElement(str);
								index++;
								str = textsplit[i];
							}else{
								str = str + " " + textsplit[i];
							}
							nodes.addElement(str);
							index++;
						}else{
							int temp = len + 1 + font.stringWidth(textsplit[i]);
							if(temp > width){
								nodes.addElement(str);
								str = textsplit[i];
								len = font.stringWidth(str);
								index++;
							}else{
								len = len + 1 + font.stringWidth(textsplit[i]);
								str = str + " " + textsplit[i];
							}
						}
					}else if(i == (textsplit.length-1)){
						len = len + 1 + font.stringWidth(textsplit[i]);
						if(len > width){
							nodes.addElement(str);
							index++;
							str = textsplit[i];
						}else{
							str = str + " " + textsplit[i];
						}
						nodes.addElement(str);
						index++;
					}else{
						nodes.addElement(str);
						len = font.stringWidth(textsplit[i]);
						str = textsplit[i];
						index++;
					}
				}
				i++;
			}
			String[] tmp = new String[ nodes.size() ];
		    if( nodes.size()>0 ) {
				int idx = 0;
		        for(int loop=0; loop<nodes.size(); loop++){
					str = (String)nodes.elementAt(loop);
					if(str.length() > 0){
						tmp[idx] = (String)nodes.elementAt(loop);
						idx++;
					}
				}
				result = new String[idx];
				for(int k=0; k<idx; k++){
					result[k] = tmp[k];
				}
		    }
			
			length = index;
			npage = height / textheight;
			page = index / npage;
			if((page*npage) < index){
				page++;
			}
			tmp = null;
			nodes = null;
		
        //Manipulasi di sini
        //if(result!=null){
          //  Vector resultCopy =  new Vector(result.length);
            //for(int c=0;c<result.length;c++){
              //  resultCopy.addElement(result[c]);
            //}
       // }
        

		return result;
	}
	
	
	public int getLength(){
		//mengembalikan length
		return length;
	}
	
	public int getnpage(){
		// jumlah kolom per halaman
		return npage;
	}
	
	public int getpage(){
		// jumlah halaman
		return page;
	}


    /*
     public synchronized void addReceivedMessage(String name, String text) {
        Vector v = splitMessage(text);
        if(!this.searchUserMessage(name)){
            this.mvID = new MessageVector(name);
        }
        synchronized(this) {
            if(!name.equals(mvID.getLastEmail())) {

                Vector lines = splitLongString(name + ":");
                addAll(mvID, lines);
                mvID.setLastEmail(name);
            }
            addAll(mvID, v);
            mvID.removeOld();
            if(start==(-1))
                start = 0;
        }
        mvID.setOpened(false);
        allMessages.addElement(mvID);
    }
     */

	private String[] split(String original, String separator) {
		//melakukan split pada teks
	    java.util.Vector nodes = new java.util.Vector();
	    int index = original.indexOf(separator);
	    while(index>=0) {
	        nodes.addElement( original.substring(0, index) );
	        original = original.substring(index+separator.length());
	        index = original.indexOf(separator);
	    }
		//mengambil simpul terakhir
	    nodes.addElement(original);
		int len = 0;
		String[] result = null;
	    if( nodes.size()>0 ) {			
            String[] tmp = new String[ nodes.size()*2];//Not Efficient
            //Dhitung berapa banyak sting yang mungkin akan dipecah..
	        for(int loop=0; loop<nodes.size(); loop++){
				String str = (String)nodes.elementAt(loop);
                if(str.length() > 0){
                    if(font.stringWidth(str) >maxWidth){
                        Vector v = new Vector();
                        v = splitLongString(str);
                            for(int tgp=0;tgp<v.size();tgp++){
                                tmp[len] = (String)v.elementAt(tgp);
                                len++;
                            }
                        v = null;
                    }
                    else{
                        tmp[len] = str;
                        len++;
                    }
				}
			}
			result = new String[len];
			for(int i=0; i<len; i++){
				result[i] = tmp[i];
			}
			tmp = null;
			nodes = null;
	    }
	    return result;
	}

    /*Sampai sini..*/

    //split string and return result in vector
    private Vector splitLongString(String text) {
        Vector v = new Vector();
        split(text, v);
        return v;
    }

    private void split(String text, Vector toAdd) {
        //System.out.println("-- split: " + text);
        String leftPart = left(text, maxWidth);
        if(leftPart==null) {
            //System.out.println(">>" + text);
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

    protected String left(String s, int max) {
        int s_width = font.stringWidth(s);
        if(s_width<=max)
            return null;
        char[] cs = s.toCharArray();
        int estimate = cs.length;
        int ax = 0;
        do {
            estimate = estimate / 2;
            ax = font.charsWidth(cs, 0, estimate);
        }
        while(ax>max);
        // now [0-estimate) is less than s_max:
        for(int i=estimate; i<cs.length; i++) {
            ax += font.charWidth(cs[i]);
            if(ax>max) {
                // found!
                return new String(cs, 0, i-1);
            }
        }
        // there must be logic error in this method:
        return "?";
    }

}