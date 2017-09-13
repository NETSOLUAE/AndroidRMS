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

import rms.netsol.com.rmsystem.Adapter.VehicleInstallmentAdapter;
import rms.netsol.com.rmsystem.Adapter.VehicleStatusAdapter;
import rms.netsol.com.rmsystem.Model.AccountSummary;
import rms.netsol.com.rmsystem.Model.InstallmentHistory;
import rms.netsol.com.rmsystem.R;
import rms.netsol.com.rmsystem.Util.DatabaseManager;
import rms.netsol.com.rmsystem.Util.WebserviceManager;

/**
 * Created by macmini on 8/12/17.
 */

public class AccountSummaryActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener{
    Button info;
    Filter filter;
    Context context;
    ListView vehicleSearchList;
    ListView paidInstallmentList;
    TextView noRecoeds;
    TextView no;
    TextView expiry;
    TextView statusText;
    TextView outstanding;
    TextView total;
    TextView nextInstallments;
    TextView installment;
    ImageView backImage;
    SearchView searchView;
    LinearLayout searchBar;
    LinearLayout back;
    LinearLayout vehicle_search_layout;
    LinearLayout vehicle_view_layout;
    LinearLayout intallmentheading;
    SwipeRefreshLayout refreshLayout;
    SwipeRefreshLayout refreshViewVehicleLayout;
    private ArrayList<AccountSummary> vehicleList;
    private ArrayList<InstallmentHistory> currentInstallmentList;
    WebserviceManager _webserviceManager;
    public static boolean clicked = false;
    private VehicleStatusAdapter vehicleStatusAdapter;
    DatabaseManager databaseManager;
    private rms.netsol.com.rmsystem.Adapter.VehicleInstallmentAdapter VehicleInstallmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_summary);

        context = AccountSummaryActivity.this;
        searchView = (SearchView) findViewById(R.id.vehicle_search);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setQueryHint("Search by vehicle ID");

        _webserviceManager = new WebserviceManager(context);
        databaseManager = new DatabaseManager(context);

        info = (Button) findViewById(R.id.info);
        vehicleSearchList = (ListView) findViewById(R.id.vehicle_search_list);
        vehicleSearchList.setTextFilterEnabled(false);
        paidInstallmentList = (ListView) findViewById(R.id.installmentList);
        noRecoeds = (TextView) findViewById(R.id.no_records_vehicle_details);
        no = (TextView) findViewById(R.id.vehicle_no);
        expiry = (TextView) findViewById(R.id.vehicle_expiry);
        statusText = (TextView) findViewById(R.id.vehicle_status);
        outstanding = (TextView) findViewById(R.id.vehicle_out);
        total = (TextView) findViewById(R.id.vehicle_total);
        nextInstallments = (TextView) findViewById(R.id.vehicle_next);
        installment = (TextView) findViewById(R.id.vehicle_installments_paid);
        backImage = (ImageView) findViewById(R.id.backImage);
        searchBar = (LinearLayout) findViewById(R.id.search_linear_vehicle);
        vehicle_search_layout = (LinearLayout) findViewById(R.id.vehicle_search_layout);
        vehicle_view_layout = (LinearLayout) findViewById(R.id.vehicle_view_layout);
        intallmentheading = (LinearLayout) findViewById(R.id.installmentHeading);
        back = (LinearLayout) findViewById(R.id.backLayout);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_vehicle);
        refreshViewVehicleLayout = (SwipeRefreshLayout) findViewById(R.id.swipeView_refresh_layout_vehicle);

        refreshLayout.setOnRefreshListener(this);
        refreshViewVehicleLayout.setOnRefreshListener(this);

        vehicleList= new ArrayList<>();
        currentInstallmentList= new ArrayList<>();

        vehicleList = databaseManager.getAccountSummary();

        if (vehicleList.size() > 0) {
            vehicle_search_layout.setVisibility(View.VISIBLE);
            vehicleSearchList.setVisibility(View.VISIBLE);
            refreshViewVehicleLayout.setVisibility(View.GONE);
            vehicle_view_layout.setVisibility(View.GONE);
            vehicleStatusAdapter = new VehicleStatusAdapter(AccountSummaryActivity.this, vehicleList);
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

        intallmentheading.setVisibility(View.INVISIBLE);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intallmentheading.setVisibility(View.VISIBLE);
                VehicleInstallmentAdapter = new VehicleInstallmentAdapter(AccountSummaryActivity.this, currentInstallmentList);
                paidInstallmentList.setAdapter(VehicleInstallmentAdapter);
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
                if (VehicleInstallmentAdapter != null && currentInstallmentList != null && currentInstallmentList.size() > 0) {
                    currentInstallmentList.clear();
                    VehicleInstallmentAdapter.notifyDataSetChanged();
                }
                vehicle_search_layout.setVisibility(View.VISIBLE);
                vehicle_view_layout.setVisibility(View.GONE);
                intallmentheading.setVisibility(View.INVISIBLE);
                clicked = false;
            }
        });

        vehicleSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountSummary vehicleDetails = null;
                currentInstallmentList = new ArrayList<>();
                if (searchView.hasFocus() && vehicleStatusAdapter.orig != null){
                    vehicleDetails = vehicleStatusAdapter.filterList;
                } else {
                    vehicleDetails = vehicleList.get(position);
                }
                currentInstallmentList = databaseManager.getInstallmentHistory(vehicleDetails.getVehicleNo());
                vehicle_view_layout.setVisibility(View.VISIBLE);
                vehicle_search_layout.setVisibility(View.GONE);
                intallmentheading.setVisibility(View.INVISIBLE);
                no.setText(vehicleDetails.getVehicleNo());
                expiry.setText(vehicleDetails.getEndDate());
                statusText.setText(vehicleDetails.getStatus());
                total.setText(vehicleDetails.getTotalPremium());
                outstanding.setText(vehicleDetails.getOutstandingAmount());
                nextInstallments.setText(vehicleDetails.getNextInstallmentAmount());
                int count = currentInstallmentList.size();
                installment.setText(String.valueOf(count));
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
                        Intent login = new Intent(AccountSummaryActivity.this, LoginActivity.class);
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
