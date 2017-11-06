package rmsllcoman.com.Activity;

import android.app.DatePickerDialog;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Pattern;

import rmsllcoman.com.R;
import rmsllcoman.com.Util.AsyncServiceCall;
import rmsllcoman.com.Util.Constants;
import rmsllcoman.com.Util.NetworkManager;
import rmsllcoman.com.Util.WebserviceManager;

/**
 * Created by macmini on 8/12/17.
 */

public class RemainderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    String selectedField = "";
    Toolbar toolbar;
    EditText name;
    EditText dob;
    EditText nationalID;
    EditText mobileNo;
    EditText email;
    EditText insuranceStartDate;
    EditText insuranceEndDate;
    EditText description;
    CheckBox auto;
    CheckBox house;
    CheckBox life;
    CheckBox health;
    Button submit;
    Context context;
    LinearLayout back;
    LinearLayout dobLayout;
    LinearLayout insuranceStartDateLayout;
    LinearLayout insuranceEndDateLayout;
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
        setContentView(R.layout.activity_remainder);

        context = RemainderActivity.this;
        progressDialog = new ProgressDialog(this);

        // Setup Actionbar / Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_remainder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        _webServiceManager = new WebserviceManager(this);
        name = (EditText) findViewById(R.id.remainder_name);
        dob = (EditText) findViewById(R.id.remainder_dob);
        nationalID = (EditText) findViewById(R.id.remainder_nationalID);
        mobileNo = (EditText) findViewById(R.id.remainder_mobile_no);
        email = (EditText) findViewById(R.id.remainder_email);
        insuranceStartDate = (EditText) findViewById(R.id.remainder_insurance_startdate);
        insuranceEndDate = (EditText) findViewById(R.id.remainder_insurance_enddate);
        description = (EditText) findViewById(R.id.remainder_description);
        auto = (CheckBox) findViewById(R.id.checkBox_question_remainder);
        house = (CheckBox) findViewById(R.id.checkBox_suggestion_remainder);
        life = (CheckBox) findViewById(R.id.checkBox_problem_remainder);
        health = (CheckBox) findViewById(R.id.checkBox_contact_remainder);
        submit = (Button) findViewById(R.id.submit_remainder);
        back = (LinearLayout) findViewById(R.id.backLayoutRemainder);
        dobLayout = (LinearLayout) findViewById(R.id.remainder_dob_layout);
        insuranceStartDateLayout = (LinearLayout) findViewById(R.id.remainder_insurance_startdate_layout);
        insuranceEndDateLayout = (LinearLayout) findViewById(R.id.remainder_insurance_enddate_layout);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datepickerdialog = new DatePickerDialog(context,this,year,month,day);
        datepickerdialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);

        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    house.setChecked(false);
                    life.setChecked(false);
                    health.setChecked(false);
                }
            }
        });

        house.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    auto.setChecked(false);
                    life.setChecked(false);
                    health.setChecked(false);
                }
            }
        });

        life.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    auto.setChecked(false);
                    house.setChecked(false);
                    health.setChecked(false);
                }
            }
        });

        health.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    auto.setChecked(false);
                    house.setChecked(false);
                    life.setChecked(false);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1 = name.getText().toString();
                String dobText = dob.getText().toString();
                String nationalIDText = nationalID.getText().toString();
                String mobileNoText = mobileNo.getText().toString();
                String emailText = email.getText().toString();
                String insuranceStartDate1 = insuranceStartDate.getText().toString();
                String insuranceEndDate1 = insuranceEndDate.getText().toString();
                String desc = description.getText().toString();
                if ((name.length() <= 0 || dob.length() <= 0 || nationalID.length() <= 0 || mobileNo.length() <= 0
                        || email.length() <= 0 || insuranceStartDate.length() <= 0 || insuranceEndDate.length() <= 0)){
                    Toast.makeText(getBaseContext(), context.getString(R.string.all_fileds_mandatory), Toast.LENGTH_LONG).show();
                } else if(!checkEmail(emailText)){
                    Toast.makeText(getBaseContext(),context.getString(R.string.valid_email),Toast.LENGTH_LONG).show();
                } else if (!Constants.isValidMobile(mobileNoText)) {
                    Toast.makeText(getBaseContext(), context.getString(R.string.valid_mobile_number),
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (!auto.isChecked() && !house.isChecked() && !life.isChecked() && !health.isChecked())
                        Toast.makeText(getBaseContext(), context.getString(R.string.all_fileds_mandatory), Toast.LENGTH_LONG).show();
                    else {

                        String contact_types = "";
                        if (auto.isChecked())
                            contact_types = "AutoInsurance";
                        else if (house.isChecked())
                            contact_types = "HouseInsurance";
                        else if (life.isChecked())
                            contact_types = "LifeInsurance";
                        else if (health.isChecked())
                            contact_types = "HealthInsurance";
                        remainder(name1, contact_types, dobText, nationalIDText, mobileNoText, emailText, insuranceStartDate1, insuranceEndDate1, desc);
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent (RemainderActivity.this, MenuActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login);
            }
        });

        dobLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedField = "dob";
                datepickerdialog.show();
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedField = "dob";
                datepickerdialog.show();
            }
        });

        insuranceStartDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedField = "start";
                datepickerdialog.show();
            }
        });

        insuranceStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedField = "start";
                datepickerdialog.show();
            }
        });

        insuranceEndDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedField = "end";
                datepickerdialog.show();
            }
        });

        insuranceEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedField = "end";
                datepickerdialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        if (selectedField.equalsIgnoreCase("dob")) {
            if (month < 10) {
                dob.setText((dayOfMonth >= 10 ? dayOfMonth : ("0" + dayOfMonth)) + "-" + "0" + (month) + "-" + year);
            } else {
                dob.setText((dayOfMonth >= 10 ? dayOfMonth : ("0" + dayOfMonth)) + "-" + (month) + "-" + year);
            }
        } else if (selectedField.equalsIgnoreCase("start")) {
            if (month < 10) {
                insuranceStartDate.setText((dayOfMonth >= 10 ? dayOfMonth : ("0" + dayOfMonth)) + "-" + "0" + (month) + "-" + year);
            } else {
                insuranceStartDate.setText((dayOfMonth >= 10 ? dayOfMonth : ("0" + dayOfMonth)) + "-" + (month) + "-" + year);
            }
        } else if (selectedField.equalsIgnoreCase("end")) {
            if (month < 10) {
                insuranceEndDate.setText((dayOfMonth >= 10 ? dayOfMonth : ("0" + dayOfMonth)) + "-" + "0" + (month) + "-" + year);
            } else {
                insuranceEndDate.setText((dayOfMonth >= 10 ? dayOfMonth : ("0" + dayOfMonth)) + "-" + (month) + "-" + year);
            }
        }
    }

    public void remainder(final String name1, final String insurance_type, final String date_of_birth, final String nationalID1, final String mobile, final String email1,
                          final String insuranceStartDate1, final String insuranceEndDate1, final String message1) {
        try {
            AsyncServiceCall _remainderCall = new AsyncServiceCall() {

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
                    return _webServiceManager.remainder(name1, insurance_type, date_of_birth, nationalID1, mobile, email1, insuranceStartDate1, insuranceEndDate1, message1);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("success")) {
                        name.setText("");
                        dob.setText("");
                        nationalID.setText("");
                        mobileNo.setText("");
                        email.setText("");
                        insuranceStartDate.setText("");
                        insuranceEndDate.setText("");
                        description.setText("");
                        auto.setChecked(false);
                        house.setChecked(false);
                        life.setChecked(false);
                        health.setChecked(false);
                        showAlert(context.getString(R.string.comments_update_heading), message);
                    } else {
                        showAlert("", message);
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(RemainderActivity.this)) {
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
                    _remainderCall.execute(0);
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

    public boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
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