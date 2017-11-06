package rmsllcoman.com.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import rmsllcoman.com.R;

/**
 * Created by apple on 6/3/17.
 */

public class MenuActivity extends AppCompatActivity {

    ImageView memberDetails;
    ImageView salaryDeductionScheme;
    ImageView personalLines;
    ImageView aggregator;
    ImageView contactInfo;
    ImageView postComments;
    ImageView remainders;
    ImageView faq;
    public static String selectedMenu = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        memberDetails = (ImageView) findViewById(R.id.memberDetails);
        salaryDeductionScheme = (ImageView) findViewById(R.id.salary_deduction_scheme);
        personalLines = (ImageView) findViewById(R.id.personal_lines);
        aggregator = (ImageView) findViewById(R.id.aggregator);
        contactInfo = (ImageView) findViewById(R.id.contactInfo);
        postComments = (ImageView) findViewById(R.id.postComments);
        remainders = (ImageView) findViewById(R.id.remainder);
        faq = (ImageView) findViewById(R.id.faq);

        memberDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMenu = "MemberDetails";
                Intent member = new Intent(MenuActivity.this, LoginActivity.class);
                member.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                member.putExtra("SELECTED_TAB", "MemberDetails");
                startActivity(member);
            }
        });

        salaryDeductionScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMenu = "salaryDeductionScheme";
                Intent information = new Intent(MenuActivity.this, LoginActivity.class);
                information.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                information.putExtra("SELECTED_TAB", "salaryDeductionScheme");
                startActivity(information);
            }
        });

        personalLines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMenu = "personalLines";
                Intent policy = new Intent(MenuActivity.this, LoginActivity.class);
                policy.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                policy.putExtra("SELECTED_TAB", "personalLines");
                startActivity(policy);
            }
        });

        aggregator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                PackageManager manager = getPackageManager();
                try {
                    i = manager.getLaunchIntentForPackage("com.rmsllcoman.agg");
                    if (i == null)
                        throw new PackageManager.NameNotFoundException();
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    //Launch from Play Store Link
                    new AlertDialog.Builder(MenuActivity.this)
                            .setTitle(null)
                            .setMessage("Coming Soon!")

                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setCancelable(true).show();
                }
            }
        });

        contactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent approval = new Intent(MenuActivity.this, ContactInfo.class);
                approval.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(approval);
            }
        });

        postComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comments = new Intent(MenuActivity.this, FeedBackForm.class);
                comments.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(comments);
            }
        });

        remainders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent claimStatus = new Intent(MenuActivity.this, RemainderActivity.class);
                claimStatus.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(claimStatus);
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comments = new Intent(MenuActivity.this, FAQActivity.class);
                comments.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(comments);
            }
        });
    }
}
