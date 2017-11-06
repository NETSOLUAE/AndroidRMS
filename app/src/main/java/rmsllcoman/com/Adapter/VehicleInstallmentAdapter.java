package rmsllcoman.com.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import rmsllcoman.com.Model.InstallmentHistory;
import rmsllcoman.com.R;

/**
 * Created by macmini on 8/3/17.
 */

public class VehicleInstallmentAdapter extends BaseAdapter{
    private ArrayList<InstallmentHistory> vehicleList;
    Context context;
    private static LayoutInflater inflater = null;
    AlertDialog alertDialog;

    public VehicleInstallmentAdapter(Activity vehicleContext, ArrayList<InstallmentHistory> vehicleDetails) {
        // TODO Auto-generated constructor stub
        vehicleList = vehicleDetails;
        context = vehicleContext;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alertDialog = null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return vehicleList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return vehicleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public class Holder {
        TextView intallmentPaidDate;
        TextView intallmentPaidAmount;
        TextView intallmentPaidStatus;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.installment_list_row, null);
        InstallmentHistory vehicleDetails = (InstallmentHistory) getItem(position);
        holder.intallmentPaidDate = (TextView) rowView.findViewById(R.id.intallmentPaidDate);
        holder.intallmentPaidAmount = (TextView) rowView.findViewById(R.id.intallmentPaidAmount);
        holder.intallmentPaidStatus = (TextView) rowView.findViewById(R.id.intallmentPaidSatus);

        if (vehicleDetails.getDate().equals("null") || vehicleDetails.getDate().equals("")) {
            holder.intallmentPaidDate.setText("");
        } else {
            holder.intallmentPaidDate.setText(vehicleDetails.getDate());
        }

        if (vehicleDetails.getAmount().equals("null") || vehicleDetails.getAmount().equals("")) {
            holder.intallmentPaidAmount.setText("");
        } else {
            holder.intallmentPaidAmount.setText(String.format("%s OMR", vehicleDetails.getAmount()));
        }

        if (vehicleDetails.getStatus().equals("null") || vehicleDetails.getStatus().equals("")) {
            holder.intallmentPaidStatus.setText("");
        } else {
            holder.intallmentPaidStatus.setText(vehicleDetails.getStatus());
            if (vehicleDetails.getStatus().equalsIgnoreCase("Paid"))
                holder.intallmentPaidStatus.setTextColor(context.getResources().getColor(R.color.settled));
            if (vehicleDetails.getStatus().equalsIgnoreCase("Unpaid"))
                holder.intallmentPaidStatus.setTextColor(context.getResources().getColor(R.color.cancelled));
        }
        return rowView;
    }
}
