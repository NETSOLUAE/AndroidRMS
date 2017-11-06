package rmsllcoman.com.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import rmsllcoman.com.Activity.PolicyDetailsActivity;
import rmsllcoman.com.Model.EbPolicyDetailChild;
import rmsllcoman.com.Model.EbPolicyDetailGroup;
import rmsllcoman.com.Model.MemberDetailsChild;
import rmsllcoman.com.Model.MemberDetailsStaff;
import rmsllcoman.com.R;

/**
 * Created by macmini on 11/2/17.
 */

public class EbPolicyDetailsAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private ArrayList<EbPolicyDetailGroup> groups;
    public AlertDialog alertDialog;

    public EbPolicyDetailsAdapter(Activity context, ArrayList<EbPolicyDetailGroup> groups) {
        this.context = context;
        this.groups = groups;
        alertDialog = null;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.eb_policy_detail_child, null);
        }
        EbPolicyDetailGroup groupObj = groups.get(groupPosition);
        EbPolicyDetailChild child = groupObj.getEbPolicyDetailChild().get(
                childPosition);
        TextView comapnyNameText = (TextView) convertView.findViewById(R.id.eb_company_name);
        TextView staffIdText = (TextView) convertView.findViewById(R.id.eb_company_staffId);
        TextView policyRef = (TextView) convertView.findViewById(R.id.eb_policy_ref);
        TextView effectiveDate = (TextView) convertView.findViewById(R.id.eb_effective_date);

        String companyName = child.getCompanyName();
        if ((companyName == null) || (companyName.equals("")) || (companyName.equals("null"))) {
            comapnyNameText.setText("-");
        } else {
            comapnyNameText.setText(companyName);
        }

        String staffId = child.getStaffId();
        if ((staffId == null) || (staffId.equals("")) || (staffId.equals("null"))) {
            staffIdText.setText("-");
        } else {
            staffIdText.setText(staffId);
        }

        String policy = child.getPolicyRef();
        if ((policy == null) || (policy.equals("")) || (policy.equals("null"))) {
            policyRef.setText("-");
        } else {
            policyRef.setText(policy);
        }

        String startDate = child.getStartDate();
        String endDate = child.getEndDate();
        if (((startDate == null) || (startDate.equals("")) || (startDate.equals("null"))) && ((endDate == null) || (endDate.equals("")) || (endDate.equals("null")))) {
            effectiveDate.setText("-");
        } else if ((startDate == null) || (startDate.equals("")) || (startDate.equals("null"))){
            String date = endDate.replace("-", "/");
            effectiveDate.setText(date);
        } else if ((endDate == null) || (endDate.equals("")) || (endDate.equals("null"))){
            String date = startDate.replace("-", "/");
            effectiveDate.setText(date);
        } else {
            String date = startDate.replace("-", "/") + " - " + endDate.replace("-", "/");
            effectiveDate.setText(date);
        }

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        ArrayList<EbPolicyDetailChild> chList = groups.get(groupPosition)
                .getEbPolicyDetailChild();
        return chList.size();
    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.eb_policy_details_group,
                    null);
        }

        EbPolicyDetailGroup groupObj = groups.get(groupPosition);
        TextView staffNameText = (TextView) convertView.findViewById(R.id.eb_staff_name);
        ImageView indicator = (ImageView) convertView.findViewById(R.id.eb_group_indicator);
//        ImageView group_icon = (ImageView) convertView.findViewById(R.id.group_icon);

//        int childCount = groupObj.getMemberDetailsChild().size();
//        String gender = groupObj.getGender();
//        if ((gender == null) || (gender.equals("")) || (gender.equals("null"))) {
//            group_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.group_icon));
//        } else {
//            if (gender.equalsIgnoreCase("M")) {
//                group_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.male));
//            } else if (gender.equalsIgnoreCase("F")) {
//                group_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.female));
//            } else {
//                group_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.group_icon));
//            }
//        }

        String staffName = groupObj.getMemberName();
        if ((staffName == null) || (staffName.equals("")) || (staffName.equals("null"))) {
            staffNameText.setText("");
        } else {
            staffNameText.setText(staffName + " (" + groupObj.getRelationship() + ")");
        }

        String relationShip = groupObj.getRelationship();
        if (relationShip.equalsIgnoreCase("P")) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.lightGrayTint));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }

        if (isExpanded)
            indicator.setImageResource(R.drawable.group_indicator);
        else
            indicator.setImageResource(R.drawable.group_indicator_closed);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}