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

import rmsllcoman.com.Model.ContactDetailsAr;
import rmsllcoman.com.Model.ContactLocationAr;
import rmsllcoman.com.R;

/**
 * Created by macmini on 8/15/17.
 */

public class ContactInfoArAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private ArrayList<ContactLocationAr> groups;

    public ContactInfoArAdapter(Activity context, ArrayList<ContactLocationAr> groups) {
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
            convertView = inflater.inflate(R.layout.contact_info_child_ar, null);
        }
        ContactLocationAr groupObj = groups.get(groupPosition);
        ContactDetailsAr child = groupObj.getContactDetails_ar().get(
                childPosition);
        TextView branchNameText = (TextView) convertView.findViewById(R.id.branch_name_ar);
        TextView attentionText = (TextView) convertView.findViewById(R.id.attention_ar);
        TextView addressText = (TextView) convertView.findViewById(R.id.address_ar);
        TextView po_boxText = (TextView) convertView.findViewById(R.id.po_box_ar);
        TextView postal_codeText = (TextView) convertView.findViewById(R.id.postal_code_ar);
        TextView telephoneText = (TextView) convertView.findViewById(R.id.telephone_ar);
        TextView mobileText = (TextView) convertView.findViewById(R.id.mobile_ar);
        TextView faxText = (TextView) convertView.findViewById(R.id.fax_ar);
        TextView emailText = (TextView) convertView.findViewById(R.id.email_ar);
        TextView hotLineText = (TextView) convertView.findViewById(R.id.hot_lines_ar);

        LinearLayout telephoneLayout = (LinearLayout) convertView.findViewById(R.id.telephoneLayout_ar);
        LinearLayout faxLayout = (LinearLayout) convertView.findViewById(R.id.faxLayout_ar);
        LinearLayout emailLayout = (LinearLayout) convertView.findViewById(R.id.emailLayout_ar);
        LinearLayout hotlineLayout = (LinearLayout) convertView.findViewById(R.id.hotLineLayout_ar);
        LinearLayout mobileLayout = (LinearLayout) convertView.findViewById(R.id.mobileLayout_ar);

        String branchName = child.getBranchName_ar();
        if (branchName.contains("\r\n")){
            branchName = branchName.replace("\r\n","");
        }
        if ((branchName == null) || (branchName.equals("")) || (branchName.equals("null"))) {
            branchNameText.setText("");
        } else {
            branchNameText.setText(branchName);
        }

        String attention = child.getattention_ar();
        if ((attention == null) || (attention.equals("")) || (attention.equals("null"))) {
            attentionText.setText("");
            attentionText.setVisibility(View.GONE);
        } else {
            attentionText.setVisibility(View.VISIBLE);
            attentionText.setText(String.format("%s ,", attention));
        }

        String address = child.getAddress_ar();
        if ((address == null) || (address.equals("")) || (address.equals("null"))) {
            addressText.setText("");
            addressText.setVisibility(View.GONE);
        } else {
            addressText.setVisibility(View.VISIBLE);
            addressText.setText(String.format("%s ,", address));
        }

        String po_box = child.getPO_Box_ar();
        if ((po_box == null) || (po_box.equals("")) || (po_box.equals("null"))) {
            po_boxText.setText("");
            po_boxText.setVisibility(View.GONE);
        } else {
            po_boxText.setVisibility(View.VISIBLE);
            po_boxText.setText(String.format("%s%s ,", "ص.ب. ",po_box));
        }

        String postal_code = child.getPostalCode_ar();
        if ((postal_code == null) || (postal_code.equals("")) || (postal_code.equals("null"))) {
            postal_codeText.setText("");
            postal_codeText.setVisibility(View.GONE);
        } else {
            postal_codeText.setVisibility(View.VISIBLE);
            postal_codeText.setText(String.format("%s%s ,", "الرمز البريدي ",postal_code));
        }

        String telephone = child.getTelephone_ar();
        if ((telephone == null) || (telephone.equals("")) || (telephone.equals("null"))) {
            telephoneText.setText("");
            telephoneLayout.setVisibility(View.GONE);
        } else {
            telephoneLayout.setVisibility(View.VISIBLE);
            telephoneText.setText(telephone);
            telephoneText.setTextColor(context.getResources().getColor(R.color.phone));
            Linkify.addLinks(telephoneText, Patterns.PHONE, "tel:");
        }

        String mobile = child.getMobileAr();
        if ((mobile == null) || (mobile.equals("")) || (mobile.equals("null"))) {
            mobileText.setText("");
            mobileLayout.setVisibility(View.GONE);
        } else {
            mobileLayout.setVisibility(View.VISIBLE);
            mobileText.setText(mobile);
            mobileText.setTextColor(context.getResources().getColor(R.color.phone));
            Linkify.addLinks(mobileText, Patterns.PHONE, "tel:");
        }

        String fax = child.getFax_ar();
        if ((fax == null) || (fax.equals("")) || (fax.equals("null"))) {
            faxText.setText("");
            faxLayout.setVisibility(View.GONE);
        } else {
            faxLayout.setVisibility(View.VISIBLE);
            faxText.setText(fax);
        }

        String email = child.getEmail_ar();
        if ((email == null) || (email.equals("")) || (email.equals("null"))) {
            emailText.setText("");
            emailLayout.setVisibility(View.GONE);
        } else {
            emailLayout.setVisibility(View.VISIBLE);
            emailText.setText(email);
            emailText.setTextColor(context.getResources().getColor(R.color.email));
            Linkify.addLinks(emailText, Linkify.EMAIL_ADDRESSES);
        }

        String hot_lines = child.getHot_lines_ar();
        if ((hot_lines == null) || (hot_lines.equals("")) || (hot_lines.equals("null"))) {
            hotLineText.setText("");
            hotlineLayout.setVisibility(View.GONE);
        } else {
            hotlineLayout.setVisibility(View.VISIBLE);
            hotLineText.setText(hot_lines);
            hotLineText.setTextColor(context.getResources().getColor(R.color.phone));
            Linkify.addLinks(hotLineText, Patterns.PHONE, "tel:");
        }

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        ArrayList<ContactDetailsAr> chList = groups.get(groupPosition)
                .getContactDetails_ar();
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
            convertView = infalInflater.inflate(R.layout.contact_info_group_ar,
                    null);
        }

        ContactLocationAr groupObj = groups.get(groupPosition);
        TextView countryNameText = (TextView) convertView.findViewById(R.id.country_name_ar);
        ImageView indicator = (ImageView) convertView.findViewById(R.id.group_indicator_contact_ar);

        String location = groupObj.getLocation_ar();
        if ((location == null) || (location.equals("")) || (location.equals("null"))) {
            countryNameText.setText("");
        } else {
            countryNameText.setText(location);
        }

        if (isExpanded)
            indicator.setImageResource(R.drawable.group_indicator);
        else
            indicator.setImageResource(R.drawable.group_indicator_closed_ar);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}