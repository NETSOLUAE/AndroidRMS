package rmsllcoman.com.Model;

import java.util.ArrayList;

/**
 * Created by macmini on 8/13/17.
 */

public class PolicyLineGroup {
    private String nationalID;
    private String staffId;
    private String clientId;
    private String name;
    private ArrayList<PolicyLineChild> policyChild;

    public PolicyLineGroup () {

    }
    public PolicyLineGroup (String nationalID, String staffId, String clientId, String name, ArrayList<PolicyLineChild> policyChild) {
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

    public ArrayList<PolicyLineChild> getPolicyChild() {
        return policyChild;
    }

    public void setPolicyChild(ArrayList<PolicyLineChild> policyChild) {
        this.policyChild = policyChild;
    }
}
