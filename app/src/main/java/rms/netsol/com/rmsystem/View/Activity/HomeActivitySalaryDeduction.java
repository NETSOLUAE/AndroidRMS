package rms.netsol.com.rmsystem.View.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import rms.netsol.com.rmsystem.R;
import rms.netsol.com.rmsystem.Util.AsyncServiceCall;
import rms.netsol.com.rmsystem.Util.Constants;
import rms.netsol.com.rmsystem.Util.DatabaseManager;
import rms.netsol.com.rmsystem.Util.NetworkManager;
import rms.netsol.com.rmsystem.Util.WebserviceManager;

/**
 * Created by macmini on 7/17/17.
 */

public class HomeActivitySalaryDeduction extends MyBaseActivity {

    static Resources ressources;
    Context context;
    View view;
    TabHost tabHost;
    DatabaseManager databaseManager;
    WebserviceManager _webserviceManager;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private AsyncServiceCall _loginServiceCall;
    public static ProgressDialog progressDialog;
    String sharedPinNational = "";
    String national_id = "";
    AlertDialog.Builder dialogBuilder;
    boolean isMandatory = true;
    private Toolbar mToolbar;
    public static String memberMessage = "";

    LocalActivityManager mLocalActivityManager;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg;
    private TextView txtName, txtEmail;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_POLICY = "policy";
    private static final String TAG_ACCOUNT = "account";
    private static final String TAG_COMMENTS = "comments";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_RESET = "reset";
    private static final String TAG_LOGOUT = "logout";
    public static String CURRENT_TAG = TAG_POLICY;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_rop);

        context = HomeActivitySalaryDeduction.this;
        databaseManager = new DatabaseManager(this);
        _webserviceManager = new WebserviceManager(this);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_rop);
        navigationView = (NavigationView) findViewById(R.id.nav_view_rop);

        // Setup Actionbar / Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_rop);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtEmail = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles_rop);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_POLICY;
        }
        //Navigation Drawer End

        pref = getApplicationContext().getSharedPreferences("RMSLocalStorage", MODE_PRIVATE);
        editor = pref.edit();

        String changePin = databaseManager.getChangePinSalary();
        if (changePin.equalsIgnoreCase("Yes")){
            showResetPin(isMandatory);
        }
        ressources = getResources();

        tabHost = (TabHost) findViewById(R.id.tabHostSalary);
        mLocalActivityManager = new LocalActivityManager(this, false);

        tabHost.setup(mLocalActivityManager);
        mLocalActivityManager.dispatchCreate(savedInstanceState); //after the tab's setup is called, you have to call this or it wont work

        Intent intentMemberDetails = new Intent(HomeActivitySalaryDeduction.this, PolicyDetailsSalaryActivity.class);
        View memberDetailsView = prepareTabView(tabHost.getContext(), "Policy Details", ressources.getDrawable(R.drawable.policy_selected_menu), true, null);
        TabHost.TabSpec tabSpecMemberDetails = tabHost
                .newTabSpec("PolicyDetail")
                .setIndicator(memberDetailsView)
                .setContent(intentMemberDetails);
        tabHost.addTab(tabSpecMemberDetails);

        Intent intentAccountSummary = new Intent().setClass(this, AccountSummaryActivity.class);
        View informationView = prepareTabView(tabHost.getContext(), "Account Summary", ressources.getDrawable(R.drawable.vehicle_selected_menu), false, null);
        TabHost.TabSpec tabSpecInformation = tabHost
                .newTabSpec("AccountSummary")
                .setIndicator(informationView)
                .setContent(intentAccountSummary);
        tabHost.addTab(tabSpecInformation);

        Intent intentPostComments = new Intent().setClass(this, PostCommentActivity.class);
        View postCommentsView = prepareTabView(tabHost.getContext(), "Post Comments", ressources.getDrawable(R.drawable.comments_selected_menu), false, null);
        TabHost.TabSpec tabSpecPostComments = tabHost
                .newTabSpec("PostComments")
                .setIndicator(postCommentsView)
                .setContent(intentPostComments);
        tabHost.addTab(tabSpecPostComments);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                TabWidget widget = tabHost.getTabWidget();
                for(int i = 0; i < widget.getChildCount(); i++) {
                    View v = widget.getChildAt(i);
                    if (tabId.equalsIgnoreCase("PolicyDetail") && i == 0){
                        prepareTabView(tabHost.getContext(), "PolicyDetailsSalaryActivity", ressources.getDrawable(R.drawable.policy_selected), true, v);
                    } else if (tabId.equalsIgnoreCase("AccountSummary") && i == 1){
                        prepareTabView(tabHost.getContext(), "AccountSummary", ressources.getDrawable(R.drawable.vehicle_menu), true, v);
                    } else if (tabId.equalsIgnoreCase("PostComments") && i == 2){
                        prepareTabView(tabHost.getContext(), "Post Comments", ressources.getDrawable(R.drawable.comments_selected), true, v);
                    } else {
                        prepareTabView(tabHost.getContext(), "NotSelectedTab", ressources.getDrawable(R.drawable.member_selected), false, v);
                    }
                }
            }
        });

        tabHost.setCurrentTab(0);
    }

    private View prepareTabView(Context context, String text, Drawable drawable, boolean isSelected, View selectedView) {
        view = LayoutInflater.from(context).inflate(R.layout.tab_layout_salary, null);

        TextView tabLabel = (TextView) view.findViewById(R.id.tabLabelSalary);
        ImageView tabImage = (ImageView) view.findViewById(R.id.tabImageSalary);
        LinearLayout tabBackground = (LinearLayout) view.findViewById(R.id.tabBackgroundLayout);

        if (selectedView == null){
            if (isSelected)
                tabBackground.setBackgroundDrawable(ressources.getDrawable(R.drawable.tab_selected));
            else
                tabBackground.setBackgroundDrawable(ressources.getDrawable(R.drawable.tab_default));

            tabLabel.setText(text);
            tabImage.setImageDrawable(drawable);

        } else {
            if (isSelected) {
                selectedView.findViewById(R.id.tabBackgroundLayout).setBackgroundDrawable(ressources.getDrawable(R.drawable.tab_selected));
            }
            else {
                selectedView.findViewById(R.id.tabBackgroundLayout).setBackgroundDrawable(ressources.getDrawable(R.drawable.tab_default));
            }

        }
        return view;
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        String name = databaseManager.getNameSalary();
        String phone = databaseManager.getNationalId();
        txtName.setText(name);
        txtEmail.setText(phone);

        // loading header background image
        imgNavHeaderBg.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.background, 100, 100));
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_notifications_rop:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_reset_pin_rop:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_RESET;
                        drawer.closeDrawers();
                        showResetPin(false);
                        break;
                    case R.id.nav_logout_rop:
                        // launch new intent instead of loading fragment
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_LOGOUT;
                        drawer.closeDrawers();
                        showLogoutAlert();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            showLogoutAlert();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(isFinishing()); //you have to manually dispatch the pause msg
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        showLogoutAlert();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void resetPIN(final String newPin) {
        try {
            _loginServiceCall = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    progressDialog = ProgressDialog.show(
                            HomeActivitySalaryDeduction.this,
                            HomeActivitySalaryDeduction.this.getResources().getString(
                                    R.string.pin_reset_heading),
                            HomeActivitySalaryDeduction.this.getResources().getString(
                                    R.string.please_wait));
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    String staffID = databaseManager.getStaffIdSalary();
                    String clientID = databaseManager.getClientIdSalary();
                    national_id=pref.getString("national_id", null);
                    return _webserviceManager.loginAuthentication("reset_pin",national_id,newPin, Constants.deviceID.trim(), staffID, clientID, "salary");
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
                    if (result.equalsIgnoreCase("success") || result.equalsIgnoreCase("fail")) {
                        if (result.equalsIgnoreCase("success")) {
                            Toast.makeText(getBaseContext(), memberMessage, Toast.LENGTH_LONG).show();
                            Intent home = new Intent(HomeActivitySalaryDeduction.this, LoginActivity.class);
                            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            home.putExtra("SELECTED_TAB", "salaryDeductionScheme");
                            startActivity(home);
                        } else {
                            Toast.makeText(getBaseContext(), memberMessage, Toast.LENGTH_LONG).show();
                            showResetPin(isMandatory);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), context.getString(R.string.comments_not_update_text), Toast.LENGTH_LONG).show();
                        showResetPin(isMandatory);
                    }
                    super.onPostExecute(result);
                }

            };
            try {
                if (NetworkManager.isNetAvailable(HomeActivitySalaryDeduction.this)) {
                        _loginServiceCall.execute(0);
                } else {
                    Toast.makeText(getBaseContext(), context.getString(R.string.network_availability_heading), Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                showResetPin(isMandatory);
                Toast.makeText(getBaseContext(), context.getString(R.string.comments_not_update_text), Toast.LENGTH_LONG).show();
                Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            showResetPin(isMandatory);
            Toast.makeText(getBaseContext(), context.getString(R.string.comments_not_update_text), Toast.LENGTH_LONG).show();
            Log.e(Constants.LOG_RMS, "Exception is " + Log.getStackTraceString(e));
        }
    }

    public void showLogoutAlert() {
        new AlertDialog.Builder(context)
                .setTitle(null)
                .setMessage(context.getString(R.string.logout))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login = new Intent(HomeActivitySalaryDeduction.this, LoginActivity.class);
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

    private void showResetPin(final boolean isMandatory) {
        dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.reset_pin, null);
        dialogBuilder.setView(dialogView);

        final EditText oldPin = (EditText) dialogView.findViewById(R.id.oldPin);
        final EditText newPin = (EditText) dialogView.findViewById(R.id.newPin);
        final EditText confirmPin = (EditText) dialogView.findViewById(R.id.confirmNewPin);
        int maxLength = 4;
        oldPin.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(maxLength)
        });
        int maxLength1 = 4;
        newPin.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(maxLength1)
        });
        int maxLength2 = 4;
        confirmPin.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(maxLength2)
        });

//        if (isMandatory)
//            dialogBuilder.setCancelable(false);

        dialogBuilder.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String oldPIN = oldPin.getText().toString();
                String newPIN = newPin.getText().toString();
                String confirmPIN = confirmPin.getText().toString();
                sharedPinNational=pref.getString("sharedPinNational", null);
                national_id=pref.getString("national_id", null);
                if (sharedPinNational != null && sharedPinNational.equalsIgnoreCase(oldPIN)) {
                    if (newPIN.length() != 4 && confirmPIN.length() != 4)  {
                        Toast.makeText(getBaseContext(), "Please Enter valid PIN", Toast.LENGTH_LONG).show();
                        showResetPin(isMandatory);
                    } else {
                        if (newPIN.equalsIgnoreCase(confirmPIN)){
                            if (confirmPIN.equalsIgnoreCase(sharedPinNational)) {
                                Toast.makeText(getBaseContext(), "Old PIN and New PIN should not be the same", Toast.LENGTH_LONG).show();
                                showResetPin(isMandatory);
                            } else {
                                resetPIN(confirmPIN);
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "New PIN and Confirm PIN does not match", Toast.LENGTH_LONG).show();
                            showResetPin(isMandatory);
                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Please check your OLD PIN", Toast.LENGTH_LONG).show();
                    showResetPin(isMandatory);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}