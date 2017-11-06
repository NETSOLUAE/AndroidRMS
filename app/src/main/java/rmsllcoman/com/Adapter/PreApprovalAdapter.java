package rmsllcoman.com.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import rmsllcoman.com.Model.PreApproval;
import rmsllcoman.com.Activity.PreApprovalActivity;
import rmsllcoman.com.R;
import rmsllcoman.com.Util.DatabaseManager;

/**
 * Created by macmini on 6/8/17.
 */

public class PreApprovalAdapter extends BaseAdapter implements Filterable {
    private ArrayList<PreApproval> preApprovalList;
    private ArrayList<PreApproval> orig;
    Context context;
    public AlertDialog alertDialogPre;
    private static LayoutInflater inflater = null;

    public PreApprovalAdapter(PreApprovalActivity preApprovalContext, ArrayList<PreApproval> preApprovalListA) {
        // TODO Auto-generated constructor stub
        preApprovalList = preApprovalListA;
        context = preApprovalContext;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alertDialogPre = null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return preApprovalList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return preApprovalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<PreApproval> results = new ArrayList<PreApproval>();
                if (orig == null)
                    orig = preApprovalList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final PreApproval g : orig) {
                            if (g.getPatientName().toLowerCase().contains(constraint.toString().toLowerCase())){
                                results.add(g);
                            }
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                preApprovalList = (ArrayList<PreApproval>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    public class Holder {
        TextView preApprovalPolicyRef;
        TextView preApprovalRegDate;
        TextView preApprovalStatusList;
        TextView preApprovalPatientName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        DatabaseManager databaseManager = new DatabaseManager(context);
        String staffName = databaseManager.getName();
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.pre_approval_view, null);
        PreApproval preApprovalDetails = (PreApproval) getItem(position);

        holder.preApprovalPolicyRef = (TextView) rowView.findViewById(R.id.preapproval_policy_list);
        holder.preApprovalRegDate = (TextView) rowView.findViewById(R.id.preapproval_entrydate_list);
        holder.preApprovalStatusList = (TextView) rowView.findViewById(R.id.preapproval_status_list);
        holder.preApprovalPatientName = (TextView) rowView.findViewById(R.id.preapproval_patient_name_list);

        if (preApprovalDetails.getPolicyRefNo().equals("null") || preApprovalDetails.getPolicyRefNo().equals("")) {
            holder.preApprovalPolicyRef.setText("");
        } else {
            holder.preApprovalPolicyRef.setText(preApprovalDetails.getPolicyRefNo());
        }

        if (preApprovalDetails.getEntryDate().equals("null") || preApprovalDetails.getEntryDate().equals("")) {
            holder.preApprovalRegDate.setText("");
        } else {
            String entryDate = preApprovalDetails.getEntryDate();
            int i = entryDate.indexOf(" ");
            entryDate = entryDate.substring(0,i);

            holder.preApprovalRegDate.setText(entryDate);
        }

        if (preApprovalDetails.getPatientName().equals("null") || preApprovalDetails.getPatientName().equals("")) {
            holder.preApprovalPatientName.setText("");
        } else {
            if (staffName.equalsIgnoreCase(preApprovalDetails.getPatientName())) {
                holder.preApprovalPatientName.setText(String.format("%s (P)", preApprovalDetails.getPatientName()));
            } else {
                holder.preApprovalPatientName.setText(String.format("%s (D)", preApprovalDetails.getPatientName()));
            }
        }

        if (preApprovalDetails.getStatus().equals("null") || preApprovalDetails.getStatus().equals("")) {
            holder.preApprovalStatusList.setText("");
        } else {
            holder.preApprovalStatusList.setText(preApprovalDetails.getStatus());
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                PreApproval preApprovalDetails = (PreApproval) getItem(position);
//                Toast.makeText(context, "You Clicked " + preApprovalDetails.getPatientName(), Toast.LENGTH_LONG).show();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.pre_approval_dialog, null);
                dialogBuilder.setView(dialogView);

                ImageView okButton = (ImageView) dialogView.findViewById(R.id.ok_preapproval);
                TextView patientID = (TextView) dialogView.findViewById(R.id.patient_id_dialog);
                TextView patientName = (TextView) dialogView.findViewById(R.id.patient_name_dialog);
                TextView staffID = (TextView) dialogView.findViewById(R.id.staff_id_dialog);
                TextView staffName = (TextView) dialogView.findViewById(R.id.staff_name_dialog);
                TextView policyRef = (TextView) dialogView.findViewById(R.id.policy_refno_dialog);
                TextView entryDate = (TextView) dialogView.findViewById(R.id.entry_date_dialog);
                TextView diagnosis = (TextView) dialogView.findViewById(R.id.diagnosis_dialog_pre);
                TextView place = (TextView) dialogView.findViewById(R.id.place_dialog);
                TextView hospitalName = (TextView) dialogView.findViewById(R.id.hospital_name_dialog);
                TextView status = (TextView) dialogView.findViewById(R.id.status_dialog_pre);
                TextView preApprovalNo = (TextView) dialogView.findViewById(R.id.preapproval_no_dialog);
                TextView preApprovalDate = (TextView) dialogView.findViewById(R.id.preapproval_date_dialog);
                TextView remarks = (TextView) dialogView.findViewById(R.id.remarks_dialog_pre);
                ScrollView scrollView = (ScrollView) dialogView.findViewById(R.id.preapproval_dialog_scroll);
                alertDialogPre = dialogBuilder.create();

                if (preApprovalDetails.getPatientId().equals("null") || preApprovalDetails.getPatientId().equals("")) {
                    patientID.setText("");
                } else {
                    patientID.setText(preApprovalDetails.getPatientId());
                }

                if (preApprovalDetails.getPatientName().equals("null") || preApprovalDetails.getPatientName().equals("")) {
                    patientName.setText("");
                } else {
                    patientName.setText(preApprovalDetails.getPatientName());
                }

                if (preApprovalDetails.getStaffID().equals("null") || preApprovalDetails.getStaffID().equals("")) {
                    staffID.setText("");
                } else {
                    staffID.setText(preApprovalDetails.getStaffID());
                }

                if (preApprovalDetails.getStaffName().equals("null") || preApprovalDetails.getStaffName().equals("")) {
                    staffName.setText("");
                } else {
                    staffName.setText(preApprovalDetails.getStaffName());
                }

                if (preApprovalDetails.getPolicyRefNo().equals("null") || preApprovalDetails.getPolicyRefNo().equals("")) {
                    policyRef.setText("");
                } else {
                    policyRef.setText(preApprovalDetails.getPolicyRefNo());
                }

                if (preApprovalDetails.getEntryDate().equals("null") || preApprovalDetails.getEntryDate().equals("")) {
                    entryDate.setText("");
                } else {
                    entryDate.setText(preApprovalDetails.getEntryDate());
                }

                if (preApprovalDetails.getDiagnosis().equals("null") || preApprovalDetails.getDiagnosis().equals("")) {
                    diagnosis.setText("");
                } else {
                    diagnosis.setText(preApprovalDetails.getDiagnosis());
                }

                if (preApprovalDetails.getPlace().equals("null") || preApprovalDetails.getPlace().equals("")) {
                    place.setText("");
                } else {
                    place.setText(preApprovalDetails.getPlace());
                }

                if (preApprovalDetails.getHospitalName().equals("null") || preApprovalDetails.getHospitalName().equals("")) {
                    hospitalName.setText("");
                } else {
                    hospitalName.setText(preApprovalDetails.getHospitalName());
                }

                if (preApprovalDetails.getStatus().equals("null") || preApprovalDetails.getStatus().equals("")) {
                    status.setText("");
                } else {
                    status.setText(preApprovalDetails.getStatus());
                }

                if (preApprovalDetails.getPreApprovalNo().equals("null") || preApprovalDetails.getPreApprovalNo().equals("")) {
                    preApprovalNo.setText("");
                } else {
                    preApprovalNo.setText(preApprovalDetails.getPreApprovalNo());
                }

                if (preApprovalDetails.getPreApprovalDate().equals("null") || preApprovalDetails.getPreApprovalDate().equals("")) {
                    preApprovalDate.setText("");
                } else {
                    preApprovalDate.setText(preApprovalDetails.getPreApprovalDate());
                }

                if (preApprovalDetails.getRemarks().equals("null") || preApprovalDetails.getRemarks().equals("")) {
                    remarks.setText("");
                } else {
                    remarks.setText(preApprovalDetails.getRemarks());
                }

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(alertDialogPre.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                alertDialogPre.show();
                alertDialogPre.getWindow().setAttributes(lp);
                alertDialogPre.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_shadow));

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialogPre.dismiss();
                    }
                });
                scrollView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        PreApprovalActivity.setLastUsedPre();
                        return false;
                    }
                });
            }
        });
        return rowView;
    }
}