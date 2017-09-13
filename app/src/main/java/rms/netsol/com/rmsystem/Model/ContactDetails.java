package rms.netsol.com.rmsystem.Model;

/**
 * Created by macmini on 6/24/17.
 */

public class ContactDetails {
    private String index;
    private String id;
    private String branch_name;
    private String attention;
    private String po_box;
    private String postal_code;
    private String address;
    private String telephone;
    private String mobile;
    private String fax;
    private String email;
    private String hot_lines;
    private String direct_lines;

    public ContactDetails () {

    }

    public void setIndex (String index) {
        this.index = index;
    }
    public String getIndex () {
        return index;
    }

    public void setID (String id) {
        this.id = id;
    }
    public String getID () {
        return id;
    }

    public void setBranchName (String branchName) {
        this.branch_name = branchName;
    }
    public String getBranchName () {
        return branch_name;
    }

    public void setAttention (String attention) {
        this.attention = attention;
    }
    public String getattention () {
        return attention;
    }

    public void setPO_Box (String po_box) {
        this.po_box = po_box;
    }
    public String getPO_Box () {
        return po_box;
    }

    public void setPostalCode (String postal_code) {
        this.postal_code = postal_code;
    }
    public String getPostalCode () {
        return postal_code;
    }

    public void setAddress (String address) {
        this.address = address;
    }
    public String getAddress () {
        return address;
    }

    public void setTelephone (String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone () {
        return telephone;
    }

    public void setMobile (String mobile) {
        this.mobile = mobile;
    }
    public String getMobile () {
        return mobile;
    }

    public void setFax (String fax) {
        this.fax = fax;
    }
    public String getFax () {
        return fax;
    }

    public void setEmail (String email) {
        this.email = email;
    }
    public String getEmail () {
        return email;
    }

    public void setHotLine (String hot_lines) {
        this.hot_lines = hot_lines;
    }
    public String getHot_lines () {
        return hot_lines;
    }

    public void setDirect_lines (String direct_lines) {
        this.direct_lines = direct_lines;
    }
    public String getDirect_lines () {
        return direct_lines;
    }
}
