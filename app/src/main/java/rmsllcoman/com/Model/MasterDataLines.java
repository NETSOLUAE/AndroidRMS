package rmsllcoman.com.Model;

/**
 * Created by macmini on 8/13/17.
 */

public class MasterDataLines {
    private String clientID;
    private String staffID;
    private String nationalId;
    private String chnagePin;

    public MasterDataLines() {

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

    public void setNationalId (String nationalId) {
        this.nationalId = nationalId;
    }
    public String getNationalId () {
        return nationalId;
    }

    public void setChangePin (String changePin) {
        this.chnagePin = changePin;
    }
    public String getChnagePin () {
        return chnagePin;
    }
}
