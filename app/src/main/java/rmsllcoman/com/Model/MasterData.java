package rmsllcoman.com.Model;

/**
 * Created by macmini on 6/20/17.
 */

public class MasterData {
    private String clientID;
    private String staffID;
    private String mobileNo;
    private String chnagePin;

    public MasterData() {

    }

    public void setClientID (String clientID) {
        this.clientID = clientID;
    }
    public String getClientID () {
        return clientID;
    }

    public void setStaffID (String staffID){
        this.staffID = staffID;
    }
    public String getStaffID () {
        return staffID;
    }

    public void setMobileNo (String mobileNo) {
        this.mobileNo = mobileNo;
    }
    public String getMobileNo () {
        return mobileNo;
    }
    public void setChangePin (String changePin) {
        this.chnagePin = changePin;
    }
    public String getChnagePin () {
        return chnagePin;
    }
}
