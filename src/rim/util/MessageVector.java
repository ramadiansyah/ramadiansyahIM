/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rim.util;
import java.util.Vector;

/**
 *
 * @author 7406030021
 */
public class MessageVector extends Vector{
    
    /*Message di dalam vector ini*/
    private String id;
    private String lastEmail;
    private int MAX_MESSAGES = 100;
    private boolean opened;//hayoo ini diimpelementasikan.. SEMANGAT!!!
    /**
     *
     * @param id
     */
    public MessageVector(String id) {
        this.id=id;
    }
    /**
     *
     * @param id
     */
    public void setId(String id){
        this.id = id;
    }
    /**
     *
     * @return
     */
    public String getId(){
        return id;
    }
    /**
     *
     * @param lastEmail
     */
    public void setLastEmail(String lastEmail){
        this.lastEmail = lastEmail;
    }
    /**
     *
     * @return
     */
    public String getLastEmail(){
        return lastEmail;
    }
    /**
     *
     * @param opened
     */
    public void setOpened(boolean opened){
        this.opened=opened;
    }
    /**
     *
     * @return
     */
    public boolean getOpened(){
        return opened;
    }
    /**
     *
     */
    public void removeOld() {
        if(this.size()>MAX_MESSAGES) {
            int n = this.size() - MAX_MESSAGES;
            for(int i=0; i<n; i++)
                this.removeElementAt(0);
        }
    }
}
