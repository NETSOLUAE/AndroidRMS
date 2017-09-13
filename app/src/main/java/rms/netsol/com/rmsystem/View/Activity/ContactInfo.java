package rms.netsol.com.rmsystem.View.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

import rms.netsol.com.rmsystem.Adapter.ContactInfoAdapter;
import rms.netsol.com.rmsystem.Adapter.ContactInfoArAdapter;
import rms.netsol.com.rmsystem.Model.ContactDetails;
import rms.netsol.com.rmsystem.Model.ContactDetailsAr;
import rms.netsol.com.rmsystem.Model.ContactLocation;
import rms.netsol.com.rmsystem.Model.ContactLocationAr;
import rms.netsol.com.rmsystem.R;
import rms.netsol.com.rmsystem.Util.AsyncServiceCall;
import rms.netsol.com.rmsystem.Util.Constants;
import rms.netsol.com.rmsystem.Util.DatabaseManager;
import rms.netsol.com.rmsystem.Util.NetworkManager;
import rms.netsol.com.rmsystem.Util.WebserviceManager;

/**
 * Created by macmini on 6/24/17.
 */

public class ContactInfo extends AppCompatActivity {
    Button english;
    Button arabic;
    Toolbar toolbar;
    Context context;
    LinearLayout back;
    ExpandableListView expListView;
    ExpandableListView expListViewAr;
    private AsyncServiceCall _contactInfoCall;
    WebserviceManager _webserviceManager;
    ContactInfoArAdapter contactInfoArAdapter;
    ContactInfoAdapter contactInfoAdapter;
    public static ProgressDialog progressDialog;
    private DatabaseManager _dbManager;
    private int lastExpandedPosition = -1;
    private int lastExpandedPositionAr = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        english = (Button) findViewById(R.id.button_english);
        arabic = (Button) findViewById(R.id.button_arabic);
        back = (LinearLayout) findViewById(R.id.backLayoutContact);
        expListView = (ExpandableListView) findViewById(R.id.expandableListViewContact);
        expListViewAr = (ExpandableListView) findViewById(R.id.expandableListViewContactAr);

        context = ContactInfo.this;
        _webserviceManager = new WebserviceManager(this);
        _dbManager = new DatabaseManager(this);
        progressDialog = new ProgressDialog(this);

        // Setup Actionbar / Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_contact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expListView.setVisibility(View.VISIBLE);
                expListViewAr.setVisibility(View.GONE);
                arabic.setTextColor(context.getResources().getColor(R.color.tabDefault));
                english.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                arabic.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.custom_background));
                english.setBackgroundColor(context.getResources().getColor(R.color.tabDefault));
                Intent login = new Intent (ContactInfo.this, MenuActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login);
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expListView.setVisibility(View.VISIBLE);
                expListViewAr.setVisibility(View.GONE);
                arabic.setTextColor(context.getResources().getColor(R.color.tabDefault));
                english.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                arabic.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.custom_background));
                english.setBackgroundColor(context.getResources().getColor(R.color.tabDefault));
            }
        });

        arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expListView.setVisibility(View.GONE);
                expListViewAr.setVisibility(View.VISIBLE);
                english.setTextColor(context.getResources().getColor(R.color.tabDefault));
                arabic.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                english.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.custom_background));
                arabic.setBackgroundColor(context.getResources().getColor(R.color.tabDefault));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent (ContactInfo.this, MenuActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login);
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        expListViewAr.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPositionAr != -1
                        && groupPosition != lastExpandedPositionAr) {
                    expListViewAr.collapseGroup(lastExpandedPositionAr);
                }
                lastExpandedPositionAr = groupPosition;
            }
        });

        // create database
        if (!isDatabaseExist()) {
            createDatabase();
        }
            contactInfoCall();
    }

    // Check whether the database exists
    private boolean isDatabaseExist() {
        File dbFile = getApplicationContext().getDatabasePath("RMS.db");
        return dbFile.exists();
    }

    // Create the tables
    private void createDatabase() {
        _dbManager.createDb();
    }

    public void contactInfoCall() {
        try {
            _contactInfoCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    // showing refresh animation before making http call
                    progressDialog = ProgressDialog.show(
                            ContactInfo.this,
                            "Getting Contact Info",
                            ContactInfo.this.getResources().getString(
                                    R.string.please_wait));
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webserviceManager.companyAddressCall("company_addresses");
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated")) {
                        if ((progressDialog != null) && (progressDialog.isShowing())) {
                            try {
                                progressDialog.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                        ArrayList<ContactLocation> contactLocations = SetCompanyAddress();
                        if (contactLocations.size() > 0) {
                            contactInfoAdapter = new ContactInfoAdapter(
                                ContactInfo.this, contactLocations);
                        expListView.setAdapter(contactInfoAdapter);

                        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                            public boolean onChildClick(ExpandableListView parent, View v,
                                                        int groupPosition, int childPosition, long id) {
                                return true;
                            }
                        });
                    } else {
//                        refreshMemberLayout.setVisibility(View.GONE);
//                        refreshViewMemberLayout.setVisibility(View.VISIBLE);
                    }

                        ArrayList<ContactLocationAr> contactLocationsAr = SetCompanyAddressAr();
                        if (contactLocationsAr.size() > 0) {
                            contactInfoArAdapter = new ContactInfoArAdapter(
                                ContactInfo.this, contactLocationsAr);
                        expListViewAr.setAdapter(contactInfoArAdapter);

                        expListViewAr.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                            public boolean onChildClick(ExpandableListView parent, View v,
                                                        int groupPosition, int childPosition, long id) {
                                return true;
                            }
                        });
                    } else {
//                        refreshMemberLayout.setVisibility(View.GONE);
//                        refreshViewMemberLayout.setVisibility(View.VISIBLE);
                    }
                    } else {
                        showAlert(context.getString(R.string.comments_not_update_text));
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(ContactInfo.this)) {
                    _contactInfoCall.execute(0);
                } else {
                    showAlert("No Network Connection");
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }


    private ArrayList<ContactLocation> SetCompanyAddress() {
        DatabaseManager databasemanager = new DatabaseManager(
                getApplicationContext());

        ArrayList<ContactLocation> contactLocationList = new ArrayList<ContactLocation>();
        ContactLocation contactLocation = new ContactLocation();
        int contact = databasemanager.getContactLocationCount();
        for (int i=0; i < contact; i++) {
            contactLocation = databasemanager.getContactLocation(i);
            contactLocation.setLocation(contactLocation.getLocation());

            int contactDetails = databasemanager.getContactDetailsCount(i);
            ArrayList<ContactDetails> contactDetailsList = new ArrayList<>();
            ContactDetails ccntactDetails = new ContactDetails();
            for (int j=0; j < contactDetails; j++) {
                ccntactDetails = databasemanager.getContactDetails(String.valueOf(j),String.valueOf(i));
                contactDetailsList.add(ccntactDetails);
            }
            contactLocation.setContactDetails(contactDetailsList);
            contactLocationList.add(contactLocation);
        }

        return contactLocationList;
    }


    private ArrayList<ContactLocationAr> SetCompanyAddressAr() {
        DatabaseManager databasemanager = new DatabaseManager(
                getApplicationContext());

        ArrayList<ContactLocationAr> contactLocationListAr = new ArrayList<ContactLocationAr>();
        ContactLocationAr contactLocationAr = new ContactLocationAr();
        int contactAr = databasemanager.getContactLocationCountAr();
        for (int i=0; i < contactAr; i++) {
            contactLocationAr = databasemanager.getContactLocationAr(i);
            contactLocationAr.setLocation_ar(contactLocationAr.getLocation_ar());

            int contactDetailsAr = databasemanager.getContactDetailsCountAr(i);
            ArrayList<ContactDetailsAr> contactDetailsListAr = new ArrayList<>();
            ContactDetailsAr ccntactDetailsAr = new ContactDetailsAr();
            for (int j=0; j < contactDetailsAr; j++) {
                ccntactDetailsAr = databasemanager.getContactDetailsAr(String.valueOf(j),String.valueOf(i));
                contactDetailsListAr.add(ccntactDetailsAr);
            }
            contactLocationAr.setContactDetails_ar(contactDetailsListAr);
            contactLocationListAr.add(contactLocationAr);
        }

        return contactLocationListAr;
    }

    private void showAlert(String message) {
        if ((progressDialog != null) && (progressDialog.isShowing())) {
            try {
                progressDialog.dismiss();
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        }
        new AlertDialog.Builder(context)
                .setTitle("Alert")
                .setMessage(message)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true).show();

    }
}
