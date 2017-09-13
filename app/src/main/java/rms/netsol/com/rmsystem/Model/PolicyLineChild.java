package rms.netsol.com.rmsystem.Model;

/**
 * Created by macmini on 8/13/17.
 */

public class PolicyLineChild {
    private String nationalID;
    private String staffId;
    private String number;
    private String startDate;
    private String expiryDate;

    public PolicyLineChild () {

    }
    public PolicyLineChild (String nationalID, String staffId, String number, String startDate, String expiryDate) {
        super();
        this.nationalID = nationalID;
        this.staffId = staffId;
        this.number = number;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
    }

    public void setNationalID (String nationalID) {
        this.nationalID = nationalID;
    }

    public String getNationalID () {
        return nationalID;
    }

    public void setStaffId (String staffId) {
        this.staffId = staffId;
    }

    public String getStaffId () {
        return staffId;
    }

    public void setNumber (String number) {
        this.number = number;
    }

    public String getNumber () {
        return number;
    }

    public void setStartDate (String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate () {
        return startDate;
    }

    public void setExpiryDate (String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiryDate () {
        return expiryDate;
    }

}
