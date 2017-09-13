package rms.netsol.com.rmsystem.Model;

/**
 * Created by macmini on 7/18/17.
 */

public class Vehicle {
    private int id;
    private String vehicleNo;
    private String status;
    private String totalPremiumCharged;
    private String installmentPaid;
    private String outstandingAmount;
    private String expiryDate;
    private String nextInstallmentAmount;

    public Vehicle() {

    }

    public Vehicle(int id, String vehicleNo, String status, String totalPremiumCharged, String installmentPaid,
                        String outstandingAmount, String expiryDate, String nextInstallmentAmount){
        this.id = id;
        this.vehicleNo = vehicleNo;
        this.status = status;
        this.totalPremiumCharged = totalPremiumCharged;
        this.installmentPaid = installmentPaid;
        this.outstandingAmount = outstandingAmount;
        this.expiryDate = expiryDate;
        this.nextInstallmentAmount = nextInstallmentAmount;
    }

    public void setID (int id) {
        this.id = id;
    }

    public int getID () {
        return id;
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

    public void setTotalPremiumCharged (String totalPremiumCharged){
        this.totalPremiumCharged = totalPremiumCharged;
    }
    public String getTotalPremiumCharged () {
        return totalPremiumCharged;
    }

    public void setInstallmentPaid (String installmentPaid) {
        this.installmentPaid = installmentPaid;
    }
    public String getInstallmentPaid () {
        return installmentPaid;
    }

    public void setOutstandingAmount (String outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }
    public String getOutstandingAmount () {
        return outstandingAmount;
    }

    public void setExpiryDate (String expiryDate) {
        this.expiryDate = expiryDate;
    }
    public String getExpiryDate () {
        return expiryDate;
    }

    public void setNextInstallmentAmount (String nextInstallmentAmount) {
        this.nextInstallmentAmount = nextInstallmentAmount;
    }
    public String getNextInstallmentAmount () {
        return nextInstallmentAmount;
    }

}
