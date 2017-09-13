package rms.netsol.com.rmsystem.View.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.WebView;

import rms.netsol.com.rmsystem.R;
import rms.netsol.com.rmsystem.Util.AsyncServiceCall;
import rms.netsol.com.rmsystem.Util.Constants;
import rms.netsol.com.rmsystem.Util.NetworkManager;
import rms.netsol.com.rmsystem.Util.WebserviceManager;

/**
 * Created by apple on 6/5/17.
 */

public class PolicyDetailsActivity extends Activity {
    Context context;
    WebView webview;
    ProgressDialog progressDialog;
    public static String message;
    public static String htmlString = "";
    WebserviceManager _webserviceManager;
    private AsyncServiceCall _policyDetailsCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_details);
        context = PolicyDetailsActivity.this;
        webview = (WebView) findViewById(R.id.webView);
        _webserviceManager = new WebserviceManager(this);
        progressDialog = new ProgressDialog(this);
        policyDetailsCall();
    }

    @Override
    public void onBackPressed() {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(null)
                .setMessage(context.getString(R.string.logout))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login = new Intent(PolicyDetailsActivity.this, LoginActivity.class);
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

    private void policyDetailsCall() {
        try {
            _policyDetailsCall = new AsyncServiceCall() {
                @Override
                protected void onPreExecute() {
                    // showing refresh animation before making http call
                    // instantiate it within the onCreate method
                    progressDialog.setMessage(context.getString(R.string.policy_dialog));
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setProgressNumberFormat(null);
                    progressDialog.setProgressPercentFormat(null);
                    progressDialog.show();
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webserviceManager.policyDetailsCall("privacy-policy");
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("success")) {
                        if ((progressDialog != null) && (progressDialog.isShowing())) {
                            try {
                                progressDialog.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                        webview.loadData(htmlString, "text/html", null);
//                        showAlert(message);
                    } else if (result.equalsIgnoreCase("fail")) {
                        showAlert(message);
                    } else {
                        if ((progressDialog != null) && (progressDialog.isShowing())) {
                            try {
                                progressDialog.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                        showAlert(context.getString(R.string.comments_not_update_text));
                    }
                    super.onPostExecute(result);
                }

            };
            try {
                if (NetworkManager.isNetAvailable(PolicyDetailsActivity.this)) {
//                    if (NetworkManager.checkIsRoaming(PolicyDetailsActivity.this)) {
//                        showAlert(context.getString(R.string.roaming_alert_message));
//                    } else {
                        _policyDetailsCall.execute(0);
//                    }
                } else {
                    showAlert(context.getString(R.string.network_availability));
                }
            } catch (Exception ex) {
                showAlert(context.getString(R.string.network_availability));
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            showAlert(context.getString(R.string.network_availability));
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
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