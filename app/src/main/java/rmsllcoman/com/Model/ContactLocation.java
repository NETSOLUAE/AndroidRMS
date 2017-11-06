package rmsllcoman.com.Model;

import java.util.ArrayList;

/**
 * Created by macmini on 6/24/17.
 */

public class ContactLocation {

    private String id;
    private String location;
    private ArrayList<ContactDetails> contactDetails;

    public ContactLocation () {

    }

    public void setID (String id) {
        this.id = id;
    }
    public String getID () {
        return id;
    }

    public void setLocation (String location) {
        this.location = location;
    }
    public String getLocation () {
        return location;
    }

    public ArrayList<ContactDetails> getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ArrayList<ContactDetails> contactDetails) {
        this.contactDetails = contactDetails;
    }
}
