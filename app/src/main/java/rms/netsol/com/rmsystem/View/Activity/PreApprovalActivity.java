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
import android.view.View;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import rms.netsol.com.rmsystem.Adapter.PreApprovalAdapter;
import rms.netsol.com.rmsystem.Model.PreApproval;
import rms.netsol.com.rmsystem.R;
import rms.netsol.com.rmsystem.Util.AsyncServiceCall;
import rms.netsol.com.rmsystem.Util.Constants;
import rms.netsol.com.rmsystem.Util.DatabaseManager;
import rms.netsol.com.rmsystem.Util.NetworkManager;
import rms.netsol.com.rmsystem.Util.WebserviceManager;

/**
 * Created by apple on 6/5/17.
 */

public class PreApprovalActivity extends Activity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener, SwipeRefreshLayout.OnRefreshListener {
    Context context;
    ListView preApprovalSearchList;
    TextView noRecords;
    LinearLayout searchBarPre;
    SwipeRefreshLayout refreshPreapprovalLayout;
    SwipeRefreshLayout refreshViewPreapprovalLayout;
    private ArrayList<PreApproval> preApprovalList;
    PreApprovalAdapter preApprovalAdapter;
    SearchView searchView;
    private AsyncServiceCall _preApprovalCall;
    WebserviceManager _webserviceManager;
    String staffID = "";
    String clientID = "";
    Filter filter;
    static long lastUsedPre = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_approval);

        searchView = (SearchView) findViewById(R.id.pre_approval_search);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setQueryHint("Search by Patient Name");

        context = PreApprovalActivity.this;
        DatabaseManager databaseManager = new DatabaseManager(getApplicationContext());
        preApprovalList = databaseManager.getPreApproval();
        staffID = databaseManager.getStaffId();
        clientID = databaseManager.getClientId();
        _webserviceManager = new WebserviceManager(this);

        preApprovalSearchList = (ListView) findViewById(R.id.preapproval_search_list_view);
        preApprovalSearchList.setTextFilterEnabled(false);
        noRecords = (TextView) findViewById(R.id.no_records_pre_approval);
        searchBarPre = (LinearLayout) findViewById(R.id.search_linear);
        refreshPreapprovalLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        refreshViewPreapprovalLayout = (SwipeRefreshLayout) findViewById(R.id.swipeView_refresh_layout);

        refreshPreapprovalLayout.setOnRefreshListener(this);
        refreshViewPreapprovalLayout.setOnRefreshListener(this);

        if (preApprovalList.size() > 0) {
            preApprovalSearchList.setVisibility(View.VISIBLE);
            refreshViewPreapprovalLayout.setVisibility(View.GONE);
            preApprovalAdapter = new PreApprovalAdapter(PreApprovalActivity.this, preApprovalList);
            preApprovalSearchList.setAdapter(preApprovalAdapter);
            filter = preApprovalAdapter.getFilter();
        } else {
            preApprovalSearchList.setVisibility(View.GONE);
            refreshViewPreapprovalLayout.setVisibility(View.VISIBLE);
        }

        searchBarPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
    }

    public static void setLastUsedPre () {
        lastUsedPre = System.currentTimeMillis();
    }
    public static boolean getPreSession(){
        long ideal = System.currentTimeMillis() - lastUsedPre;
        if (ideal > HomeActivity.DISCONNECT_TIMEOUT) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onClose() {
        if (preApprovalSearchList != null) {
            preApprovalSearchList.clearTextFilter();
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
            preApprovalSearchList.clearTextFilter();
        } else {
            filter.filter(newText);
        }
        return true;
    }

    @Override
    public void onRefresh() {
        preApprovalCall();
    }

    private void preApprovalCall() {
        try {
            _preApprovalCall = new AsyncServiceCall() {
                @Override
                protected void onPreExecute() {
                    // showing refresh animation before making http call
                        refreshPreapprovalLayout.setRefreshing(true);
                        refreshViewPreapprovalLayout.setRefreshing(true);
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webserviceManager.preApprovalCall("pre_approval", staffID, clientID);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if ((result.equalsIgnoreCase("Updated")) || (result.equalsIgnoreCase("NoDataAvailable"))) {
                        DatabaseManager databaseManager = new DatabaseManager(getApplicationContext());
                        preApprovalList = databaseManager.getPreApproval();
                        if (preApprovalList.size() > 0) {
                            preApprovalSearchList.setVisibility(View.VISIBLE);
                            refreshViewPreapprovalLayout.setVisibility(View.GONE);
                            preApprovalAdapter = new PreApprovalAdapter(PreApprovalActivity.this, preApprovalList);
                            preApprovalSearchList.setAdapter(preApprovalAdapter);
                            preApprovalAdapter.notifyDataSetChanged();
                            preApprovalSearchList.invalidateViews();
                            if (preApprovalAdapter.alertDialogPre != null)
                                if ((preApprovalAdapter.alertDialogPre.isShowing()))
                                    preApprovalAdapter.alertDialogPre.dismiss();
                            searchView.setQuery("", false);
                            searchView.setIconified(true);
                        } else {
                            preApprovalSearchList.setVisibility(View.GONE);
                            refreshViewPreapprovalLayout.setVisibility(View.VISIBLE);
                        }
                            refreshPreapprovalLayout.setRefreshing(false);
                            refreshViewPreapprovalLayout.setRefreshing(false);
                    } else {
                        showAlert(context.getString(R.string.comments_not_update_text));
                    }
                    super.onPostExecute(result);
                }
            };
            try {
                if (NetworkManager.isNetAvailable(PreApprovalActivity.this)) {
//                    if (NetworkManager.checkIsRoaming(PreApprovalActivity.this)) {
//                            refreshPreapprovalLayout.setRefreshing(false);
//                            refreshViewPreapprovalLayout.setRefreshing(false);
//                    } else {
                        _preApprovalCall.execute(0);
//                    }
                } else {
                    refreshPreapprovalLayout.setRefreshing(false);
                    refreshViewPreapprovalLayout.setRefreshing(false);
                    showAlert("No Network Connection");
                }
            } catch (Exception ex) {
                    refreshPreapprovalLayout.setRefreshing(false);
                    refreshViewPreapprovalLayout.setRefreshing(false);
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
                refreshPreapprovalLayout.setRefreshing(false);
                refreshViewPreapprovalLayout.setRefreshing(false);
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    private void showAlert(String message) {
            refreshPreapprovalLayout.setRefreshing(false);
            refreshViewPreapprovalLayout.setRefreshing(false);
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
                        Intent login = new Intent(PreApprovalActivity.this, LoginActivity.class);
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
