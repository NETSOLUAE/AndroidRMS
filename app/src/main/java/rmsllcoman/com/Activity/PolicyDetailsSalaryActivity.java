package rmsllcoman.com.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

import rmsllcoman.com.Adapter.PolicyAdapter;
import rmsllcoman.com.Model.PolicyChild;
import rmsllcoman.com.Model.PolicyGroup;
import rmsllcoman.com.R;
import rmsllcoman.com.Util.AsyncServiceCall;
import rmsllcoman.com.Util.Constants;
import rmsllcoman.com.Util.DatabaseManager;
import rmsllcoman.com.Util.NetworkManager;
import rmsllcoman.com.Util.WebserviceManager;

/**
 * Created by macmini on 8/12/17.
 */

public class PolicyDetailsSalaryActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    ExpandableListView expListView;
    TextView noRecords;
    SwipeRefreshLayout refreshPolicyLayout;
    SwipeRefreshLayout refreshViewPolicyLayout;
    PolicyAdapter expListAdapter;

    public static ArrayList<PolicyGroup> memberDetails;
    WebserviceManager _webserviceManager;
    SharedPreferences pref;
    Context context;
    String staffID;
    String clientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_policy_details);

        expListView = (ExpandableListView) findViewById(R.id.expandableListViewPolicy);
        noRecords = (TextView) findViewById(R.id.no_records_policy_details);
        refreshPolicyLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_policy);
        refreshViewPolicyLayout = (SwipeRefreshLayout) findViewById(R.id.swipeView_refresh_layout_policy);

        refreshPolicyLayout.setOnRefreshListener(this);
        refreshViewPolicyLayout.setOnRefreshListener(this);

        _webserviceManager = new WebserviceManager(this);
        pref = getApplicationContext().getSharedPreferences("RMSLocalStorage", MODE_PRIVATE);
        context = PolicyDetailsSalaryActivity.this;

        ArrayList<PolicyGroup> memberDetailsStaff = SetStandardMemberDetails();
        if (memberDetailsStaff.size() > 0) {
            refreshPolicyLayout.setVisibility(View.VISIBLE);
            refreshViewPolicyLayout.setVisibility(View.GONE);
            expListAdapter = new PolicyAdapter(
                    this, memberDetailsStaff);
            expListView.setAdapter(expListAdapter);

            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    return true;
                }
            });
        } else {
            refreshPolicyLayout.setVisibility(View.GONE);
            refreshViewPolicyLayout.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<PolicyGroup> SetStandardMemberDetails() {
        DatabaseManager databasemanager = new DatabaseManager(
                getApplicationContext());
        memberDetails = new ArrayList<PolicyGroup>();

        PolicyGroup memberListStaff = databasemanager.getMemberDetailsStaffSalary();

        ArrayList<PolicyChild> memberDetailsChild = new ArrayList<PolicyChild>();
        memberDetailsChild = databasemanager.getMemberDetailsStaffSalaryPolicy();
        PolicyGroup gruMemberDetails = new PolicyGroup();
        gruMemberDetails.setPolicyChild(memberDetailsChild);
        gruMemberDetails.setName(memberListStaff.getName());

        memberDetails.add(gruMemberDetails);
        return memberDetails;
    }

    @Override
    public void onRefresh() {
        loginServiceCall();
    }

    public void loginServiceCall() {
        try {
            AsyncServiceCall _memberServiceCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    // showing refresh animation before making http call
                    refreshPolicyLayout.setRefreshing(true);
                    refreshViewPolicyLayout.setRefreshing(true);
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    String nationalID = pref.getString("national_id", null);
                    return _webserviceManager.policyDetailsSalaryCall("policies", nationalID);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated")) {
                        ArrayList<PolicyGroup> memberDetailsStaff = SetStandardMemberDetails();
                        if (memberDetailsStaff.size() > 0) {
                            refreshPolicyLayout.setVisibility(View.VISIBLE);
                            refreshViewPolicyLayout.setVisibility(View.GONE);
                            expListAdapter = new PolicyAdapter(PolicyDetailsSalaryActivity.this, memberDetailsStaff);
                            expListView.setAdapter(expListAdapter);
                            expListAdapter.notifyDataSetChanged();
                            expListView.invalidateViews();
                        } else {
                            refreshPolicyLayout.setVisibility(View.GONE);
                            refreshViewPolicyLayout.setVisibility(View.VISIBLE);
                        }
                        refreshPolicyLayout.setRefreshing(false);
                        refreshViewPolicyLayout.setRefreshing(false);
                    } else {
                        showAlert(context.getString(R.string.comments_not_update_text));
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(PolicyDetailsSalaryActivity.this)) {
//                    if (NetworkManager.checkIsRoaming(MemberDetailsActivity.this)) {
//                            refreshPolicyLayout.setRefreshing(false);
//                            refreshViewPolicyLayout.setRefreshing(false);
//                    } else {
                    _memberServiceCall.execute(0);
//                    }
                } else {
                    refreshPolicyLayout.setRefreshing(false);
                    refreshViewPolicyLayout.setRefreshing(false);
                    showAlert("No Network Connection");
                }
            } catch (Exception ex) {
                refreshPolicyLayout.setRefreshing(false);
                refreshViewPolicyLayout.setRefreshing(false);
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            refreshPolicyLayout.setRefreshing(false);
            refreshViewPolicyLayout.setRefreshing(false);
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    private void showAlert(String message) {
        refreshPolicyLayout.setRefreshing(false);
        refreshViewPolicyLayout.setRefreshing(false);
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

    @Override
    public void onBackPressed() {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(null)
                .setMessage(context.getString(R.string.logout))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login = new Intent(PolicyDetailsSalaryActivity.this, LoginActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        login.putExtra("SELECTED_TAB", "salaryDeductionScheme");
                        startActivity(login);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).show();
    }
}
