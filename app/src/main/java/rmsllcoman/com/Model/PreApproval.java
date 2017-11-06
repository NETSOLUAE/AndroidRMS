package rmsllcoman.com.Model;

/**
 * Created by macmini on 6/13/17.
 */

public class PreApproval {
    public String patientId;
    public String patientName;
    public String staffID;
    public String staffName;
    public String policyRefNo;
    public String entryDate;
    public String diagnosis;
    public String place;
    public String hospitalName;
    public String status;
    public String preApprovalNo;
    public String preApprovalDate;
    public String remarks;

    public PreApproval () {

    }

    public PreApproval (String patientId, String patientName, String staffID, String staffName, String policyRefNo, String entryDate,
                        String diagnosis, String place, String hospitalName, String status, String preApprovalNo,
                        String preApprovalDate, String remarks){
        this.patientId = patientId;
        this.patientName = patientName;
        this.staffID = staffID;
        this.staffName = staffName;
        this.policyRefNo = policyRefNo;
        this.entryDate = entryDate;
        this.diagnosis = diagnosis;
        this.place = place;
        this.hospitalName = hospitalName;
        this.status = status;
        this.preApprovalNo =preApprovalNo;
        this.preApprovalDate = preApprovalDate;
        this.remarks = remarks;
    }

    public void setPatientId (String patientId) {
        this.patientId = patientId;
    }
    public String getPatientId () {
        return patientId;
    }

    public void setPatientName (String patientName) {
        this.patientName = patientName;
    }
    public String getPatientName () {
        return patientName;
    }

    public void setStaffId (String staffID) {
        this.staffID = staffID;
    }
    public String getStaffID () {
        return staffID;
    }

    public void setStaffName (String staffName) {
        this.staffName = staffName;
    }
    public String getStaffName () {
        return staffName;
    }

    public void setPolicyRefNo (String policyRefNo) {
        this.policyRefNo = policyRefNo;
    }
    public String getPolicyRefNo () {
        return policyRefNo;
    }

    public void setEntryDate (String entryDate){
        this.entryDate = entryDate;
    }
    public String getEntryDate () {
        return entryDate;
    }

    public void setDiagnosis (String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public String getDiagnosis () {
        return diagnosis;
    }

    public void setPlace (String place) {
        this.place = place;
    }
    public String getPlace () {
        return place;
    }

    public void setHospitalName (String hospitalName) {
        this.hospitalName = hospitalName;
    }
    public String getHospitalName () {
        return hospitalName;
    }

    public void setStatus (String status) {
        this.status = status;
    }
    public String getStatus () {
        return status;
    }

    public void setPreApprovalNo (String preApprovalNo) {
        this.preApprovalNo = preApprovalNo;
    }
    public String getPreApprovalNo () {
        return preApprovalNo;
    }

    public void setPreApprovalDate (String preApprovalDate) {
        this.preApprovalDate = preApprovalDate;
    }
    public String getPreApprovalDate () {
        return preApprovalDate;
    }

    public void setRemarks (String remarks) {
        this.remarks = remarks;
    }
    public String getRemarks () {
        return remarks;
    }
}
