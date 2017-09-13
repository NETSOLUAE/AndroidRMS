package rms.netsol.com.rmsystem.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import rms.netsol.com.rmsystem.Model.MemberDetailsChild;
import rms.netsol.com.rmsystem.Model.MemberDetailsStaff;
import rms.netsol.com.rmsystem.R;
import rms.netsol.com.rmsystem.View.Activity.MemberDetailsActivity;

/**
 * Created by macmini on 6/7/17.
 */

public class MemberDetailAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private ArrayList<MemberDetailsStaff> groups;
    public AlertDialog alertDialog;

    public MemberDetailAdapter(Activity context, ArrayList<MemberDetailsStaff> groups) {
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
            convertView = inflater.inflate(R.layout.member_detail_child, null);
        }
        MemberDetailsStaff groupObj = groups.get(groupPosition);
        MemberDetailsChild child = groupObj.getMemberDetailsChild().get(
                childPosition);
        TextView dependentName = (TextView) convertView.findViewById(R.id.dependent_name);
        TextView relationshipText = (TextView) convertView.findViewById(R.id.dependent_relation);
        ImageView child_icon = (ImageView) convertView.findViewById(R.id.child_icon);

        String memberName = child.getMemberName();
        if ((memberName == null) || (memberName.equals("")) || (memberName.equals("null"))) {
            dependentName.setText("");
        } else {
            dependentName.setText(memberName);
        }

        String relationship = child.getRelationship();
        if ((relationship == null) || (relationship.equals("")) || (relationship.equals("null"))) {
            relationshipText.setText("");
        } else {
            if (relationship.equalsIgnoreCase("S")){
                relationshipText.setText("Son");
                child_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.child_male));
            } else if (relationship.equalsIgnoreCase("W")) {
                relationshipText.setText("Wife");
                child_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.female));
            } else if (relationship.equalsIgnoreCase("D")) {
                relationshipText.setText("Daughter");
                child_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.child_female));
            } else if (relationship.equalsIgnoreCase("H")) {
                relationshipText.setText("Daughter");
                child_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.male));
            } else {
                relationshipText.setText(relationship);
                child_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.child_icon));
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//                LayoutInflater inflater = context.getLayoutInflater();
//                final View dialogView = inflater.inflate(R.layout.member_details_dialog, null);
//                dialogBuilder.setView(dialogView);
//
////                final EditText nationalID = (EditText) dialogView.findViewById(R.id.nationalID);
//
//                dialogBuilder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
////                        String nationalID1 = nationalID.getText().toString();
////                        if (nationalID1.length() == 0)  {
////                            Toast.makeText(getBaseContext(), "Please Enter Your NationalID", Toast.LENGTH_LONG).show();
////                            showForgetDialog(isMandatory);
////                        } else {
////                            resetPin("eb");
////                        }
//                    }
//                });
//                dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                AlertDialog b = dialogBuilder.create();
//                b.show();


                MemberDetailsStaff groupObj = groups.get(groupPosition);
                MemberDetailsChild child = groupObj.getMemberDetailsChild().get(
                        childPosition);
//                Toast.makeText(context, "You Clicked " + claimDetails.getClaimNo(), Toast.LENGTH_LONG).show();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.member_details_dialog, null);
                dialogBuilder.setView(dialogView);

                ImageView okButton = (ImageView) dialogView.findViewById(R.id.ok);
                ImageView relationship_dialog = (ImageView) dialogView.findViewById(R.id.relationship_dialog);
                TextView policyRef = (TextView) dialogView.findViewById(R.id.policy_ref_no_dialog);
                TextView name = (TextView) dialogView.findViewById(R.id.name_dialog);
                TextView relation = (TextView) dialogView.findViewById(R.id.relation_dialog);
                TextView gender = (TextView) dialogView.findViewById(R.id.gender_dialog);
                TextView nationality = (TextView) dialogView.findViewById(R.id.nationality_dialog);
                TextView mobile = (TextView) dialogView.findViewById(R.id.mobile_dialog);
                TextView email = (TextView) dialogView.findViewById(R.id.email_dialog);
                ScrollView scrollView = (ScrollView) dialogView.findViewById(R.id.claim_dialog_scroll);

                alertDialog = dialogBuilder.create();

                if (child.getPolicyRef().equals("null") || child.getPolicyRef().equals("")) {
                    policyRef.setText("");
                } else {
                    policyRef.setText(child.getPolicyRef());
                }

                if (child.getMemberName().equals("null") || child.getMemberName().equals("")) {
                    name.setText("");
                } else {
                    name.setText(child.getMemberName());
                }

                if (child.getGender().equals("null") || child.getGender().equals("")) {
                    gender.setText("");
                } else {
                    String gender1 = child.getGender();
                    if (gender1.equalsIgnoreCase("M")) {
                        gender.setText("Male");
                    } else if (gender1.equalsIgnoreCase("F")) {
                        gender.setText("Female");
                    } else {
                        gender.setText(gender1);
                    }
                }

                if (child.getNationality().equals("null") || child.getNationality().equals("")) {
                    nationality.setText("");
                } else {
                    nationality.setText(child.getNationality());
                }

                if (child.getPhoneNumber().equals("null") || child.getPhoneNumber().equals("")) {
                    mobile.setText("");
                } else {
                    mobile.setText(child.getPhoneNumber());
                }

                if (child.getEmail().equals("null") || child.getEmail().equals("")) {
                    email.setText("");
                } else {
                    email.setText(child.getEmail());
                }

                if (child.getRelationship().equals("null") || child.getRelationship().equals("")) {
                    relation.setText("");
                } else {
                    String relationship = child.getRelationship();
                    if (relationship.equalsIgnoreCase("S")){
                        relation.setText("Son");
                        relationship_dialog.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.child_male));
                    } else if (relationship.equalsIgnoreCase("W")) {
                        relation.setText("Wife");
                        relationship_dialog.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.female));
                    } else if (relationship.equalsIgnoreCase("D")) {
                        relation.setText("Daughter");
                        relationship_dialog.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.child_female));
                    } else if (relationship.equalsIgnoreCase("H")) {
                        relation.setText("Daughter");
                        relationship_dialog.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.male));
                    } else {
                        relation.setText(relationship);
                        relationship_dialog.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.child_icon));
                    }
                }

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(alertDialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                alertDialog.show();
                alertDialog.getWindow().setAttributes(lp);
                alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_shadow));

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                scrollView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        MemberDetailsActivity.setLastUsed();
                        return false;
                    }
                });
            }
        });

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        ArrayList<MemberDetailsChild> chList = groups.get(groupPosition)
                .getMemberDetailsChild();
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
            convertView = infalInflater.inflate(R.layout.member_detail_group,
                    null);
        }

        MemberDetailsStaff groupObj = groups.get(groupPosition);
        TextView staffNameText = (TextView) convertView.findViewById(R.id.staff_name);
        ImageView indicator = (ImageView) convertView.findViewById(R.id.group_indicator);
        ImageView group_icon = (ImageView) convertView.findViewById(R.id.group_icon);

        int childCount = groupObj.getMemberDetailsChild().size();
        if (childCount > 0) {
            group_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.family));
        } else {
            if (groupObj.getGender().equalsIgnoreCase("M")) {
                group_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.male));
            } else if (groupObj.getGender().equalsIgnoreCase("F")) {
                group_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.female));
            } else {
                group_icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.group_icon));
            }
        }

        String staffName = groupObj.getMemberName();
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
