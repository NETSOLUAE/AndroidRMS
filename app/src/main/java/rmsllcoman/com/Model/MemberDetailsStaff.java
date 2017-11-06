package rmsllcoman.com.Model;

import java.util.ArrayList;

/**
 * Created by macmini on 6/10/17.
 */

public class MemberDetailsStaff {
    private String clientID;
    private String staffID;
    private String policyRef;
    private String memberID;
    private String memberName;
    private String relationship;
    private String gender;
    private String nationality;
    private String phoneNumber;
    private String effectiveDate;
    private String email;
    private ArrayList<MemberDetailsChild> memberDetailsChild;

    public MemberDetailsStaff () {

    }
    public MemberDetailsStaff (String clientID, String staffID, String policyRef, String memberID, String memberName, String relationship,
                               String gender, String nationality, String phoneNumber, String effectiveDate, String email, ArrayList<MemberDetailsChild> memberDetailsChild) {
        super();
        this.clientID = clientID;
        this.staffID = staffID;
        this.policyRef = policyRef;
        this.memberID = memberID;
        this.memberName = memberName;
        this.relationship = relationship;
        this.gender = gender;
        this.nationality = nationality;
        this.phoneNumber = phoneNumber;
        this.effectiveDate = effectiveDate;
        this.email = email;
        this.memberDetailsChild = memberDetailsChild;
    }

    public void setClientID (String clientID) {
        this.clientID = clientID;
    }

    public String getClientID () {
        return clientID;
    }

    public void setStaffID (String staffID) {
        this.staffID = staffID;
    }

    public String getStaffID () {
        return staffID;
    }

    public void setPolicyRef (String policyRef) {
        this.policyRef = policyRef;
    }

    public String getPolicyRef () {
        return policyRef;
    }

    public void setMemberID (String memberID) {
        this.memberID = memberID;
    }

    public String getMemberID () {
        return memberID;
    }

    public void setMemberName (String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName () {
        return memberName;
    }

    public void setRelationship (String relationship) {
        this.relationship = relationship;
    }

    public String getRelationship () {
        return relationship;
    }

    public void setGender (String gender) {
        this.gender = gender;
    }

    public String getGender () {
        return gender;
    }

    public void setNationality (String nationality) {
        this.nationality = nationality;
    }

    public String getNationality () {
        return nationality;
    }

    public void setPhoneNumber (String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber () {
        return phoneNumber;
    }

    public void setEffectiveDate (String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getEffectiveDate () {
        return effectiveDate;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getEmail () {
        return email;
    }

    public ArrayList<MemberDetailsChild> getMemberDetailsChild() {
        return memberDetailsChild;
    }

    public void setMemberDetailsChild(ArrayList<MemberDetailsChild> memberDetailsChild) {
        this.memberDetailsChild = memberDetailsChild;
    }
}
