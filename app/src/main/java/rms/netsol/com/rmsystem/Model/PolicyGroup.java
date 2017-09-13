package rms.netsol.com.rmsystem.Model;

import java.util.ArrayList;

/**
 * Created by macmini on 7/18/17.
 */

public class PolicyGroup {
    private String nationalID;
    private String staffId;
    private String clientId;
    private String name;
    private ArrayList<PolicyChild> policyChild;

    public PolicyGroup () {

    }
    public PolicyGroup (String nationalID, String staffId, String clientId, String name, ArrayList<PolicyChild> policyChild) {
        super();
        this.nationalID = nationalID;
        this.staffId = staffId;
        this.clientId = clientId;
        this.name = name;
        this.policyChild = policyChild;
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

    public void setClientId (String clientId) {
        this.clientId = clientId;
    }

    public String getclientId () {
        return clientId;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getName () {
        return name;
    }

    public ArrayList<PolicyChild> getPolicyChild() {
        return policyChild;
    }

    public void setPolicyChild(ArrayList<PolicyChild> policyChild) {
        this.policyChild = policyChild;
    }
}
