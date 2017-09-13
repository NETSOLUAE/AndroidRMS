package rms.netsol.com.rmsystem.View.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import rms.netsol.com.rmsystem.R;
import rms.netsol.com.rmsystem.Util.AsyncServiceCall;
import rms.netsol.com.rmsystem.Util.Constants;
import rms.netsol.com.rmsystem.Util.DatabaseManager;
import rms.netsol.com.rmsystem.Util.NetworkManager;
import rms.netsol.com.rmsystem.Util.WebserviceManager;

import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by apple on 6/3/17.
 */

public class LoginActivity extends AppCompatActivity {

    Button login;
    TextView mobileHeading;
    EditText password;
    EditText passwordText;
    EditText pin1;
    EditText pin2;
    EditText pin3;
    EditText pin4;
    String pin = "";
    String textPassword;
    String phoneNumber = "";
    String nationalID = "";
    String nationalIDLines = "";
    String sharedPin = "";
    String sharedPinNational = "";
    String sharedPinLines = "";
    String nationalID1 = "";
    String staffID;
    String clientID;
    String staffIDSalary;
    String clientIDSalary;
    String staffIDLines;
    String clientIDLines;
    String selectedMenu;
    TextView forgetPin;
    TextView pinHeading;
    TextView registerNow;
    TextView forgetPassword;
    LinearLayout back;
    LinearLayout pinLayout;
    private WebserviceManager _webServiceManager;
    AlertDialog.Builder dialogBuilder;
    private DatabaseManager _dbManager;
    private Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static ProgressDialog progressDialogReset;
    public static ProgressDialog progressDialog;
    public static ProgressDialog progressDialogFetching;
    public static ProgressDialog progressDialogFetchingSalary;
    public static ProgressDialog progressDialogFetchingLines;
    private static final int SEND_SMS_M = 102;
    public static String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle bundle = this.getIntent().getExtras();
        selectedMenu = bundle.getString("SELECTED_TAB");

        mobileHeading = (TextView) findViewById(R.id.mobileHeading);
        registerNow = (TextView) findViewById(R.id.registerNow);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        pinHeading = (TextView) findViewById(R.id.pinHeading);
        forgetPin = (TextView) findViewById(R.id.forgetPin);
        password = (EditText) findViewById(R.id.password);
        passwordText = (EditText) findViewById(R.id.textpassword);
        pin1 = (EditText) findViewById(R.id.secret_pin);
        pin2 = (EditText) findViewById(R.id.secret_pin1);
        pin3 = (EditText) findViewById(R.id.secret_pin2);
        pin4 = (EditText) findViewById(R.id.secret_pin3);
        back = (LinearLayout) findViewById(R.id.back);
        pinLayout = (LinearLayout) findViewById(R.id.pinLayout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(LoginActivity.this, MenuActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(home);
            }
        });

        pin1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                pin1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                if(s.length() != 0 && s.length() == 1)
                    pin2.requestFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        pin2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                pin2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                if(s.length() != 0 && s.length() == 1)
                    pin3.requestFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                pin2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        pin3.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                pin3.setTransformationMethod(PasswordTransformationMethod.getInstance());
                if(s.length() != 0 && s.length() == 1)
                    pin4.requestFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                pin3.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        pin4.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                pin4.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                pin4.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        pin4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if (pin4.getText().toString().length() == 0) {
                        pin3.requestFocus();
                    }
                }
                return false;
            }
        });

        pin3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if (pin3.getText().toString().length() == 0) {
                        pin2.requestFocus();
                    }
                }
                return false;
            }
        });

        pin2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if (pin2.getText().toString().length() == 0) {
                        pin1.requestFocus();
                    }
                }
                return false;
            }
        });
        forgetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedMenu.equalsIgnoreCase("salaryDeductionScheme")) {
                    resetPin("salary");
                } else if (selectedMenu.equalsIgnoreCase("personalLines")) {
                    resetPin("lines");
                }
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedMenu.equalsIgnoreCase("MemberDetails")) {
                    showForgetDialog(true);
                }
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(LoginActivity.this, RegistrationActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(home);
            }
        });

        _dbManager = new DatabaseManager(this);
        _webServiceManager = new WebserviceManager(this);
        context = LoginActivity.this;

        pref = getApplicationContext().getSharedPreferences("RMSLocalStorage", MODE_PRIVATE);
        editor = pref.edit();

        if (selectedMenu.equalsIgnoreCase("MemberDetails")) {
            mobileHeading.setText("Mobile Number");
            pinLayout.setVisibility(View.GONE);
            passwordText.setVisibility(View.VISIBLE);
            forgetPin.setVisibility(View.GONE);
            forgetPassword.setVisibility(View.VISIBLE);
            registerNow.setVisibility(View.VISIBLE);
            phoneNumber=pref.getString("phone_number", "");
            if (!phoneNumber.equalsIgnoreCase("")){
                password.setText(phoneNumber);
//                password.setTag(password.getKeyListener());
//                password.setKeyListener(null);
//                password.setFocusable(false);
                pinHeading.setText("Enter your Password");
            } else {
                pinHeading.setText("Enter your Password");
                password.setTag(password.getKeyListener());
                password.setKeyListener((KeyListener) password.getTag());
                password.setFocusable(true);
            }
        } else if (selectedMenu.equalsIgnoreCase("salaryDeductionScheme")) {
            mobileHeading.setText("National ID");
            pinLayout.setVisibility(View.VISIBLE);
            passwordText.setVisibility(View.GONE);
            forgetPin.setVisibility(View.VISIBLE);
            forgetPassword.setVisibility(View.GONE);
            registerNow.setVisibility(View.GONE);
            nationalID=pref.getString("national_id", "");
            if (!nationalID.equalsIgnoreCase("")){
                password.setText(nationalID);
                password.setTag(password.getKeyListener());
                password.setKeyListener(null);
                password.setFocusable(false);
                pinHeading.setText("Enter your Secret PIN");
            } else {
                pinHeading.setText("Setup your Secret PIN");
                password.setTag(password.getKeyListener());
                password.setKeyListener((KeyListener) password.getTag());
                password.setFocusable(true);
            }
        } else if (selectedMenu.equalsIgnoreCase("personalLines")) {
            mobileHeading.setText("National ID");
            pinLayout.setVisibility(View.VISIBLE);
            passwordText.setVisibility(View.GONE);
            forgetPin.setVisibility(View.VISIBLE);
            forgetPassword.setVisibility(View.GONE);
            registerNow.setVisibility(View.GONE);
            nationalIDLines=pref.getString("national_id_lines", "");
            if (!nationalIDLines.equalsIgnoreCase("")){
                password.setText(nationalIDLines);
                password.setTag(password.getKeyListener());
                password.setKeyListener(null);
                password.setFocusable(false);
                pinHeading.setText("Enter your Secret PIN");
            } else {
                pinHeading.setText("Setup your Secret PIN");
                password.setTag(password.getKeyListener());
                password.setKeyListener((KeyListener) password.getTag());
                password.setFocusable(true);
            }
        }
        // create database
        if (!isDatabaseExist()) {
            createDatabase();
        }

        if (selectedMenu.equalsIgnoreCase("MemberDetails")) {
            if(!checkPermission()){
                requestPermission();
            }
        }

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textPassword = password.getText().toString();
                if (textPassword.length() != 0) {
                    if (selectedMenu.equalsIgnoreCase("MemberDetails")) {
                        if (passwordText.getText().toString().length() != 0 ){
                            pin = passwordText.getText().toString();
                            Log.d("password", textPassword);
                            if (selectedMenu.equalsIgnoreCase("MemberDetails")) {
                                if (!phoneNumber.equalsIgnoreCase(password.getText().toString().trim())) {
                                    editor.apply();
                                }
                                editor.putString("phone_number", textPassword);
                                editor.putString("pin", pin);
                                editor.apply();

                                phoneNumber=pref.getString("phone_number", null);
                                sharedPin=pref.getString("pin", null);
                                loginServiceCall("eb");
                            }
                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert")
                                    .setMessage(context.getString(R.string.pin_number_error_message))

                                    .setNeutralButton(
                                            "Ok",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                }
                                            }).setCancelable(true).show();
                        }
                    } else if (selectedMenu.equalsIgnoreCase("salaryDeductionScheme") || selectedMenu.equalsIgnoreCase("personalLines")) {
                        if (pin1.getText().toString().length() == 1 && pin2.getText().toString().length() == 1 &&
                                pin3.getText().toString().length() == 1 && pin4.getText().toString().length() == 1){
                            pin = pin1.getText().toString() + pin2.getText().toString()
                                    + pin3.getText().toString() + pin4.getText().toString();
                            Log.d("password", textPassword);
                            if (selectedMenu.equalsIgnoreCase("MemberDetails")) {
                                if (!phoneNumber.equalsIgnoreCase(password.getText().toString().trim())) {
                                    editor.apply();
                                }
                                editor.putString("phone_number", textPassword);
                                editor.putString("pin", pin);
                                editor.apply();

                                phoneNumber=pref.getString("phone_number", null);
                                sharedPin=pref.getString("pin", null);
                                loginServiceCall("eb");
                            } else if (selectedMenu.equalsIgnoreCase("salaryDeductionScheme")) {
                                if (!nationalID.equalsIgnoreCase(password.getText().toString().trim())) {
                                    editor.apply();
                                }
                                editor.putString("national_id", textPassword);
                                editor.putString("sharedPinNational", pin);
                                editor.apply();

                                nationalID=pref.getString("national_id", null);
                                sharedPinNational=pref.getString("sharedPinNational", null);
                                loginServiceCall("salary");
                            } else if (selectedMenu.equalsIgnoreCase("personalLines")) {
                                if (!nationalIDLines.equalsIgnoreCase(password.getText().toString().trim())) {
                                    editor.apply();
                                }
                                editor.putString("national_id_lines", textPassword);
                                editor.putString("pin_lines", pin);
                                editor.apply();

                                nationalIDLines=pref.getString("national_id_lines", null);
                                sharedPinLines=pref.getString("pin_lines", null);
                                loginServiceCall("lines");
                            }
                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert")
                                    .setMessage(context.getString(R.string.pin_number_error_message))

                                    .setNeutralButton(
                                            "Ok",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                }
                                            }).setCancelable(true).show();
                        }
                    }
                } else {
                    String message = "";
                    if (selectedMenu.equalsIgnoreCase("MemberDetails")) {
                        message = context.getString(R.string.phone_number_error_message);
                    } else if (selectedMenu.equalsIgnoreCase("salaryDeductionScheme") || selectedMenu.equalsIgnoreCase("personalLines")) {
                        message = context.getString(R.string.national_id_error_message);
                    }
                    new AlertDialog.Builder(context)
                            .setTitle("Alert")
                            .setMessage(message)

                            .setNeutralButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                        }
                                    }).setCancelable(true).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_M: {
                boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (CameraPermission) {

                    Toast.makeText(LoginActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LoginActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                }
            }
        }

    }

    public boolean checkPermission() {

        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]
                {
                        WRITE_EXTERNAL_STORAGE
                }, SEND_SMS_M);

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

    public void resetPin(final String type) {
        try {
            AsyncServiceCall _resetPinCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    progressDialogReset = ProgressDialog.show(
                            LoginActivity.this,
                            LoginActivity.this.getResources().getString(
                                    R.string.reset_heading),
                            LoginActivity.this.getResources().getString(
                                    R.string.reset_text));
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    if (type.equalsIgnoreCase("eb")) {
                        staffID = _dbManager.getStaffId();
                        clientID = _dbManager.getClientId();
                        return _webServiceManager.sms("reset_pin_new", phoneNumber, staffID, clientID, nationalID1, type);
                    }else if (type.equalsIgnoreCase("salary")) {
                        staffIDSalary = _dbManager.getStaffIdSalary();
                        clientIDSalary = _dbManager.getClientIdSalary();
                        nationalID = _dbManager.getNationalId();
                        return _webServiceManager.sms("send_sms", nationalID, staffIDSalary, clientIDSalary, "", type);
                    }else if (type.equalsIgnoreCase("lines")) {
                        staffIDLines = _dbManager.getStaffIdLines();
                        clientIDLines = _dbManager.getClientIdLines();
                        nationalIDLines = _dbManager.getNationalIdLines();
                        return _webServiceManager.sms("send_sms", nationalIDLines, staffIDLines, clientIDLines, "", type);
                    } else {
                        staffID = _dbManager.getStaffId();
                        clientID = _dbManager.getClientId();
                        return _webServiceManager.sms("send_sms", phoneNumber, staffID, clientID, nationalID1, "eb");
                    }
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
                        try {
                            progressDialogReset.dismiss();
                        } catch (Exception ex) {
                            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
                        }
                    }
                    if (result.equalsIgnoreCase("success") || result.equalsIgnoreCase("fail")) {
                        new AlertDialog.Builder(context)
                                .setTitle("Alert")
                                .setMessage(message)

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true).show();
                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("Alert")
                                .setMessage(context.getString(R.string.comments_not_update_text))

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true).show();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(LoginActivity.this)) {
//                    if (NetworkManager.checkIsRoaming(LoginActivity.this)) {
//                        new AlertDialog.Builder(LoginActivity.this)
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
                        _resetPinCall.execute(0);
//                    }
                } else {
                    showNoNetworkAlert();
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    public void loginServiceCall(final String type) {
        try {
            AsyncServiceCall _loginServiceCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    progressDialog = ProgressDialog.show(
                            LoginActivity.this,
                            LoginActivity.this.getResources().getString(
                                    R.string.authenticating),
                            LoginActivity.this.getResources().getString(
                                    R.string.please_wait));
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    if (type.equalsIgnoreCase("eb")) {
                        staffID = _dbManager.getStaffId();
                        clientID = _dbManager.getClientId();
                        return _webServiceManager.loginAuthentication("login", phoneNumber, sharedPin, Constants.deviceID.trim(), staffID, clientID, type);
                    } else if (type.equalsIgnoreCase("salary")) {
                        staffIDSalary = _dbManager.getStaffIdSalary();
                        clientIDSalary = _dbManager.getClientIdSalary();
                        return _webServiceManager.loginAuthentication("login", nationalID, sharedPinNational, Constants.deviceID.trim(), staffIDSalary, clientIDSalary, type);
                    } else if (type.equalsIgnoreCase("lines")) {
                        staffIDLines = _dbManager.getStaffIdLines();
                        clientIDLines = _dbManager.getClientIdLines();
                        return _webServiceManager.loginAuthentication("login", nationalIDLines, sharedPinLines, Constants.deviceID.trim(), staffIDLines, clientIDLines, type);
                    } else {
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Object resultObj) {
                    if ((progressDialog != null) && (progressDialog.isShowing())) {
                        try {
                            progressDialog.dismiss();
                        } catch (Exception ex) {
                            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
                        }
                    }
                    if (type.equalsIgnoreCase("eb")) {
                        editor.putString("phone_number", textPassword);
                        editor.putString("pin", pin);
                        editor.apply();
                    } else if (type.equalsIgnoreCase("salary")) {
                        editor.putString("national_id", textPassword);
                        editor.putString("sharedPinNational", pin);
                        editor.apply();
                    } else if (type.equalsIgnoreCase("lines")) {
                        editor.putString("national_id_lines", textPassword);
                        editor.putString("pin_lines", pin);
                        editor.apply();
                    }

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("success") || result.equalsIgnoreCase("fail")) {
                        if (result.equalsIgnoreCase("success")) {
                            if (type.equalsIgnoreCase("eb")) {
                                loginMemberDetailsCall();
                            }else if (type.equalsIgnoreCase("salary")) {
                                loginPolicyDetails();
                            }else if (type.equalsIgnoreCase("lines")) {
                                loginPolicyDetailsLines();
                            }
                            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        } else {
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
                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("Alert")
                                .setMessage(context.getString(R.string.comments_not_update_text))

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true).show();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(LoginActivity.this)) {
                        _loginServiceCall.execute(0);
                } else {
                    showNoNetworkAlert();
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    public void loginMemberDetailsCall() {
        try {
            AsyncServiceCall _memberServiceCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    progressDialogFetching = ProgressDialog.show(
                            LoginActivity.this,
                            LoginActivity.this.getResources().getString(
                                    R.string.fetching_data),
                            LoginActivity.this.getResources().getString(
                                    R.string.please_wait));
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    staffID = _dbManager.getStaffId();
                    clientID = _dbManager.getClientId();
                    return _webServiceManager.memberDetailsCall("staff_details", phoneNumber, staffID, clientID);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated") || result.equalsIgnoreCase("NoUpdate")) {
                        claimDetailsCall();
                    } else {
                        showNoDataAlert();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(LoginActivity.this)) {
                        _memberServiceCall.execute(0);
                } else {
                    showNoNetworkAlert();
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    private void claimDetailsCall() {
        try {
            AsyncServiceCall _claimDetailsCall = new AsyncServiceCall() {
                @Override
                protected void onPreExecute() {

                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    staffID = _dbManager.getStaffId();
                    clientID = _dbManager.getClientId();
                    return _webServiceManager.claimDetailsCall("claim_status", staffID, clientID);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if ((result.equalsIgnoreCase("Updated")) || (result.equalsIgnoreCase("NoUpdate")) || (result.equalsIgnoreCase("NoDataAvailable"))) {
                        preApprovalsCall();
                    } else {
                        showNoDataAlert();
                    }

                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(LoginActivity.this)) {
                        _claimDetailsCall.execute(0);
                } else {
                    showNoNetworkAlert();
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    private void preApprovalsCall() {
        try {
            AsyncServiceCall _preApprovalCall = new AsyncServiceCall() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    staffID = _dbManager.getStaffId();
                    clientID = _dbManager.getClientId();
                    return _webServiceManager.preApprovalCall("pre_approval", staffID, clientID);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if ((progressDialog != null) && (progressDialog.isShowing())) {
                        try {
                            progressDialog.dismiss();
                        } catch (Exception ex) {
                            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
                        }
                    }
                    if ((progressDialogFetching != null) && (progressDialogFetching.isShowing())) {
                        try {
                            progressDialogFetching.dismiss();
                        } catch (Exception ex) {
                            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
                        }
                    }
                    if ((result.equalsIgnoreCase("Updated")) || (result.equalsIgnoreCase("NoUpdate") || (result.equalsIgnoreCase("NoDataAvailable")))) {
                        Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(home);
                    } else {
                        //AlertDialog
                        showNoDataAlert();
                    }

                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(LoginActivity.this)) {

//                    if (NetworkManager.checkIsRoaming(LoginActivity.this)) {
//                        new AlertDialog.Builder(LoginActivity.this)
//                                .setTitle("Alert")
//                                .setMessage(context.getString(R.string.roaming_alert_message))
//
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
                        _preApprovalCall.execute(0);
//                    }
                } else {
                    showNoNetworkAlert();
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    public void loginPolicyDetails() {
        try {
            AsyncServiceCall _policyDetailsCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    progressDialogFetchingSalary = ProgressDialog.show(
                            LoginActivity.this,
                            LoginActivity.this.getResources().getString(
                                    R.string.fetching_data),
                            LoginActivity.this.getResources().getString(
                                    R.string.please_wait));
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.policyDetailsSalaryCall("policies", nationalID);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated") || result.equalsIgnoreCase("NoUpdate")) {
                        loginAccountSummary();
                    } else {
                        showNoDataAlert();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(LoginActivity.this)) {
                    _policyDetailsCall.execute(0);
                } else {
                    showNoNetworkAlert();
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    public void loginAccountSummary() {
        try {
            AsyncServiceCall _policyDetailsCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.accountSummaryCall("vehicles", nationalID);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated") || result.equalsIgnoreCase("NoUpdate")) {
                        if ((progressDialog != null) && (progressDialog.isShowing())) {
                            try {
                                progressDialog.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                        if ((progressDialogFetchingSalary != null) && (progressDialogFetchingSalary.isShowing())) {
                            try {
                                progressDialogFetchingSalary.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                        Intent homeRop = new Intent(LoginActivity.this, HomeActivitySalaryDeduction.class);
                        homeRop.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(homeRop);
                    } else {
                        showNoDataAlert();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(LoginActivity.this)) {
                    _policyDetailsCall.execute(0);
                } else {
                    showNoNetworkAlert();
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    public void loginPolicyDetailsLines() {
        try {
            AsyncServiceCall _policyDetailsCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    progressDialogFetchingLines = ProgressDialog.show(
                            LoginActivity.this,
                            LoginActivity.this.getResources().getString(
                                    R.string.fetching_data),
                            LoginActivity.this.getResources().getString(
                                    R.string.please_wait));
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.policyDetailsLinesCall("policies", nationalIDLines);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated") || result.equalsIgnoreCase("NoUpdate")) {
                        loginAccountSummaryLines();
                    } else {
                        showNoDataAlert();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(LoginActivity.this)) {
                    _policyDetailsCall.execute(0);
                } else {
                    showNoNetworkAlert();
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    public void loginAccountSummaryLines() {
        try {
            AsyncServiceCall _policyDetailsCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.accountSummaryCallLines("vehicles", nationalIDLines);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated") || result.equalsIgnoreCase("NoUpdate")) {
                        if ((progressDialog != null) && (progressDialog.isShowing())) {
                            try {
                                progressDialog.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                        if ((progressDialogFetchingLines != null) && (progressDialogFetchingLines.isShowing())) {
                            try {
                                progressDialogFetchingLines.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                        Intent homeLines = new Intent(LoginActivity.this, HomeActivityLines.class);
                        homeLines.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(homeLines);
                    } else {
                        showNoDataAlert();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(LoginActivity.this)) {
                    _policyDetailsCall.execute(0);
                } else {
                    showNoNetworkAlert();
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    private void showForgetDialog(final boolean isMandatory) {
        dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.forget_password, null);
        dialogBuilder.setView(dialogView);

        final EditText nationalID = (EditText) dialogView.findViewById(R.id.nationalID);

        dialogBuilder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                nationalID1 = nationalID.getText().toString();
                if (nationalID1.length() == 0)  {
                    Toast.makeText(getBaseContext(), "Please Enter Your NationalID", Toast.LENGTH_LONG).show();
                    showForgetDialog(isMandatory);
                } else {
                    resetPin("eb");
                }
            }
        });
        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void showNoNetworkAlert() {

        if ((progressDialog != null) && (progressDialog.isShowing())) {
            try {
                progressDialog.dismiss();

            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        }

        if ((progressDialogFetching != null) && (progressDialogFetching.isShowing())) {
            try {
                progressDialogFetching.dismiss();
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        }

        if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
            try {
                progressDialogReset.dismiss();
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        }

        if ((progressDialogFetchingSalary != null) && (progressDialogFetchingSalary.isShowing())) {
            try {
                progressDialogFetchingSalary.dismiss();
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        }

        if ((progressDialogFetchingLines != null) && (progressDialogFetchingLines.isShowing())) {
            try {
                progressDialogFetchingLines.dismiss();
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        }
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.network_availability_heading))
                .setMessage(context.getString(R.string.network_availability))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true).show();

    }

    private void showNoDataAlert() {

        if ((progressDialog != null) && (progressDialog.isShowing())) {
            try {
                progressDialog.dismiss();

            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        }

        if ((progressDialogFetching != null) && (progressDialogFetching.isShowing())) {
            try {
                progressDialogFetching.dismiss();
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        }

        if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
            try {
                progressDialogReset.dismiss();
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        }

        if ((progressDialogFetchingSalary != null) && (progressDialogFetchingSalary.isShowing())) {
            try {
                progressDialogFetchingSalary.dismiss();
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        }

        if ((progressDialogFetchingLines != null) && (progressDialogFetchingLines.isShowing())) {
            try {
                progressDialogFetchingLines.dismiss();
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        }
        new AlertDialog.Builder(context)
                .setTitle("Alert")
                .setMessage(context.getString(R.string.comments_not_update_text))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true).show();

    }
}
