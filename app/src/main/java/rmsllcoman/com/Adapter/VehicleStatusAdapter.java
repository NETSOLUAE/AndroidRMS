package rmsllcoman.com.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import rmsllcoman.com.Model.AccountSummary;
import rmsllcoman.com.R;

/**
 * Created by macmini on 7/17/17.
 */

public class VehicleStatusAdapter extends BaseAdapter implements Filterable {
    private ArrayList<AccountSummary> vehicleList;
    public ArrayList<AccountSummary> orig;
    public AccountSummary filterList;
    Context context;
    private static LayoutInflater inflater = null;
    AlertDialog alertDialog;

    public VehicleStatusAdapter(Activity vehicleContext, ArrayList<AccountSummary> vehicleDetails) {
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

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<AccountSummary> results = new ArrayList<>();
                if (orig == null)
                    orig = vehicleList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final AccountSummary g : orig) {
                            if (g.getVehicleNo().toLowerCase().contains(constraint.toString().toLowerCase())){
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
                vehicleList = (ArrayList<AccountSummary>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public class Holder {
        TextView vehicleNoList;
        TextView vehicleExpDate;
        TextView vehicleStatusList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.vehicle_list_view, null);
        AccountSummary vehicleDetails = (AccountSummary) getItem(position);
        filterList = (AccountSummary) getItem(position);
        holder.vehicleNoList = (TextView) rowView.findViewById(R.id.vehicle_no_list);
        holder.vehicleExpDate = (TextView) rowView.findViewById(R.id.vehicle_expdate_list);
        holder.vehicleStatusList = (TextView) rowView.findViewById(R.id.vehicle_status_list);

        if (vehicleDetails.getVehicleNo().equals("null") || vehicleDetails.getVehicleNo().equals("")) {
            holder.vehicleNoList.setText("");
        } else {
            holder.vehicleNoList.setText(vehicleDetails.getVehicleNo());
        }

        if (vehicleDetails.getEndDate().equals("null") || vehicleDetails.getEndDate().equals("")) {
            holder.vehicleExpDate.setText("");
        } else {
            holder.vehicleExpDate.setText(vehicleDetails.getEndDate());
        }

        if (vehicleDetails.getStatus().equals("null") || vehicleDetails.getStatus().equals("")) {
            holder.vehicleStatusList.setText("");
        } else {
            holder.vehicleStatusList.setText(vehicleDetails.getStatus());
            if (vehicleDetails.getStatus().equalsIgnoreCase("Active"))
                holder.vehicleStatusList.setTextColor(context.getResources().getColor(R.color.settled));
            if (vehicleDetails.getStatus().equalsIgnoreCase("InActive"))
                holder.vehicleStatusList.setTextColor(context.getResources().getColor(R.color.cancelled));
        }
        return rowView;
    }
}