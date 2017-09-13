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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import rms.netsol.com.rmsystem.R;
import rms.netsol.com.rmsystem.Util.AsyncServiceCall;
import rms.netsol.com.rmsystem.Util.Constants;
import rms.netsol.com.rmsystem.Util.NetworkManager;
import rms.netsol.com.rmsystem.Util.WebserviceManager;

/**
 * Created by macmini on 8/12/17.
 */

public class Enquiry extends AppCompatActivity {
    LinearLayout back;
    Toolbar toolbar;
    EditText name;
    EditText mobileNo;
    EditText subject;
    EditText description;
    CheckBox question;
    CheckBox suggestion;
    CheckBox problem;
    CheckBox contcat;
    Button submit;
    Context context;
    private WebserviceManager _webServiceManager;
    ProgressDialog progressDialog;
    public static String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);

        context = Enquiry.this;
        progressDialog = new ProgressDialog(this);

        // Setup Actionbar / Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_enquiry);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        _webServiceManager = new WebserviceManager(this);
        name = (EditText) findViewById(R.id.enquiry_name);
        mobileNo = (EditText) findViewById(R.id.enquiry_mobile_no);
        subject = (EditText) findViewById(R.id.enquiry_subject);
        description = (EditText) findViewById(R.id.enquiry_description);
        question = (CheckBox) findViewById(R.id.checkBox_question_enquiry);
        suggestion = (CheckBox) findViewById(R.id.checkBox_suggestion_enquiry);
        problem = (CheckBox) findViewById(R.id.checkBox_problem_enquiry);
        contcat = (CheckBox) findViewById(R.id.checkBox_contact_enquiry);
        submit = (Button) findViewById(R.id.submit_enquiry);
        back = (LinearLayout) findViewById(R.id.backLayoutEnquiry);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((name.length() <= 0 || mobileNo.length() <= 0
                        || subject.length() <= 0 || description.length() <= 0)){
                    Toast.makeText(getBaseContext(), "All fields are Mandatory", Toast.LENGTH_LONG).show();
                } else {
                    if (!question.isChecked() && !suggestion.isChecked() && !problem.isChecked() && !contcat.isChecked())
                        Toast.makeText(getBaseContext(), "All fields are Mandatory", Toast.LENGTH_LONG).show();
                    else {
                        String name1 = name.getText().toString();
                        String mobie = mobileNo.getText().toString();
                        String subject1 = subject.getText().toString();
                        String desc = description.getText().toString();

                        String contact_types = "";
                        if (question.isChecked())
                            contact_types = "AutoInsurance";
                        if (suggestion.isChecked())
                            contact_types = contact_types + ",House";
                        if (problem.isChecked())
                            contact_types = contact_types + ",Life/Health";
                        if (contcat.isChecked())
                            contact_types = contact_types + ",Others";
                        feedBack(name1, mobie, contact_types, subject1, desc);
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent (Enquiry.this, MenuActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login);
            }
        });
    }
    public void feedBack(final String name1, final String mobile, final String contact_type, final String subject1, final String description1) {
        try {
            AsyncServiceCall _enquiryCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {

                    // instantiate it within the onCreate method
                    progressDialog.setMessage(context.getString(R.string.comments_submitting_dialog));
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setProgressNumberFormat(null);
                    progressDialog.setProgressPercentFormat(null);
                    progressDialog.show();
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.feedBack(name1, mobile, contact_type, subject1, description1);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("success")) {
                        name.setText("");
                        subject.setText("");
                        mobileNo.setText("");
                        description.setText("");
                        showAlert(context.getString(R.string.comments_update_heading), message);
                    } else {
                        showAlert("", message);
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(Enquiry.this)) {
//                    if (NetworkManager.checkIsRoaming(FeedBackForm.this)) {
//                        new AlertDialog.Builder(FeedBackForm.this)
//                                .setTitle("Alert")
//                                .setMessage(context.getString(R.string.roaming_alert_message))
//                                .setNeutralButton("Ok",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int which) {
//
//                                            }
//                                        }).setCancelable(true).show();
//                    } else {
                    _enquiryCall.execute(0);
//                    }
                } else {
                    showAlert(context.getString(R.string.network_availability_heading), context.getString(R.string.network_availability));
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    private void showAlert(String heading, String message) {

        if ((progressDialog != null) && (progressDialog.isShowing())) {
            try {
                progressDialog.dismiss();
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        }
        new AlertDialog.Builder(context)
                .setTitle(heading)
                .setMessage(message)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true).show();
    }
}