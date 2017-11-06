package rmsllcoman.com.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import rmsllcoman.com.R;

/**
 * Created by macmini on 6/20/17.
 */

public class MyBaseActivity extends AppCompatActivity {
    public static final long DISCONNECT_TIMEOUT = 15 * 60 * 1000; // 30 sec = 30 * 1000 ms
    public Context context = this;

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            boolean sessionEllapsed = PostCommentActivity.getSession();
            boolean claimSessionEllapsed = ClaimStatus.getClaimSession();
            boolean preSessionEllapsed = PreApprovalActivity.getPreSession();
            if (!sessionEllapsed || !claimSessionEllapsed || !preSessionEllapsed){
                resetDisconnectTimer();
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        MyBaseActivity.this);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Alert");
                alertDialog
                        .setMessage(context.getString(R.string.session_timeout));
                alertDialog.setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MyBaseActivity.this,
                                        LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("SELECTED_TAB", "MemberDetails");
                                startActivity(intent);

                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }

            // Perform any required operation on disconnect
        }
    };

    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }


    @Override
    public void onBackPressed() {
//        HomeActivity.showLogoutAlert();
        Log.d("","");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }
}
