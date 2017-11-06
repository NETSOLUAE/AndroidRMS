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

import rmsllcoman.com.Adapter.MemberDetailAdapter;
import rmsllcoman.com.Model.MemberDetailsChild;
import rmsllcoman.com.Model.MemberDetailsStaff;
import rmsllcoman.com.R;
import rmsllcoman.com.Util.AsyncServiceCall;
import rmsllcoman.com.Util.Constants;
import rmsllcoman.com.Util.DatabaseManager;
import rmsllcoman.com.Util.NetworkManager;
import rmsllcoman.com.Util.WebserviceManager;

/**
 * Created by apple on 6/5/17.
 */

public class MemberDetailsActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{
    ExpandableListView expListView;
    TextView noRecords;
    SwipeRefreshLayout refreshMemberLayout;
    SwipeRefreshLayout refreshViewMemberLayout;
    MemberDetailAdapter expListAdapter;
    public static ArrayList<MemberDetailsStaff> memberDetails;
    private AsyncServiceCall _memberServiceCall;
    WebserviceManager _webserviceManager;
    private DatabaseManager _dbManager;
    SharedPreferences pref;
    Context context;
    String staffID;
    String clientID;
    static long lastUsedClaim = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memeber_details);

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        noRecords = (TextView) findViewById(R.id.no_records_member_details);
        refreshMemberLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_member);
        refreshViewMemberLayout = (SwipeRefreshLayout) findViewById(R.id.swipeView_refresh_layout_member);

        refreshMemberLayout.setOnRefreshListener(this);
        refreshViewMemberLayout.setOnRefreshListener(this);

        _webserviceManager = new WebserviceManager(this);
        _dbManager = new DatabaseManager(this);
        pref = getApplicationContext().getSharedPreferences("RMSLocalStorage", MODE_PRIVATE);
        context = MemberDetailsActivity.this;

        ArrayList<MemberDetailsStaff> memberDetailsStaff = SetStandardMemberDetails();
        if (memberDetailsStaff.size() > 0) {
            refreshMemberLayout.setVisibility(View.VISIBLE);
            refreshViewMemberLayout.setVisibility(View.GONE);
            expListAdapter = new MemberDetailAdapter(
                    this, memberDetailsStaff);
            expListView.setAdapter(expListAdapter);

            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    return true;
                }
            });
        } else {
            refreshMemberLayout.setVisibility(View.GONE);
            refreshViewMemberLayout.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<MemberDetailsStaff> SetStandardMemberDetails() {
        DatabaseManager databasemanager = new DatabaseManager(
                getApplicationContext());
        memberDetails = new ArrayList<MemberDetailsStaff>();

        MemberDetailsStaff memberListStaff = databasemanager.getMemberDetailsStaff();

        ArrayList<MemberDetailsChild> memberDetailsChild = new ArrayList<MemberDetailsChild>();
        memberDetailsChild = databasemanager.getMemberDetailsChild();
        MemberDetailsStaff gruMemberDetails = new MemberDetailsStaff();
        gruMemberDetails.setMemberDetailsChild(memberDetailsChild);
        gruMemberDetails.setMemberName(memberListStaff.getMemberName());
        gruMemberDetails.setGender(memberListStaff.getGender());

        memberDetails.add(gruMemberDetails);
        return memberDetails;
    }

    @Override
    public void onRefresh() {
        loginServiceCall();
    }

    public void loginServiceCall() {
        try {
            _memberServiceCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    // showing refresh animation before making http call
                        refreshMemberLayout.setRefreshing(true);
                        refreshViewMemberLayout.setRefreshing(true);
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    String phoneNumber=pref.getString("phone_number", null);
                    staffID = _dbManager.getStaffId();
                    clientID = _dbManager.getClientId();
                    return _webserviceManager.memberDetailsCall("staff_details",phoneNumber, staffID, clientID);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated")) {
                        ArrayList<MemberDetailsStaff> memberDetailsStaff = SetStandardMemberDetails();
                        if (memberDetailsStaff.size() > 0) {
                            refreshMemberLayout.setVisibility(View.VISIBLE);
                            refreshViewMemberLayout.setVisibility(View.GONE);
                            expListAdapter = new MemberDetailAdapter(MemberDetailsActivity.this, memberDetailsStaff);
                            expListView.setAdapter(expListAdapter);
                            expListAdapter.notifyDataSetChanged();
                            expListView.invalidateViews();
                        } else {
                            refreshMemberLayout.setVisibility(View.GONE);
                            refreshViewMemberLayout.setVisibility(View.VISIBLE);
                        }
                            refreshMemberLayout.setRefreshing(false);
                            refreshViewMemberLayout.setRefreshing(false);
                    } else {
                        showAlert(context.getString(R.string.comments_not_update_text));
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(MemberDetailsActivity.this)) {
//                    if (NetworkManager.checkIsRoaming(MemberDetailsActivity.this)) {
//                            refreshMemberLayout.setRefreshing(false);
//                            refreshViewMemberLayout.setRefreshing(false);
//                    } else {
                        _memberServiceCall.execute(0);
//                    }
                } else {
                    refreshMemberLayout.setRefreshing(false);
                    refreshViewMemberLayout.setRefreshing(false);
                    showAlert("No Network Connection");
                }
            } catch (Exception ex) {
                    refreshMemberLayout.setRefreshing(false);
                    refreshViewMemberLayout.setRefreshing(false);
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
                refreshMemberLayout.setRefreshing(false);
                refreshViewMemberLayout.setRefreshing(false);
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    private void showAlert(String message) {
            refreshMemberLayout.setRefreshing(false);
            refreshViewMemberLayout.setRefreshing(false);
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

    public static void setLastUsed () {
        lastUsedClaim = System.currentTimeMillis();
    }
    public static boolean getClaimSession(){
        long ideal = System.currentTimeMillis() - lastUsedClaim;
        if (ideal > HomeActivity.DISCONNECT_TIMEOUT) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(null)
                .setMessage(context.getString(R.string.logout))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login = new Intent(MemberDetailsActivity.this, LoginActivity.class);
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
}
