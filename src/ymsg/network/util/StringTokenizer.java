//The string tokenizer class allows an application to break a string into tokens. 
//The tokenization method is much simpler than the one used by the StreamTokenizer class. 
//The StringTokenizer methods do not distinguish among identifiers, numbers, and quoted strings, 
//nor do they recognize and skip comments. 

package ymsg.network.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 *
 * @author 7406030021
 */
public class StringTokenizer implements Enumeration{
    /**
     *
     */
    protected String text;
    /**
     *
     */
    protected int strLength;
    /**
     *
     */
    protected String nontokenDelims;
    /**
     *
     */
    protected String tokenDelims;
    /**
     *
     */
    protected int position;
    /**
     *
     */
    protected boolean emptyReturned;
    /**
     *
     */
    protected char maxDelimChar;
    /**
     *
     */
    protected boolean returnEmptyTokens;
    /**
     *
     */
    protected int delimsChangedPosition;
    /**
     *
     */
    protected int tokenCount;
    /**
     *
     * @param text
     * @param nontokenDelims
     * @param tokenDelims
     */
    public StringTokenizer(String text, String nontokenDelims, String tokenDelims){
        this(text, nontokenDelims, tokenDelims, false);
    }
    /**
     *
     * @param text
     * @param nontokenDelims
     * @param tokenDelims
     * @param returnEmptyTokens
     */
    public StringTokenizer(String text, String nontokenDelims, String tokenDelims, boolean returnEmptyTokens){
        setDelims(nontokenDelims, tokenDelims);
        setText(text);
        setReturnEmptyTokens(returnEmptyTokens);
    }
    /**
     *
     * @param text
     * @param delims
     * @param delimsAreTokens
     */
    public StringTokenizer(String text, String delims, boolean delimsAreTokens){
        this(text, delimsAreTokens ? null : delims, delimsAreTokens ? delims : null);
    }
    /**
     *
     * @param text
     * @param nontokenDelims
     */
    public StringTokenizer(String text, String nontokenDelims){
        this(text, nontokenDelims, ((String) (null)));
    }
    /**
     *
     * @param text
     */
    public StringTokenizer(String text){
        this(text, " \t\n\r\f", ((String) (null)));
    }
    /**
     *
     * @param text
     */
    public void setText(String text){
        if(text == null){
            throw new NullPointerException();
        } else{
            this.text = text;
            strLength = text.length();
            emptyReturned = false;
            position = strLength <= 0 ? -1 : 0;
            delimsChangedPosition = 0;
            tokenCount = -1;
            return;
        }
    }
    private void setDelims(String nontokenDelims, String tokenDelims){
        this.nontokenDelims = nontokenDelims;
        this.tokenDelims = tokenDelims;
        delimsChangedPosition = position == -1 ? strLength : position;
        maxDelimChar = '\0';
        for(int i = 0; nontokenDelims != null && i < nontokenDelims.length(); i++)
            if(maxDelimChar < nontokenDelims.charAt(i))
                maxDelimChar = nontokenDelims.charAt(i);

        for(int i = 0; tokenDelims != null && i < tokenDelims.length(); i++)
            if(maxDelimChar < tokenDelims.charAt(i))
                maxDelimChar = tokenDelims.charAt(i);

        tokenCount = -1;
    }
    /**
     *
     * @return
     */
    public boolean hasMoreTokens(){
        if(tokenCount == 0)
            return false;
        if(tokenCount > 0)
            return true;
        int savedPosition = position;
        boolean savedEmptyReturned = emptyReturned;
        int workingPosition = position;
        boolean workingEmptyReturned = emptyReturned;
        for(boolean onToken = advancePosition(); position != workingPosition || emptyReturned != workingEmptyReturned; onToken = advancePosition()){
            if(onToken){
                position = savedPosition;
                emptyReturned = savedEmptyReturned;
                return true;
            }
            workingPosition = position;
            workingEmptyReturned = emptyReturned;
        }

        position = savedPosition;
        emptyReturned = savedEmptyReturned;
        return false;
    }
    /**
     *
     * @return
     */
    public String nextToken(){
        int workingPosition = position;
        boolean workingEmptyReturned = emptyReturned;
        for(boolean onToken = advancePosition(); position != workingPosition || emptyReturned != workingEmptyReturned; onToken = advancePosition()){
            if(onToken){
                tokenCount--;
                return emptyReturned ? "" : text.substring(workingPosition, position == -1 ? strLength : position);
            }
            workingPosition = position;
            workingEmptyReturned = emptyReturned;
        }
        throw new NoSuchElementException();
    }
    /**
     *
     * @return
     */
    public boolean skipDelimiters(){
        int workingPosition = position;
        boolean workingEmptyReturned = emptyReturned;
        boolean onToken = advancePosition();
        tokenCount = -1;
        while(position != workingPosition || emptyReturned != workingEmptyReturned) {
            if(onToken){
                position = workingPosition;
                emptyReturned = workingEmptyReturned;
                return true;
            }
            workingPosition = position;
            workingEmptyReturned = emptyReturned;
            onToken = advancePosition();
        }
        return false;
    }
    /**
     *
     * @return
     */
    public int countTokens(){
        if(this.tokenCount >= 0)
            return this.tokenCount;
        int tokenCount = 0;
        int savedPosition = position;
        boolean savedEmptyReturned = emptyReturned;
        int workingPosition = position;
        boolean workingEmptyReturned = emptyReturned;
        for(boolean onToken = advancePosition(); position != workingPosition || emptyReturned != workingEmptyReturned; onToken = advancePosition()){
            if(onToken)
                tokenCount++;
            workingPosition = position;
            workingEmptyReturned = emptyReturned;
        }
        position = savedPosition;
        emptyReturned = savedEmptyReturned;
        this.tokenCount = tokenCount;
        return tokenCount;
    }
    /**
     *
     * @param delims
     */
    public void setDelimiters(String delims){
        setDelims(delims, null);
    }
    /**
     *
     * @param delims
     * @param delimsAreTokens
     */
    public void setDelimiters(String delims, boolean delimsAreTokens){
        setDelims(delimsAreTokens ? null : delims, delimsAreTokens ? delims : null);
    }
    /**
     *
     * @param nontokenDelims
     * @param tokenDelims
     */
    public void setDelimiters(String nontokenDelims, String tokenDelims){
        setDelims(nontokenDelims, tokenDelims);
    }
    /**
     *
     * @param nontokenDelims
     * @param tokenDelims
     * @param returnEmptyTokens
     */
    public void setDelimiters(String nontokenDelims, String tokenDelims, boolean returnEmptyTokens){
        setDelims(nontokenDelims, tokenDelims);
        setReturnEmptyTokens(returnEmptyTokens);
    }
    /**
     *
     * @param delims
     * @return
     */
    public int countTokens(String delims){
        setDelims(delims, null);
        return countTokens();
    }
    /**
     *
     * @param delims
     * @param delimsAreTokens
     * @return
     */
    public int countTokens(String delims, boolean delimsAreTokens){
        setDelims(delimsAreTokens ? null : delims, delimsAreTokens ? delims : null);
        return countTokens();
    }
    /**
     *
     * @param nontokenDelims
     * @param tokenDelims
     * @return
     */
    public int countTokens(String nontokenDelims, String tokenDelims){
        setDelims(nontokenDelims, tokenDelims);
        return countTokens();
    }
    /**
     *
     * @param nontokenDelims
     * @param tokenDelims
     * @param returnEmptyTokens
     * @return
     */
    public int countTokens(String nontokenDelims, String tokenDelims, boolean returnEmptyTokens){
        setDelims(nontokenDelims, tokenDelims);
        setReturnEmptyTokens(returnEmptyTokens);
        return countTokens();
    }
    private boolean advancePosition(){
        if(returnEmptyTokens && !emptyReturned && (delimsChangedPosition == position || position == -1 && strLength == delimsChangedPosition)){
            if(strLength == delimsChangedPosition){
                emptyReturned = true;
                return true;
            }
            char c = text.charAt(position);
            if(c <= maxDelimChar && nontokenDelims != null && nontokenDelims.indexOf(c) != -1 || tokenDelims != null && tokenDelims.indexOf(c) != -1){
                emptyReturned = true;
                return true;
            }
        }
        if(position != -1){
            char c = text.charAt(position);
            if(returnEmptyTokens && !emptyReturned && position > delimsChangedPosition){
                char c1 = text.charAt(position - 1);
                if(c <= maxDelimChar && c1 <= maxDelimChar && (nontokenDelims != null && nontokenDelims.indexOf(c) != -1 || tokenDelims != null && tokenDelims.indexOf(c) != -1) && (nontokenDelims != null && nontokenDelims.indexOf(c1) != -1 || tokenDelims != null && tokenDelims.indexOf(c1) != -1)){
                    emptyReturned = true;
                    return true;
                }
            }
            int nextDelimiter = position >= strLength - 1 ? -1 : indexOfNextDelimiter(position + 1);
            if(c > maxDelimChar || (nontokenDelims == null || nontokenDelims.indexOf(c) == -1) && (tokenDelims == null || tokenDelims.indexOf(c) == -1)){
                position = nextDelimiter;
                emptyReturned = false;
                return true;
            }
            if(tokenDelims != null && tokenDelims.indexOf(c) != -1){
                emptyReturned = false;
                position = position >= strLength - 1 ? -1 : position + 1;
                return true;
            } else{
                emptyReturned = false;
                position = position >= strLength - 1 ? -1 : position + 1;
                return false;
            }
        }
        if(returnEmptyTokens && !emptyReturned && strLength > 0){
            char c = text.charAt(strLength - 1);
            if(c <= maxDelimChar && nontokenDelims != null && nontokenDelims.indexOf(c) != -1 || tokenDelims != null && tokenDelims.indexOf(c) != -1){
                emptyReturned = true;
                return true;
            }
        }
        return false;
    }
    /**
     *
     * @param nontokenDelims
     * @param tokenDelims
     * @return
     */
    public String nextToken(String nontokenDelims, String tokenDelims){
        setDelims(nontokenDelims, tokenDelims);
        return nextToken();
    }
    /**
     *
     * @param nontokenDelims
     * @param tokenDelims
     * @param returnEmptyTokens
     * @return
     */
    public String nextToken(String nontokenDelims, String tokenDelims, boolean returnEmptyTokens){
        setDelims(nontokenDelims, tokenDelims);
        setReturnEmptyTokens(returnEmptyTokens);
        return nextToken();
    }
    /**
     *
     * @param delims
     * @param delimsAreTokens
     * @return
     */
    public String nextToken(String delims, boolean delimsAreTokens){
        return delimsAreTokens ? nextToken(null, delims) : nextToken(delims, ((String) (null)));
    }
    /**
     *
     * @param nontokenDelims
     * @return
     */
    public String nextToken(String nontokenDelims){
        return nextToken(nontokenDelims, ((String) (null)));
    }
    private int indexOfNextDelimiter(int start){
        char c;
        int next;
        for(next = start; (c = text.charAt(next)) > maxDelimChar || (nontokenDelims == null || nontokenDelims.indexOf(c) == -1) && (tokenDelims == null || tokenDelims.indexOf(c) == -1); next++)
            if(next == strLength - 1)
                return -1;
        return next;
    }
    /**
     *
     * @return
     */
    public boolean hasMoreElements(){
        return hasMoreTokens();
    }
    /**
     *
     * @return
     */
    public Object nextElement(){
        return nextToken();
    }
    /**
     *
     * @return
     */
    public boolean hasNext(){
        return hasMoreTokens();
    }
    /**
     *
     * @return
     */
    public Object next(){
        return nextToken();
    }
    /**
     *
     * @param returnEmptyTokens
     */
    public void setReturnEmptyTokens(boolean returnEmptyTokens){
        tokenCount = -1;
        this.returnEmptyTokens = returnEmptyTokens;
    }
    /**
     *
     * @return
     */
    public int getCurrentPosition(){
        return position;
    }
    /**
     *
     * @return
     */
    public String[] toArray(){
        String tokenArray[] = new String[countTokens()];
        for(int i = 0; hasMoreTokens(); i++)
            tokenArray[i] = nextToken();
        return tokenArray;
    }
    /**
     *
     * @return
     */
    public String restOfText(){
        return nextToken(null, ((String) (null)));
    }
    /**
     *
     * @return
     */
    public String peek(){
        int savedPosition = position;
        boolean savedEmptyReturned = emptyReturned;
        int savedtokenCount = tokenCount;
        String retval = nextToken();
        position = savedPosition;
        emptyReturned = savedEmptyReturned;
        tokenCount = savedtokenCount;
        return retval;
    }
}