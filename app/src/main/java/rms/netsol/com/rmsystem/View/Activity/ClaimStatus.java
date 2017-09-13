package rms.netsol.com.rmsystem.View.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import rms.netsol.com.rmsystem.Adapter.ClaimStatusAdapter;
import rms.netsol.com.rmsystem.Model.ClaimDetails;
import rms.netsol.com.rmsystem.R;
import rms.netsol.com.rmsystem.Util.AsyncServiceCall;
import rms.netsol.com.rmsystem.Util.Constants;
import rms.netsol.com.rmsystem.Util.DatabaseManager;
import rms.netsol.com.rmsystem.Util.NetworkManager;
import rms.netsol.com.rmsystem.Util.WebserviceManager;

/**
 * Created by macmini on 6/6/17.
 */

public class ClaimStatus extends Activity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener, SwipeRefreshLayout.OnRefreshListener {
    Context context;
    ListView claimSearchList;
    TextView noRecoeds;
    LinearLayout searchBar;
    SwipeRefreshLayout refreshLayout;
    SwipeRefreshLayout refreshViewClaimLayout;
    private ArrayList<ClaimDetails> claimList;
    private ClaimStatusAdapter claimStatusAdapter;
    SearchView searchView;
    private AsyncServiceCall _claimDetailsCall;
    WebserviceManager _webserviceManager;
    String staffID = "";
    String clientNo = "";
    Filter filter;
    static long lastUsedClaim = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_status);

        searchView = (SearchView) findViewById(R.id.claim_search);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setQueryHint("Search by Claim ID");

        context = ClaimStatus.this;
        DatabaseManager databaseManager = new DatabaseManager(getApplicationContext());
        claimList = databaseManager.getClaimStatus();
        staffID = databaseManager.getStaffId();
        clientNo = databaseManager.getClientId();
        _webserviceManager = new WebserviceManager(this);

        claimSearchList = (ListView) findViewById(R.id.claim_search_list);
        claimSearchList.setTextFilterEnabled(false);
        noRecoeds = (TextView) findViewById(R.id.no_records_claim_details);
        searchBar = (LinearLayout) findViewById(R.id.search_linear_claim);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_claim);
        refreshViewClaimLayout = (SwipeRefreshLayout) findViewById(R.id.swipeView_refresh_layout_claim);

        refreshLayout.setOnRefreshListener(this);
        refreshViewClaimLayout.setOnRefreshListener(this);

        if (claimList.size() > 0) {
            claimSearchList.setVisibility(View.VISIBLE);
            refreshViewClaimLayout.setVisibility(View.GONE);
            claimStatusAdapter = new ClaimStatusAdapter(ClaimStatus.this, claimList);
            claimSearchList.setAdapter(claimStatusAdapter);
            filter = claimStatusAdapter.getFilter();
        } else {
            claimSearchList.setVisibility(View.GONE);
            refreshViewClaimLayout.setVisibility(View.VISIBLE);
        }

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
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
    public boolean onClose() {
        if (claimSearchList != null) {
            claimSearchList.clearTextFilter();
            filter.filter("");
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        filter.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            claimSearchList.clearTextFilter();
        } else {
            filter.filter(newText);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(null)
                .setMessage(context.getString(R.string.logout))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login = new Intent(ClaimStatus.this, LoginActivity.class);
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
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void onRefresh() {
        claimDetailsCall();
    }

    private void claimDetailsCall() {
        try {
            _claimDetailsCall = new AsyncServiceCall() {
                @Override
                protected void onPreExecute() {
                    // showing refresh animation before making http call
                        refreshViewClaimLayout.setRefreshing(true);
                        refreshLayout.setRefreshing(true);
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webserviceManager.claimDetailsCall("claim_status", staffID, clientNo);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated") || result.equalsIgnoreCase("NoDataAvailable")) {
                        DatabaseManager databaseManager = new DatabaseManager(getApplicationContext());
                        claimList = databaseManager.getClaimStatus();
                        if (claimList.size() > 0) {
                            claimSearchList.setVisibility(View.VISIBLE);
                            refreshViewClaimLayout.setVisibility(View.GONE);
                            claimStatusAdapter = new ClaimStatusAdapter(ClaimStatus.this, claimList);
                            claimSearchList.setAdapter(claimStatusAdapter);
                            filter = claimStatusAdapter.getFilter();
                            claimStatusAdapter.notifyDataSetChanged();
                            claimSearchList.invalidateViews();
                            if (claimStatusAdapter.alertDialog != null)
                                if ((claimStatusAdapter.alertDialog.isShowing()))
                                    claimStatusAdapter.alertDialog.dismiss();
                            searchView.setQuery("",false);
                            searchView.setIconified(true);
                        } else {
                            claimSearchList.setVisibility(View.GONE);
                            refreshViewClaimLayout.setVisibility(View.VISIBLE);
                        }
                            refreshViewClaimLayout.setRefreshing(false);
                            refreshLayout.setRefreshing(false);
                    } else {
                        showAlert(context.getString(R.string.comments_not_update_text));
                    }
                    super.onPostExecute(result);
                }

            };
            try {
                if (NetworkManager.isNetAvailable(ClaimStatus.this)) {
//                    if (NetworkManager.checkIsRoaming(ClaimStatus.this)) {
//                            refreshViewClaimLayout.setRefreshing(false);
//                            refreshLayout.setRefreshing(false);
//                    } else {
                        _claimDetailsCall.execute(0);
//                    }
                } else {
                    refreshViewClaimLayout.setRefreshing(false);
                    refreshLayout.setRefreshing(false);
                    showAlert("No Network Connection");
                }
            } catch (Exception ex) {
                    refreshViewClaimLayout.setRefreshing(false);
                    refreshLayout.setRefreshing(false);
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
                refreshViewClaimLayout.setRefreshing(false);
                refreshLayout.setRefreshing(false);
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    private void showAlert(String message) {
            refreshViewClaimLayout.setRefreshing(false);
            refreshLayout.setRefreshing(false);
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
