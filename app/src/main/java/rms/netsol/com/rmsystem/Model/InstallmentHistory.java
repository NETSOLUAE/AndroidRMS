package rms.netsol.com.rmsystem.Model;

/**
 * Created by macmini on 8/12/17.
 */

public class InstallmentHistory {
    private String vehicleNo;
    private String date;
    private String amount;
    private String status;

    public InstallmentHistory () {

    }
    public InstallmentHistory (String vehicleNo, String date, String amount, String status) {
        super();
        this.vehicleNo = vehicleNo;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }

    public void setVehicleNo (String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleNo () {
        return vehicleNo;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public String getDate () {
        return date;
    }

    public void setAmount (String amount) {
        this.amount = amount;
    }

    public String getAmount () {
        return amount;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public String getStatus () {
        return status;
    }
}
