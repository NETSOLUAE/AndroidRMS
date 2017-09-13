package rms.netsol.com.rmsystem.Model;

import java.util.ArrayList;

/**
 * Created by macmini on 8/12/17.
 */

public class AccountSummary {
    private String nationalID;
    private String staffId;
    private String vehicleNo;
    private String status;
    private String outstanding_amount;
    private String total_premium;
    private String next_installment_amount;
    private String insurer;
    private String start_date;
    private String end_date;
    private String policy_no;
    private String policy_year;
    private ArrayList<InstallmentHistory> installmentHistory;

    public AccountSummary () {

    }
    public AccountSummary (String nationalID, String staffId, String vehicleNo, String status, String outstanding_amount, String total_premium, String next_installment_amount, String insurer,
                           String start_date, String end_date, String policy_no, String policy_year, ArrayList<InstallmentHistory> installmentHistory) {
        super();
        this.nationalID = nationalID;
        this.staffId = staffId;
        this.vehicleNo = vehicleNo;
        this.status = status;
        this.outstanding_amount = outstanding_amount;
        this.total_premium = total_premium;
        this.next_installment_amount = next_installment_amount;
        this.insurer = insurer;
        this.start_date = start_date;
        this.end_date = end_date;
        this.policy_no = policy_no;
        this.policy_year = policy_year;
        this.installmentHistory = installmentHistory;
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

    public void setVehicleNo (String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleNo () {
        return vehicleNo;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public String getStatus () {
        return status;
    }

    public void setOutstandingAmount (String outstanding_amount) {
        this.outstanding_amount = outstanding_amount;
    }

    public String getOutstandingAmount () {
        return outstanding_amount;
    }

    public void setTotalPremium (String total_premium) {
        this.total_premium = total_premium;
    }

    public String getTotalPremium () {
        return total_premium;
    }

    public void setNextInstallmentAmount (String next_installment_amount) {
        this.next_installment_amount = next_installment_amount;
    }

    public String getNextInstallmentAmount () {
        return next_installment_amount;
    }

    public void setInsurer (String insurer) {
        this.insurer = insurer;
    }

    public String getInsurer () {
        return insurer;
    }

    public void setStartDate (String start_date) {
        this.start_date = start_date;
    }

    public String getStartDate () {
        return start_date;
    }

    public void setEndDate (String end_date) {
        this.end_date = end_date;
    }

    public String getEndDate () {
        return end_date;
    }

    public void setPolicyNo (String policy_no) {
        this.policy_no = policy_no;
    }

    public String getPolicyNo () {
        return policy_no;
    }

    public void setPolicyYear (String policy_year) {
        this.policy_year = policy_year;
    }

    public String getPolicyYear () {
        return policy_year;
    }

    public ArrayList<InstallmentHistory> getInstallmentHistory() {
        return installmentHistory;
    }

    public void setInstallmentHistory(ArrayList<InstallmentHistory> installmentHistory) {
        this.installmentHistory = installmentHistory;
    }
}
