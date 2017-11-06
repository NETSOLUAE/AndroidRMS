package rmsllcoman.com.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rmsllcoman.com.Model.PolicyChild;
import rmsllcoman.com.Model.PolicyGroup;
import rmsllcoman.com.R;

/**
 * Created by macmini on 7/18/17.
 */

public class PolicyAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private ArrayList<PolicyGroup> groups;

    public PolicyAdapter(Activity context, ArrayList<PolicyGroup> groups) {
        this.context = context;
        this.groups = groups;
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
            convertView = inflater.inflate(R.layout.policy_child, null);
        }
        PolicyGroup groupObj = groups.get(groupPosition);
        PolicyChild child = groupObj.getPolicyChild().get(
                childPosition);
        TextView policy_no = (TextView) convertView.findViewById(R.id.policy_no);
        TextView policy_period = (TextView) convertView.findViewById(R.id.policy_period);

        String number = child.getNumber();
        if ((number == null) || (number.equals("")) || (number.equals("null"))) {
            policy_no.setText("");
        } else {
            policy_no.setText(number);
        }

        String startDate = child.getStartDate();
        String expiryDate = child.getExpiryDate();
        if ((startDate == null) || (expiryDate == null) || (startDate.equals("")) || (startDate.equals("null"))) {
            policy_period.setText("");
        } else {
            policy_period.setText(startDate + " - " + expiryDate);
        }

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        ArrayList<PolicyChild> chList = groups.get(groupPosition)
                .getPolicyChild();
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
            convertView = infalInflater.inflate(R.layout.policy_group,
                    null);
        }

        PolicyGroup groupObj = groups.get(groupPosition);
        TextView staffNameText = (TextView) convertView.findViewById(R.id.insurer_name);
        ImageView indicator = (ImageView) convertView.findViewById(R.id.group_indicator_policy);

        String staffName = groupObj.getName();
        if ((staffName == null) || (staffName.equals("")) || (staffName.equals("null"))) {
            staffNameText.setText("");
        } else {
            staffNameText.setText(staffName);
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
