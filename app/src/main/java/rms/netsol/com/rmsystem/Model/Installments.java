package rms.netsol.com.rmsystem.Model;

/**
 * Created by macmini on 8/3/17.
 */

public class Installments {
    private int id;
    private String paidAmount;
    private String paidDate;

    public Installments() {

    }

    public Installments(int id, String paidAmount, String paidDate){
        this.id = id;
        this.paidAmount = paidAmount;
        this.paidDate = paidDate;
    }

    public void setID (int id) {
        this.id = id;
    }

    public int getID () {
        return id;
    }

    public void setPaidAmount (String paidAmount) {
        this.paidAmount = paidAmount;
    }
    public String getPaidAmount () {
        return paidAmount;
    }

    public void setPaidDate (String paidDate) {
        this.paidDate = paidDate;
    }
    public String getPaidDate () {
        return paidDate;
    }

}