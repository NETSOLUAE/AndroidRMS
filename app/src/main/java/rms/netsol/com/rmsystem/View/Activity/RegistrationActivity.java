package rms.netsol.com.rmsystem.View.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Pattern;

import rms.netsol.com.rmsystem.R;
import rms.netsol.com.rmsystem.Util.AsyncServiceCall;
import rms.netsol.com.rmsystem.Util.Constants;
import rms.netsol.com.rmsystem.Util.NetworkManager;
import rms.netsol.com.rmsystem.Util.WebserviceManager;

import static android.R.attr.startYear;

/**
 * Created by macmini on 9/12/17.
 */

public class RegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Toolbar toolbar;
    EditText name;
    static EditText dob;
    EditText mobileNo;
    EditText companyName;
    EditText email;
    EditText nationalID;
    EditText password;
    EditText confirmPassword;
    Button register;
    Context context;
    LinearLayout back;
    LinearLayout dobLayout;
    DatePickerDialog datepickerdialog;
    private WebserviceManager _webServiceManager;
    ProgressDialog progressDialog;
    public static String message = "";
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        context = RegistrationActivity.this;
        progressDialog = new ProgressDialog(this);

        // Setup Actionbar / Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_registration);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        _webServiceManager = new WebserviceManager(this);
        name = (EditText) findViewById(R.id.reg_name);
        dob = (EditText) findViewById(R.id.reg_dob);
        mobileNo = (EditText) findViewById(R.id.reg_mobile);
        companyName = (EditText) findViewById(R.id.reg_company);
        email = (EditText) findViewById(R.id.reg_email);
        nationalID = (EditText) findViewById(R.id.reg_nationalID);
        password = (EditText) findViewById(R.id.reg_password);
        confirmPassword = (EditText) findViewById(R.id.reg_confirm_password);
        register = (Button) findViewById(R.id.register);
        back = (LinearLayout) findViewById(R.id.backLayoutRegistration);
        dobLayout = (LinearLayout) findViewById(R.id.registration_dob_layout);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datepickerdialog = new DatePickerDialog(context,this,year,month,day);
        datepickerdialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1 = name.getText().toString();
                String dobText = dob.getText().toString();
                String mobileNoText = mobileNo.getText().toString();
                String companyNameText = companyName.getText().toString();
                String emailText = email.getText().toString();
                String nationalIDText = nationalID.getText().toString();
                String passwordText = password.getText().toString();
                String confirmPasswordText = confirmPassword.getText().toString();
                if ((name1.length() <= 0 || dobText.length() <= 0 || mobileNoText.length() <= 0 || companyNameText.length() <= 0
                        || emailText.length() <= 0 || nationalIDText.length() <= 0 || password.length() <= 0 || confirmPasswordText.length() <= 0)){
                    Toast.makeText(getBaseContext(), "All fields are Mandatory", Toast.LENGTH_LONG).show();
                }  else if(!checkEmail(emailText)){
                    Toast.makeText(getBaseContext(),"You have entered an invalid email address.",Toast.LENGTH_LONG).show();
                } else if (!isValidMobile(mobileNoText)) {
                    Toast.makeText(getBaseContext(), "Please enter valid mobile number",
                            Toast.LENGTH_SHORT).show();
                } else if (!passwordText.equalsIgnoreCase(confirmPasswordText)) {
                    Toast.makeText(getBaseContext(), "Entered Password and Confirm PAssword does not match", Toast.LENGTH_LONG).show();
                } else {
                    registration(name1, dobText, mobileNoText, companyNameText, emailText, nationalIDText, passwordText);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dobLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepickerdialog.show();
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepickerdialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (month < 10) {
            dob.setText((dayOfMonth >= 10 ? dayOfMonth : ("0" + dayOfMonth)) + "-" + "0" + (month + 1) + "-" + year);
        } else {
            dob.setText((dayOfMonth >= 10 ? dayOfMonth : ("0" + dayOfMonth)) + "-" + (month + 1) + "-" + year);
        }
    }

    public void registration(final String name1, final String dob1, final String mobile1, final String companyName1, final String email1, final String nationalID1,
                             final String password1) {
        try {
            AsyncServiceCall _feedbackCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {

                    // instantiate it within the onCreate method
                    progressDialog.setMessage(context.getString(R.string.comments_reg_dialog));
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setProgressNumberFormat(null);
                    progressDialog.setProgressPercentFormat(null);
                    progressDialog.show();
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.registration("new_registration", name1, dob1, mobile1, companyName1, email1, nationalID1, password1);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("success")) {
                        name.setText("");
                        dob.setText("");
                        mobileNo.setText("");
                        companyName.setText("");
                        email.setText("");
                        nationalID.setText("");
                        password.setText("");
                        confirmPassword.setText("");
                        showAlert(context.getString(R.string.comments_update_heading), message, "feedback");
                    } else {
                        showAlert("", message, "feedback");
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(RegistrationActivity.this)) {
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
                    _feedbackCall.execute(0);
//                    }
                } else {
                    showAlert(context.getString(R.string.network_availability_heading), context.getString(R.string.network_availability), "network");
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    private boolean isValidMobile(String phone) {
        boolean check=false;
        String number = phone;
//        if (phone.contains("968") || phone.contains("971") ) {
//            number = phone.substring(3);
//        }
//        if (number.substring(0,1).equalsIgnoreCase("0")) {
//            number = number.substring(1);
//        }
        int length = number.length();
        if(length > 12) {
            check = false;
        } else {
            check = true;
        }
        return check;
    }
    public boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private void showAlert(String heading, String message, final String type) {

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

