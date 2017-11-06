package rmsllcoman.com.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import rmsllcoman.com.Model.ContactDetails;
import rmsllcoman.com.Model.ContactLocation;
import rmsllcoman.com.R;

/**
 * Created by macmini on 6/24/17.
 */

public class ContactInfoAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private ArrayList<ContactLocation> groups;

    public ContactInfoAdapter(Activity context, ArrayList<ContactLocation> groups) {
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
            convertView = inflater.inflate(R.layout.contact_info_child, null);
        }
        ContactLocation groupObj = groups.get(groupPosition);
        ContactDetails child = groupObj.getContactDetails().get(
                childPosition);
        TextView branchNameText = (TextView) convertView.findViewById(R.id.branch_name);
        TextView attentionText = (TextView) convertView.findViewById(R.id.attention);
        TextView addressText = (TextView) convertView.findViewById(R.id.address);
        TextView po_boxText = (TextView) convertView.findViewById(R.id.po_box);
        TextView postal_codeText = (TextView) convertView.findViewById(R.id.postal_code);
        TextView telephoneText = (TextView) convertView.findViewById(R.id.telephone);
        TextView mobileText = (TextView) convertView.findViewById(R.id.mobile);
        TextView faxText = (TextView) convertView.findViewById(R.id.fax);
        TextView emailText = (TextView) convertView.findViewById(R.id.email);
        TextView hotLineText = (TextView) convertView.findViewById(R.id.hot_lines);
        TextView directLineText = (TextView) convertView.findViewById(R.id.direct_lines);

        LinearLayout telephoneLayout = (LinearLayout) convertView.findViewById(R.id.telephoneLayout);
        LinearLayout mobileLayout = (LinearLayout) convertView.findViewById(R.id.mobileLayout);
        LinearLayout faxLayout = (LinearLayout) convertView.findViewById(R.id.faxLayout);
        LinearLayout emailLayout = (LinearLayout) convertView.findViewById(R.id.emailLayout);
        LinearLayout hotlineLayout = (LinearLayout) convertView.findViewById(R.id.hotLineLayout);
        LinearLayout directLineLayout = (LinearLayout) convertView.findViewById(R.id.directLineLayout);

        String branchName = child.getBranchName();
        if (branchName.contains("\r\n")){
            branchName = branchName.replace("\r\n","");
        }
        if ((branchName == null) || (branchName.equals("")) || (branchName.equals("null"))) {
            branchNameText.setText("");
        } else {
            branchNameText.setText(branchName);
        }

        String attention = child.getattention();
        if ((attention == null) || (attention.equals("")) || (attention.equals("null"))) {
            attentionText.setText("");
            attentionText.setVisibility(View.GONE);
        } else {
            attentionText.setVisibility(View.VISIBLE);
            attentionText.setText(String.format("%s ,", attention));
        }

        String address = child.getAddress();
        if ((address == null) || (address.equals("")) || (address.equals("null"))) {
            addressText.setText("");
            addressText.setVisibility(View.GONE);
        } else {
            addressText.setVisibility(View.VISIBLE);
            addressText.setText(String.format("%s ,", address));
        }

        String po_box = child.getPO_Box();
        if ((po_box == null) || (po_box.equals("")) || (po_box.equals("null"))) {
            po_boxText.setText("");
            po_boxText.setVisibility(View.GONE);
        } else {
            po_boxText.setVisibility(View.VISIBLE);
            po_boxText.setText(String.format("%s%s ,", "PO.Box ",po_box));
        }

        String postal_code = child.getPostalCode();
        if ((postal_code == null) || (postal_code.equals("")) || (postal_code.equals("null"))) {
            postal_codeText.setText("");
            postal_codeText.setVisibility(View.GONE);
        } else {
            postal_codeText.setVisibility(View.VISIBLE);
            postal_codeText.setText(String.format("%s%s ,", "Postal Code ",postal_code));
        }

        String telephone = child.getTelephone();
        if ((telephone == null) || (telephone.equals("")) || (telephone.equals("null"))) {
            telephoneText.setText("");
            telephoneLayout.setVisibility(View.GONE);
        } else {
            telephoneLayout.setVisibility(View.VISIBLE);
            telephoneText.setText(telephone);
            telephoneText.setTextColor(context.getResources().getColor(R.color.phone));
            Linkify.addLinks(telephoneText, Patterns.PHONE, "tel:");
        }

        String mobile = child.getMobile();
        if ((mobile == null) || (mobile.equals("")) || (mobile.equals("null"))) {
            mobileText.setText("");
            mobileLayout.setVisibility(View.GONE);
        } else {
            telephoneLayout.setVisibility(View.VISIBLE);
            mobileText.setText(mobile);
            mobileText.setTextColor(context.getResources().getColor(R.color.phone));
            Linkify.addLinks(mobileText, Patterns.PHONE, "tel:");
        }

        String fax = child.getFax();
        if ((fax == null) || (fax.equals("")) || (fax.equals("null"))) {
            faxText.setText("");
            faxLayout.setVisibility(View.GONE);
        } else {
            faxLayout.setVisibility(View.VISIBLE);
            faxText.setText(fax);
        }

        String email = child.getEmail();
        if ((email == null) || (email.equals("")) || (email.equals("null"))) {
            emailText.setText("");
            emailLayout.setVisibility(View.GONE);
        } else {
            emailLayout.setVisibility(View.VISIBLE);
            emailText.setText(email);
            emailText.setTextColor(context.getResources().getColor(R.color.email));
            Linkify.addLinks(emailText, Linkify.EMAIL_ADDRESSES);
        }

        String hot_lines = child.getHot_lines();
        if ((hot_lines == null) || (hot_lines.equals("")) || (hot_lines.equals("null"))) {
            hotLineText.setText("");
            hotlineLayout.setVisibility(View.GONE);
        } else {
            hotlineLayout.setVisibility(View.VISIBLE);
            hotLineText.setText(hot_lines);
            hotLineText.setTextColor(context.getResources().getColor(R.color.phone));
            Linkify.addLinks(hotLineText, Patterns.PHONE, "tel:");
        }

        String direct_line = child.getDirect_lines();
        if ((direct_line == null) || (direct_line.equals("")) || (direct_line.equals("null"))) {
            directLineText.setText("");
            directLineLayout.setVisibility(View.GONE);
        } else {
            directLineLayout.setVisibility(View.VISIBLE);
            directLineText.setText(direct_line);
            directLineText.setTextColor(context.getResources().getColor(R.color.phone));
            Linkify.addLinks(directLineText, Patterns.PHONE, "tel:");
        }

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        ArrayList<ContactDetails> chList = groups.get(groupPosition)
                .getContactDetails();
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
            convertView = infalInflater.inflate(R.layout.contact_info_group,
                    null);
        }

        ContactLocation groupObj = groups.get(groupPosition);
        TextView countryNameText = (TextView) convertView.findViewById(R.id.country_name);
        ImageView indicator = (ImageView) convertView.findViewById(R.id.group_indicator_contact);

        String location = groupObj.getLocation();
        if ((location == null) || (location.equals("")) || (location.equals("null"))) {
            countryNameText.setText("");
        } else {
            countryNameText.setText(location);
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