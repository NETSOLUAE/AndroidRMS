package rmsllcoman.com.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Toast;

import rmsllcoman.com.R;
import rmsllcoman.com.Util.AsyncServiceCall;
import rmsllcoman.com.Util.Constants;
import rmsllcoman.com.Util.DatabaseManager;
import rmsllcoman.com.Util.NetworkManager;
import rmsllcoman.com.Util.WebserviceManager;

/**
 * Created by apple on 6/5/17.
 */

public class PostCommentActivity extends Activity {
    EditText name;
    EditText subject;
    EditText mobile_no;
    EditText comments;
    Button submit;
    LinearLayout heading;
    private WebserviceManager _webServiceManager;
    private DatabaseManager _dbManager;
    String clientID;
    String staffID;
    String memberID;
    String memberName;
    String memberNameSalary;
    String memberNameLines;
    String phoneNumber;
    String nationalId;
    Context context;
    ProgressDialog progressDialog;
    private static long lastUsed = 0;
    public static String message = "";
    String selectedMenu = MenuActivity.selectedMenu;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);

        name = (EditText) findViewById(R.id.comments_name);
        subject = (EditText) findViewById(R.id.comments_subject);
        mobile_no = (EditText) findViewById(R.id.comments_mobile_no);
        comments = (EditText) findViewById(R.id.comments_comment);
        submit = (Button) findViewById(R.id.submit);
        heading = (LinearLayout) findViewById(R.id.comments_heading);

        heading.setVisibility(View.GONE);

        context = PostCommentActivity.this;
        progressDialog = new ProgressDialog(this);
        _dbManager = new DatabaseManager(this);
        _webServiceManager = new WebserviceManager(this);

        if (selectedMenu.equalsIgnoreCase("MemberDetails")) {
            clientID = _dbManager.getClientId();
            staffID = _dbManager.getStaffId();
            memberID = _dbManager.getMemberID();
            memberName = _dbManager.getName();
            phoneNumber = _dbManager.getPhoneNumber();
            if (!memberName.equalsIgnoreCase("")) {
                name.setText(memberName);
            }
            if (!phoneNumber.equalsIgnoreCase("")) {
                mobile_no.setText(phoneNumber);
            }
        } else if (selectedMenu.equalsIgnoreCase("salaryDeductionScheme")) {
            clientID = _dbManager.getClientIdSalary();
            staffID = _dbManager.getStaffIdSalary();
            memberNameSalary = _dbManager.getNameSalary();
            nationalId = _dbManager.getNationalId();
            if (!memberNameSalary.equalsIgnoreCase("")) {
                name.setText(memberNameSalary);
            }
        } else if (selectedMenu.equalsIgnoreCase("personalLines")) {
            clientID = _dbManager.getClientIdLines();
            staffID = _dbManager.getStaffIdLines();
            memberNameLines = _dbManager.getNameLines();
            nationalId = _dbManager.getNationalIdLines();
            if (!memberNameLines.equalsIgnoreCase("")) {
                name.setText(memberNameLines);
            }
        }

        /*
            This will provide Scroll option for EditText,
            but it will not show the scroll bar on EditText
        */
        comments.setScroller(new Scroller(getApplicationContext()));
        comments.setVerticalScrollBarEnabled(true);

        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                lastUsed = System.currentTimeMillis();
            }
        });
        subject.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                lastUsed = System.currentTimeMillis();
            }
        });
        mobile_no.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                lastUsed = System.currentTimeMillis();
            }
        });
        comments.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                lastUsed = System.currentTimeMillis();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameFiled = name.getText().toString();
                String subjectFiled = subject.getText().toString();
                String mobileNoFiled = mobile_no.getText().toString();
                String commentsField = comments.getText().toString();

                if (nameFiled.length() <= 0 || subjectFiled.length() <= 0
                        || mobileNoFiled.length() <= 0 || commentsField.length() <= 0){
                    Toast.makeText(getBaseContext(), context.getString(R.string.all_fileds_mandatory), Toast.LENGTH_LONG).show();
                } else if (!Constants.isValidMobile(mobileNoFiled)) {
                    Toast.makeText(getBaseContext(), context.getString(R.string.valid_mobile_number), Toast.LENGTH_LONG).show();
                } else {
                    postCommentsCall(nameFiled, subjectFiled, mobileNoFiled, commentsField);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        Log.v("User", "Interacted");
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(null)
                .setMessage(context.getString(R.string.logout))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login = new Intent(PostCommentActivity.this, LoginActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        login.putExtra("SELECTED_TAB", "MemberDetails");
                        startActivity(login);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).show();
    }

    @Override
    public void onDestroy() {
        Log.v("User", "Interacted");
        super.onDestroy();
    }

    @Override
    public void onUserInteraction() {
        Log.v("User", "Interacted");
    }

    @Override
    public void onResume() {
        Log.v("User", "Interacted");
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.v("User", "Interacted");
        super.onStop();
    }

    public static boolean getSession(){
        long ideal = System.currentTimeMillis() - lastUsed;
        if (ideal > HomeActivity.DISCONNECT_TIMEOUT) {
            return true;
        } else {
            return false;
        }
    }

    public void postCommentsCall(final String name1, final String subject1, final String mobileNo, final String comments1) {
        try {
            AsyncServiceCall _postCommentsCall = new AsyncServiceCall() {

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

                    if (selectedMenu.equalsIgnoreCase("MemberDetails")) {
                        return _webServiceManager.postComments(clientID, memberID, staffID, name1, subject1, mobileNo, comments1, "MemberDetails");
                    } else if (selectedMenu.equalsIgnoreCase("salaryDeductionScheme")) {
                        return _webServiceManager.postComments(clientID, nationalId, staffID, name1, subject1, mobileNo, comments1, "salaryDeductionScheme");
                    }  else if (selectedMenu.equalsIgnoreCase("personalLines")) {
                        return _webServiceManager.postComments(clientID, nationalId, staffID, name1, subject1, mobileNo, comments1, "personalLines");
                    } else {
                        return _webServiceManager.postComments(clientID, memberID, staffID, name1, subject1, mobileNo, comments1, "MemberDetails");
                    }

                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("success")) {
                        subject.setText("");
                        mobile_no.setText("");
                        name.setText("");
                        comments.setText("");
                        showAlert(context.getString(R.string.comments_update_heading), message, "comments");
                    } else if (result.equalsIgnoreCase("fail")) {
                        showAlert(context.getString(R.string.comments_update_heading), message, "comments");
                    } else {
                        showAlert("", context.getString(R.string.comments_not_update_text), "comments");
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(PostCommentActivity.this)) {
//                    if (NetworkManager.checkIsRoaming(PostCommentActivity.this)) {
//                        if ((progressDialog != null) && (progressDialog.isShowing())) {
//                            try {
//                                progressDialog.dismiss();
//                            } catch (Exception ex) {
//                                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
//                            }
//                        }
//                        new AlertDialog.Builder(PostCommentActivity.this)
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
                        _postCommentsCall.execute(0);
//                    }
                } else {
                        if ((progressDialog != null) && (progressDialog.isShowing())) {
                            try {
                                progressDialog.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                    showAlert(context.getString(R.string.network_availability_heading), context.getString(R.string.network_availability), "network");
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
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
