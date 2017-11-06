package rmsllcoman.com.Model;

/**
 * Created by macmini on 6/13/17.
 */

public class ClaimDetails {
    private String policyRefNo;
    private String claimNo;
    public String diagnosis;
    private String treatmentDate;
    public String status;
    private String memberType;
    private String claimedAmount;
    private String approvedAmount;
    private String excess;
    private String disallowance;
    private String settledAmountRO;
    private String modeOfPayment;
    private String chequeNo;
    private String settledAmount;
    private String remarks;
    private String registeredDate;
    private String patientName;
    private String staffName;

    public ClaimDetails() {

    }

    public ClaimDetails(String policyRefNo, String claimNo, String diagnosis, String treatmentDate, String status, String memberType, String claimedAmount,
                        String approvedAmount, String excess, String disallowance, String settledAmountRO, String modeOfPayment,
                        String chequeNo, String settledAmount, String remarks, String registeredDate, String patientName, String staffName){
        this.policyRefNo = policyRefNo;
        this.claimNo = claimNo;
        this.diagnosis = diagnosis;
        this.treatmentDate = treatmentDate;
        this.status = status;
        this.memberType = memberType;
        this.claimedAmount = claimedAmount;
        this.approvedAmount = approvedAmount;
        this.excess = excess;
        this.disallowance = disallowance;
        this.settledAmountRO = settledAmountRO;
        this.modeOfPayment =modeOfPayment;
        this.chequeNo = chequeNo;
        this.settledAmount = settledAmount;
        this.remarks = remarks;
        this.registeredDate = registeredDate;
        this.patientName = patientName;
        this.staffName = staffName;
    }

    public void setPolicyRefNo (String policyRefNo) {
        this.policyRefNo = policyRefNo;
    }
    public String getPolicyRefNo () {
        return policyRefNo;
    }

    public void setClaimNo (String claimNo){
        this.claimNo = claimNo;
    }
    public String getClaimNo () {
        return claimNo;
    }

    public void setDiagnosis (String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public String getDiagnosis () {
        return diagnosis;
    }

    public void setTreatmentDate (String treatmentDate) {
        this.treatmentDate = treatmentDate;
    }
    public String getTreatmentDate () {
        return treatmentDate;
    }

    public void setStatus (String status) {
        this.status = status;
    }
    public String getStatus () {
        return status;
    }

    public void setMemberType (String memberType) {
        this.memberType = memberType;
    }
    public String getMemberType () {
        return memberType;
    }

    public void setClaimedAmount (String claimedAmount) {
        this.claimedAmount = claimedAmount;
    }
    public String getClaimedAmount () {
        return claimedAmount;
    }

    public void setApprovedAmount (String approvedAmount) {
        this.approvedAmount = approvedAmount;
    }
    public String getApprovedAmount () {
        return approvedAmount;
    }

    public void setExcess (String excess) {
        this.excess = excess;
    }
    public String getExcess () {
        return excess;
    }

    public void setDisallowance (String disallowance) {
        this.disallowance = disallowance;
    }
    public String getDisallowance () {
        return disallowance;
    }

    public void setSettledAmountRO (String settledAmountRO) {
        this.settledAmountRO = settledAmountRO;
    }
    public String getSettledAmountRO () {
        return settledAmountRO;
    }

    public void setModeOfPayment (String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }
    public String getModeOfPayment () {
        return modeOfPayment;
    }

    public void setChequeNo (String chequeNo) {
        this.chequeNo = chequeNo;
    }
    public String getChequeNo () {
        return chequeNo;
    }

    public void setSettledAmount (String settledAmount) {
        this.settledAmount = settledAmount;
    }
    public String getSettledAmount () {
        return settledAmount;
    }

    public void setRemarks (String remarks) {
        this.remarks = remarks;
    }
    public String getRemarks () {
        return remarks;
    }

    public void setRegisteredDate (String registeredDate) {
        this.registeredDate = registeredDate;
    }
    public String getRegisteredDate () {
        return registeredDate;
    }

    public void setPatientName (String patientName) {
        this.patientName = patientName;
    }
    public String getPatientName () {
        return patientName;
    }

    public void setStaffName (String staffName) {
        this.staffName = staffName;
    }
    public String getStaffName () {
        return staffName;
    }
}
