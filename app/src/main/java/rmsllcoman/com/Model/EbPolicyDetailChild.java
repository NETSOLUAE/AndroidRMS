package rmsllcoman.com.Model;

/**
 * Created by macmini on 11/2/17.
 */

public class EbPolicyDetailChild {
    public String memberID;
    public String staffId;
    public String companyName;
    public String policyRef;
    public String startDate;
    public String endDate;

    public EbPolicyDetailChild () {

    }
    public EbPolicyDetailChild (String memberID, String staffId, String companyName, String policyRef, String startDate, String endDate) {
        super();
        this.memberID = memberID;
        this.staffId = staffId;
        this.companyName = companyName;
        this.policyRef = policyRef;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setMemberID (String memberID) {
        this.memberID = memberID;
    }

    public String getMemberID () {
        return memberID;
    }

    public void setStaffId (String staffId) {
        this.staffId = staffId;
    }

    public String getStaffId () {
        return staffId;
    }

    public void setCompanyName (String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName () {
        return companyName;
    }

    public void setPolicyRef (String policyRef) {
        this.policyRef = policyRef;
    }

    public String getPolicyRef () {
        return policyRef;
    }

    public void setstartDate (String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate () {
        return startDate;
    }

    public void setEndDate (String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate () {
        return endDate;
    }

}
