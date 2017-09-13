package rms.netsol.com.rmsystem.View.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rms.netsol.com.rmsystem.Adapter.VehicleStatusLinesAdapter;
import rms.netsol.com.rmsystem.Model.AccountSummaryLines;
import rms.netsol.com.rmsystem.R;
import rms.netsol.com.rmsystem.Util.DatabaseManager;
import rms.netsol.com.rmsystem.Util.WebserviceManager;

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
    TextView outstanding;
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
    WebserviceManager _webserviceManager;
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

        _webserviceManager = new WebserviceManager(context);
        databaseManager = new DatabaseManager(context);

        vehicleSearchList = (ListView) findViewById(R.id.vehicle_search_list_lines);
        vehicleSearchList.setTextFilterEnabled(false);
        noRecoeds = (TextView) findViewById(R.id.no_records_vehicle_details_lines);
        no = (TextView) findViewById(R.id.vehicle_no_lines);
        expiry = (TextView) findViewById(R.id.vehicle_expiry_lines);
        statusText = (TextView) findViewById(R.id.vehicle_status_lines);
        outstanding = (TextView) findViewById(R.id.vehicle_out_lines);
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
                total.setText(vehicleDetails.getTotalPremium());
                outstanding.setText(vehicleDetails.getOutstandingAmount());
                clicked = true;
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshViewVehicleLayout.setRefreshing(false);
        refreshLayout.setRefreshing(false);
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
}

