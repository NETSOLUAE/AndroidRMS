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

import rmsllcoman.com.Activity.ClaimStatus;
import rmsllcoman.com.Model.ClaimDetails;
import rmsllcoman.com.R;
import rmsllcoman.com.Util.DatabaseManager;

/**
 * Created by macmini on 6/8/17.
 */

public class ClaimStatusAdapter extends BaseAdapter implements Filterable {
    private ArrayList<ClaimDetails> claimList;
    private ArrayList<ClaimDetails> orig;
    Context context;
    private static LayoutInflater inflater = null;
    public AlertDialog alertDialog;

    public ClaimStatusAdapter(ClaimStatus claimStatusContext, ArrayList<ClaimDetails> claimDetails) {
        // TODO Auto-generated constructor stub
        claimList = claimDetails;
        context = claimStatusContext;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alertDialog = null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return claimList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return claimList.get(position);
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
                final ArrayList<ClaimDetails> results = new ArrayList<ClaimDetails>();
                if (orig == null)
                    orig = claimList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final ClaimDetails g : orig) {
                            if (g.getClaimNo().toLowerCase().contains(constraint.toString().toLowerCase())){
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
                claimList = (ArrayList<ClaimDetails>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public class Holder {
        TextView claimNoList;
        TextView claimRegDate;
        TextView claimStatusList;
        TextView claimPatientName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        DatabaseManager databaseManager = new DatabaseManager(context);
        String staffName = databaseManager.getName();
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.claim_status_view, null);
        ClaimDetails claimDetails = (ClaimDetails) getItem(position);
        holder.claimNoList = (TextView) rowView.findViewById(R.id.claim_no_list);
        holder.claimRegDate = (TextView) rowView.findViewById(R.id.claim_regdate_list);
        holder.claimStatusList = (TextView) rowView.findViewById(R.id.claim_status_list);
        holder.claimPatientName = (TextView) rowView.findViewById(R.id.claim_patient_name_list);

        if (claimDetails.getClaimNo().equals("null") || claimDetails.getClaimNo().equals("")) {
            holder.claimNoList.setText("");
        } else {
            holder.claimNoList.setText(claimDetails.getClaimNo());
        }

        if (claimDetails.getRegisteredDate().equals("null") || claimDetails.getRegisteredDate().equals("")) {
            holder.claimRegDate.setText("");
        } else {
            holder.claimRegDate.setText(claimDetails.getRegisteredDate());
        }

        if (claimDetails.getPatientName().equals("null") || claimDetails.getPatientName().equals("")) {
            holder.claimPatientName.setText("");
        } else {
            if (claimDetails.getMemberType().equals("null") || claimDetails.getMemberType().equals("")) {
                if (staffName.equalsIgnoreCase(claimDetails.getPatientName())) {
                    holder.claimPatientName.setText(String.format("%s (P)", claimDetails.getPatientName()));
                } else {
                    holder.claimPatientName.setText(String.format("%s (D)", claimDetails.getPatientName()));
                }
            } else {
                holder.claimPatientName.setText(String.format("%s%s%s%s%s", claimDetails.getPatientName(), " ", "(", claimDetails.getMemberType(), ")"));
            }
        }

        if (claimDetails.getStatus().equals("null") || claimDetails.getStatus().equals("")) {
            holder.claimStatusList.setText("");
        } else {
            holder.claimStatusList.setText(claimDetails.getStatus());
            if (claimDetails.getStatus().equalsIgnoreCase("Approved"))
                holder.claimStatusList.setTextColor(context.getResources().getColor(R.color.approved));
            if (claimDetails.getStatus().equalsIgnoreCase("Settled"))
                holder.claimStatusList.setTextColor(context.getResources().getColor(R.color.settled));
            if (claimDetails.getStatus().equalsIgnoreCase("Registered"))
                holder.claimStatusList.setTextColor(context.getResources().getColor(R.color.registered));
            if (claimDetails.getStatus().equalsIgnoreCase("Rejected"))
                holder.claimStatusList.setTextColor(context.getResources().getColor(R.color.cancelled));
            if (claimDetails.getStatus().equalsIgnoreCase("Queried"))
                holder.claimStatusList.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            if (claimDetails.getStatus().equalsIgnoreCase("Cancelled"))
                holder.claimStatusList.setTextColor(context.getResources().getColor(R.color.cancelled));
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ClaimDetails claimDetails = (ClaimDetails) getItem(position);
//                Toast.makeText(context, "You Clicked " + claimDetails.getClaimNo(), Toast.LENGTH_LONG).show();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.claim_satus_dialog, null);
                dialogBuilder.setView(dialogView);

                ImageView okButton = (ImageView) dialogView.findViewById(R.id.ok);
                TextView policyRef = (TextView) dialogView.findViewById(R.id.policy_ref_no);
                TextView claimNo = (TextView) dialogView.findViewById(R.id.claim_no_dialog);
                TextView diagnosis = (TextView) dialogView.findViewById(R.id.diagnosis);
                TextView treatmentDate = (TextView) dialogView.findViewById(R.id.treatment_date);
                TextView status = (TextView) dialogView.findViewById(R.id.status_dialog);
                TextView claimedAmount = (TextView) dialogView.findViewById(R.id.claimed_amount);
                TextView approvedAmount = (TextView) dialogView.findViewById(R.id.approved_amount_dialog);
                TextView excess = (TextView) dialogView.findViewById(R.id.excess_dialog);
                TextView disallowance = (TextView) dialogView.findViewById(R.id.disallowance_dialog);
                TextView settledAmountRo = (TextView) dialogView.findViewById(R.id.settled_amountro_dialog);
                TextView modeOfPayment = (TextView) dialogView.findViewById(R.id.modeofpayment_dialog);
                TextView chequeNo = (TextView) dialogView.findViewById(R.id.cheque_no_dialog);
                TextView settledDate = (TextView) dialogView.findViewById(R.id.settled_amount_dialog);
                TextView remarks = (TextView) dialogView.findViewById(R.id.remarks_dialog);
                ScrollView scrollView = (ScrollView) dialogView.findViewById(R.id.claim_dialog_scroll);

//                okButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.close_icon));

                alertDialog = dialogBuilder.create();

                if (claimDetails.getClaimNo().equals("null") || claimDetails.getClaimNo().equals("")) {
                    claimNo.setText("");
                } else {
                    claimNo.setText(claimDetails.getClaimNo());
                }

                if (claimDetails.getTreatmentDate().equals("null") || claimDetails.getTreatmentDate().equals("")) {
                    treatmentDate.setText("");
                } else {
                    treatmentDate.setText(claimDetails.getTreatmentDate());
                }

                if (claimDetails.getStatus().equals("null") || claimDetails.getStatus().equals("")) {
                    status.setText("");
                } else {
                    status.setText(claimDetails.getStatus());
                }

                if (claimDetails.getClaimedAmount().equals("null") || claimDetails.getClaimedAmount().equals("")) {
                    claimedAmount.setText("");
                } else {
                    claimedAmount.setText(String.format("%s OMR", claimDetails.getClaimedAmount()));
                }

                if (claimDetails.getApprovedAmount().equals("null") || claimDetails.getApprovedAmount().equals("")) {
                    approvedAmount.setText("");
                } else {
                    approvedAmount.setText(String.format("%s OMR", claimDetails.getApprovedAmount()));
                }

                if (claimDetails.getExcess().equals("null") || claimDetails.getExcess().equals("")) {
                    excess.setText("");
                } else {
                    excess.setText(String.format("%s OMR", claimDetails.getExcess()));
                }

                if (claimDetails.getDisallowance().equals("null") || claimDetails.getDisallowance().equals("")) {
                    disallowance.setText("");
                } else {
                    disallowance.setText(String.format("%s OMR", claimDetails.getDisallowance()));
                }

                if (claimDetails.getSettledAmountRO().equals("null") || claimDetails.getSettledAmountRO().equals("")) {
                    settledAmountRo.setText("");
                } else {
                    settledAmountRo.setText(String.format("%s OMR", claimDetails.getSettledAmountRO()));
                }

                if (claimDetails.getModeOfPayment().equals("null") || claimDetails.getModeOfPayment().equals("")) {
                    modeOfPayment.setText("");
                } else {
                    modeOfPayment.setText(claimDetails.getModeOfPayment());
                }

                if (claimDetails.getChequeNo().equals("null") || claimDetails.getChequeNo().equals("")) {
                    chequeNo.setText("");
                } else {
                    chequeNo.setText(claimDetails.getChequeNo());
                }

                if (claimDetails.getSettledAmount().equals("null") || claimDetails.getSettledAmount().equals("")) {
                    settledDate.setText("");
                } else {
                    settledDate.setText(claimDetails.getSettledAmount());
                }

                if (claimDetails.getRemarks().equals("null") || claimDetails.getRemarks().equals("")) {
                    remarks.setText("");
                } else {
                    remarks.setText(claimDetails.getRemarks());
                }

                if (claimDetails.getPolicyRefNo().equals("null") || claimDetails.getPolicyRefNo().equals("")) {
                    policyRef.setText("");
                } else {
                    policyRef.setText(claimDetails.getPolicyRefNo());
                }

                if (claimDetails.getDiagnosis().equals("null") || claimDetails.getDiagnosis().equals("")) {
                    diagnosis.setText("");
                } else {
                    diagnosis.setText(claimDetails.getDiagnosis());
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
                        ClaimStatus.setLastUsed();
                        return false;
                    }
                });
            }
        });
        return rowView;
    }
}
