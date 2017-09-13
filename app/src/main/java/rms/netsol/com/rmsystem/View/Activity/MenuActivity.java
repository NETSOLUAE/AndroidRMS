package rms.netsol.com.rmsystem.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import rms.netsol.com.rmsystem.R;

/**
 * Created by apple on 6/3/17.
 */

public class MenuActivity extends AppCompatActivity {

    ImageView memberDetails;
    ImageView salaryDeductionScheme;
    ImageView personalLines;
    ImageView callBack;
    ImageView contactInfo;
    ImageView postComments;
    public static String selectedMenu = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        memberDetails = (ImageView) findViewById(R.id.memberDetails);
        salaryDeductionScheme = (ImageView) findViewById(R.id.salary_deduction_scheme);
        personalLines = (ImageView) findViewById(R.id.personal_lines);
        callBack = (ImageView) findViewById(R.id.callBack);
        contactInfo = (ImageView) findViewById(R.id.contactInfo);
        postComments = (ImageView) findViewById(R.id.postComments);

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

        callBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent claimStatus = new Intent(MenuActivity.this, Enquiry.class);
                claimStatus.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(claimStatus);
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
    }
}
