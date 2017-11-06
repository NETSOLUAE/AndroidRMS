package rmsllcoman.com.Activity;

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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

import rmsllcoman.com.Adapter.FAQAdapter;
import rmsllcoman.com.Model.FAQAnswers;
import rmsllcoman.com.Model.FAQQuestions;
import rmsllcoman.com.R;
import rmsllcoman.com.Util.AsyncServiceCall;
import rmsllcoman.com.Util.Constants;
import rmsllcoman.com.Util.DatabaseManager;
import rmsllcoman.com.Util.NetworkManager;
import rmsllcoman.com.Util.WebserviceManager;

/**
 * Created by macmini on 10/9/17.
 */

public class FAQActivity extends AppCompatActivity {
    Toolbar toolbar;
    Context context;
    LinearLayout back;
    ExpandableListView expListView;
    private AsyncServiceCall _faqCall;
    WebserviceManager _webserviceManager;
    FAQAdapter faqAdapter;
    public static ProgressDialog progressDialog;
    private DatabaseManager _dbManager;
    private int lastExpandedPosition = -1;
    public static boolean onCreate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        back = (LinearLayout) findViewById(R.id.backLayoutFaq);
        expListView = (ExpandableListView) findViewById(R.id.expandableListViewFaq);

        context = FAQActivity.this;
        _webserviceManager = new WebserviceManager(this);
        _dbManager = new DatabaseManager(this);
        progressDialog = new ProgressDialog(this);

        // Setup Actionbar / Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_faq);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent (FAQActivity.this, MenuActivity.class);
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

        // create database
        if (!isDatabaseExist()) {
            createDatabase();
        }
        contactInfoCall();
    }

    // Check whether the database exists
    private boolean isDatabaseExist() {
        File dbFile = getApplicationContext().getDatabasePath("RMSDATA.db");
        return dbFile.exists();
    }

    // Create the tables
    private void createDatabase() {
        _dbManager.createDb();
    }

    public void contactInfoCall() {
        try {
            _faqCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    // showing refresh animation before making http call
                    progressDialog = ProgressDialog.show(
                            FAQActivity.this,
                            "Getting FAQ Details",
                            FAQActivity.this.getResources().getString(
                                    R.string.please_wait));
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webserviceManager.faqCall("faqs");
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
                        ArrayList<FAQQuestions> contactLocations = SetFaq();
                        if (contactLocations.size() > 0) {
                            faqAdapter = new FAQAdapter(
                                    FAQActivity.this, contactLocations);
                            expListView.setAdapter(faqAdapter);

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
                    } else {
                        showAlert(context.getString(R.string.comments_not_update_text));
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(FAQActivity.this)) {
                    _faqCall.execute(0);
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


    private ArrayList<FAQQuestions> SetFaq() {
        DatabaseManager databasemanager = new DatabaseManager(
                getApplicationContext());

        ArrayList<FAQQuestions> contactLocationList = new ArrayList<FAQQuestions>();
        FAQQuestions contactLocation = new FAQQuestions();
        int contact = databasemanager.getFaqQuestionsCount();
        for (int i=0; i < contact; i++) {
            contactLocation = databasemanager.getFaqQuestions(i);
            contactLocation.setTitle(contactLocation.getTitle());

            int contactDetails = databasemanager.getFaqAnswerCount(i);
            ArrayList<FAQAnswers> contactDetailsList = new ArrayList<>();
            FAQAnswers ccntactDetails = new FAQAnswers();
            for (int j=0; j < contactDetails; j++) {
                ccntactDetails = databasemanager.getFaqAnswers(String.valueOf(i));
                contactDetailsList.add(ccntactDetails);
            }
            contactLocation.setFaqAnswers(contactDetailsList);
            contactLocationList.add(contactLocation);
        }

        return contactLocationList;
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
