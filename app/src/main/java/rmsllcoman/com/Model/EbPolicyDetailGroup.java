package rmsllcoman.com.Model;

import java.util.ArrayList;

/**
 * Created by macmini on 11/2/17.
 */

public class EbPolicyDetailGroup {
    public String memberName;
    public String memberID;
    public String relationshipType;
    private ArrayList<EbPolicyDetailChild> ebPolicyDetailChild;

    public EbPolicyDetailGroup () {

    }
    public EbPolicyDetailGroup (String memberName, String memberID, String relationshipType, ArrayList<EbPolicyDetailChild> ebPolicyDetailChild) {
        super();
        this.memberName = memberName;
        this.memberID = memberID;
        this.relationshipType = relationshipType;
        this.ebPolicyDetailChild = ebPolicyDetailChild;
    }

    public void setMemberName (String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName () {
        return memberName;
    }

    public void setmemberID (String memberID) {
        this.memberID = memberID;
    }

    public String getmemberID () {
        return memberID;
    }

    public void setRelationship (String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getRelationship () {
        return relationshipType;
    }

    public ArrayList<EbPolicyDetailChild> getEbPolicyDetailChild() {
        return ebPolicyDetailChild;
    }

    public void setEbPolicyDetailChild(ArrayList<EbPolicyDetailChild> ebPolicyDetailChild) {
        this.ebPolicyDetailChild = ebPolicyDetailChild;
    }
}