package rmsllcoman.com.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import rmsllcoman.com.Adapter.VehicleStatusLinesAdapter;
import rmsllcoman.com.Model.AccountSummaryLines;
import rmsllcoman.com.R;
import rmsllcoman.com.Util.AsyncServiceCall;
import rmsllcoman.com.Util.Constants;
import rmsllcoman.com.Util.DatabaseManager;
import rmsllcoman.com.Util.NetworkManager;
import rmsllcoman.com.Util.WebserviceManager;

/**
 * Created by macmini on 8/13/17.
 */

public class AccountSummaryLinesActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    Filter filter;
    Context context;
    ListView vehicleSearchList;
    TextView noRecoeds;
    TextView no;
    TextView expiry;
    TextView statusText;
    TextView total;
    ImageView backImage;
    SearchView searchView;
    LinearLayout searchBar;
    LinearLayout back;
    LinearLayout vehicle_search_layout;
    LinearLayout vehicle_view_layout;
    SwipeRefreshLayout refreshLayout;
    SwipeRefreshLayout refreshViewVehicleLayout;
    private ArrayList<AccountSummaryLines> vehicleList;
    WebserviceManager _webServiceManager;
    SharedPreferences pref;
    public static boolean clicked = false;
    private VehicleStatusLinesAdapter vehicleStatusAdapter;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_summary_lines);

        context = AccountSummaryLinesActivity.this;
        searchView = (SearchView) findViewById(R.id.vehicle_search_lines);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setQueryHint("Search by vehicle ID");

        _webServiceManager = new WebserviceManager(context);
        databaseManager = new DatabaseManager(context);
        pref = getApplicationContext().getSharedPreferences("RMSLocalStorage", MODE_PRIVATE);

        vehicleSearchList = (ListView) findViewById(R.id.vehicle_search_list_lines);
        vehicleSearchList.setTextFilterEnabled(false);
        noRecoeds = (TextView) findViewById(R.id.no_records_vehicle_details_lines);
        no = (TextView) findViewById(R.id.vehicle_no_lines);
        expiry = (TextView) findViewById(R.id.vehicle_expiry_lines);
        statusText = (TextView) findViewById(R.id.vehicle_status_lines);
        total = (TextView) findViewById(R.id.vehicle_total_lines);
        backImage = (ImageView) findViewById(R.id.backImage_lines);
        searchBar = (LinearLayout) findViewById(R.id.search_linear_vehicle_lines);
        vehicle_search_layout = (LinearLayout) findViewById(R.id.vehicle_search_layout_lines);
        vehicle_view_layout = (LinearLayout) findViewById(R.id.vehicle_view_layout_lines);
        back = (LinearLayout) findViewById(R.id.backLayout_lines);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_vehicle_lines);
        refreshViewVehicleLayout = (SwipeRefreshLayout) findViewById(R.id.swipeView_refresh_layout_vehicle_lines);

        refreshLayout.setOnRefreshListener(this);
        refreshViewVehicleLayout.setOnRefreshListener(this);

        vehicleList= new ArrayList<>();

        vehicleList = databaseManager.getAccountSummaryLines();

        if (vehicleList.size() > 0) {
            vehicleSearchList.setVisibility(View.VISIBLE);
            refreshViewVehicleLayout.setVisibility(View.GONE);
            vehicle_search_layout.setVisibility(View.VISIBLE);
            vehicle_view_layout.setVisibility(View.GONE);
            vehicleStatusAdapter = new VehicleStatusLinesAdapter(AccountSummaryLinesActivity.this, vehicleList);
            vehicleSearchList.setAdapter(vehicleStatusAdapter);
            filter = vehicleStatusAdapter.getFilter();
        } else {
            vehicleSearchList.setVisibility(View.GONE);
            refreshViewVehicleLayout.setVisibility(View.VISIBLE);
            vehicle_search_layout.setVisibility(View.VISIBLE);
            vehicle_view_layout.setVisibility(View.GONE);
        }

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

//        if (clicked) {
//            vehicle_search_layout.setVisibility(View.GONE);
//            vehicle_view_layout.setVisibility(View.VISIBLE);
//        } else {
//            vehicle_search_layout.setVisibility(View.VISIBLE);
//            vehicle_view_layout.setVisibility(View.GONE);
//        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle_search_layout.setVisibility(View.VISIBLE);
                vehicle_view_layout.setVisibility(View.GONE);
                clicked = false;
            }
        });

        vehicleSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountSummaryLines vehicleDetails = null;
                if (searchView.hasFocus() && vehicleStatusAdapter.orig != null){
                    vehicleDetails = vehicleStatusAdapter.filterList;
                } else {
                    vehicleDetails = vehicleList.get(position);
                }
                vehicle_view_layout.setVisibility(View.VISIBLE);
                vehicle_search_layout.setVisibility(View.GONE);
                no.setText(vehicleDetails.getVehicleNo());
                expiry.setText(vehicleDetails.getEndDate());
                statusText.setText(vehicleDetails.getStatus());
                total.setText(String.format("%s OMR", vehicleDetails.getTotalPremium()));
                clicked = true;
            }
        });
    }

    @Override
    public void onRefresh() {
        loginAccountSummaryLines();
    }

    @Override
    public boolean onClose() {
        if (vehicleSearchList != null) {
            vehicleSearchList.clearTextFilter();
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
            vehicleSearchList.clearTextFilter();
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
                        Intent login = new Intent(AccountSummaryLinesActivity.this, LoginActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        login.putExtra("SELECTED_TAB", "personalLines");
                        startActivity(login);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).show();
    }

    public void loginAccountSummaryLines() {
        try {
            AsyncServiceCall _policyDetailsCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    refreshViewVehicleLayout.setRefreshing(true);
                    refreshLayout.setRefreshing(true);
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    String nationalIDLines=pref.getString("national_id_lines", "");
                    return _webServiceManager.accountSummaryCallLines("vehicles", nationalIDLines);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated") || result.equalsIgnoreCase("NoUpdate")) {

                        vehicleList = databaseManager.getAccountSummaryLines();

                        if (vehicleList.size() > 0) {
                            vehicleStatusAdapter = new VehicleStatusLinesAdapter(AccountSummaryLinesActivity.this, vehicleList);
                            vehicleSearchList.setAdapter(vehicleStatusAdapter);
                            filter = vehicleStatusAdapter.getFilter();
                            vehicleStatusAdapter.notifyDataSetChanged();
                            vehicleSearchList.invalidateViews();
                        } else {
                            refreshViewVehicleLayout.setRefreshing(false);
                            refreshLayout.setRefreshing(false);
                        }
                        refreshViewVehicleLayout.setRefreshing(false);
                        refreshLayout.setRefreshing(false);
                    } else {
                        showNoDataAlert();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(AccountSummaryLinesActivity.this)) {
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

    private void showNoNetworkAlert() {
        refreshViewVehicleLayout.setRefreshing(false);
        refreshLayout.setRefreshing(false);
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
        refreshViewVehicleLayout.setRefreshing(false);
        refreshLayout.setRefreshing(false);
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

