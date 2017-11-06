package rmsllcoman.com.Adapter;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import rmsllcoman.com.Util.FileDownloader;
import rmsllcoman.com.Activity.Information;
import rmsllcoman.com.Model.PdfLinks;
import rmsllcoman.com.R;

/**
 * Created by macmini on 6/6/17.
 */

public class InformationAdapter extends BaseAdapter {
    private ArrayList<PdfLinks> pdfLinks;
    Context context;
    private static LayoutInflater inflater = null;
    private File pdfFile = null;
    private ProgressDialog mProgressDialog;
    // declare the dialog as a member_details_menu1 field of your activity

    public InformationAdapter(Information information, ArrayList<PdfLinks> pdfLinks) {
        // TODO Auto-generated constructor stub
        this.pdfLinks = pdfLinks;
        context = information;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pdfLinks.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return pdfLinks.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView pdfHeading;
        ImageView pdf;
        ImageView download;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.information_view, null);
        PdfLinks pdfLinks1 = (PdfLinks) getItem(position);
        holder.pdfHeading = (TextView) rowView.findViewById(R.id.pdfHeading);
        holder.pdf = (ImageView) rowView.findViewById(R.id.pdf);
        holder.download = (ImageView) rowView.findViewById(R.id.download);

        holder.pdf.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pdf));
        holder.download.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.download));
        holder.pdfHeading.setText(pdfLinks1.getPdfName());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Toast.makeText(context, "You Clicked " + pdfLinks.get(position), Toast.LENGTH_LONG).show();
                new DownloadFile().execute(pdfLinks.get(position).getPdfLink(), pdfLinks.get(position).getPdfName());
            }
        });
        return rowView;
    }

    private class DownloadFile extends AsyncTask<String, Integer, Void> {
        private boolean isFileExist = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);

            // instantiate it within the onCreate method
            mProgressDialog.setMessage("Downloading...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setProgressNumberFormat(null);
            mProgressDialog.setProgressPercentFormat(null);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mProgressDialog.cancel();
                }
            });
        }


        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(20);
            // if we get here, length is known, now set indeterminate to false
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1] + ".pdf";

            File directory = null;
            //if there is no SD card, create new directory objects to make directory on device
            if (Environment.getExternalStorageState() == null) {
                // search for directory on SD card
                directory = new File(Environment.getExternalStorageDirectory()
                        + "/RMS");
                if (!directory.exists()) {
                    directory.mkdir();
                }
                isFileExist = false;
//                mProgressDialog.setMessage("Downloading...");
                pdfFile = new File(directory, fileName);
                try {
                    pdfFile.createNewFile();
                    mProgressDialog.setProgress(50);
                    FileDownloader.downloadFile(fileUrl, pdfFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // if phone DOES have sd card
            } else if (Environment.getExternalStorageState() != null) {
                // search for directory on SD card
                directory = new File(Environment.getExternalStorageDirectory()
                        + "/RMS");
                if (!directory.exists()) {
                    directory.mkdir();
                }
                isFileExist = false;
//                mProgressDialog.setMessage("Downloading...");
                pdfFile = new File(directory, fileName);
                try {
                    pdfFile.createNewFile();
                    mProgressDialog.setProgress(50);
                    FileDownloader.downloadFile(fileUrl, pdfFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            mWakeLock.release();
            mProgressDialog.setProgress(100);
            mProgressDialog.dismiss();
            if (!isFileExist) {
                viewFile(pdfFile);
            }
        }
    }

    private void viewFile(File filepath) {
        if (filepath.exists()) {
            Uri path = Uri.fromFile(filepath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context,
                        "No Application available to view file",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context,
                        "Unable to open PDF File!",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context,
                    "File not found.", Toast.LENGTH_LONG).show();
        }
    }
}
