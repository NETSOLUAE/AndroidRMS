package rms.netsol.com.rmsystem.Model;

import java.util.ArrayList;

/**
 * Created by macmini on 8/15/17.
 */

public class ContactLocationAr {

    private String id_ar;
    private String location_ar;
    private ArrayList<ContactDetailsAr> contactDetails_ar;

    public ContactLocationAr () {

    }

    public void setID_ar (String id_ar) {
        this.id_ar = id_ar;
    }
    public String getID_ar () {
        return id_ar;
    }

    public void setLocation_ar (String location_ar) {
        this.location_ar = location_ar;
    }
    public String getLocation_ar () {
        return location_ar;
    }

    public ArrayList<ContactDetailsAr> getContactDetails_ar() {
        return contactDetails_ar;
    }

    public void setContactDetails_ar(ArrayList<ContactDetailsAr> contactDetails_ar) {
        this.contactDetails_ar = contactDetails_ar;
    }
}
