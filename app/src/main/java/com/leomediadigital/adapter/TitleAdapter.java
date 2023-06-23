package com.leomediadigital.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leomediadigital.BuildConfig;
import com.leomediadigital.R;
import com.leomediadigital.activity.TitleListPdfweb;
import com.leomediadigital.model.ContactModel;
import com.leomediadigital.util.Constant;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder> {

    ArrayList<ContactModel> contactModelArrayList;
    Context context;
    private static final String googleDocsUrl = "http://docs.google.com/viewer?url=";
    boolean isDownloadingCancel = false;
    ProgressDialog progressDialog;


    public TitleAdapter(Context context, ArrayList<ContactModel> contactModelArrayList) {
        this.contactModelArrayList = contactModelArrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final ContactModel contactModel = contactModelArrayList.get(position);
        viewHolder.txtname.setText(contactModel.getName().trim());
        viewHolder.txtUpdatedDate.setText(contactModel.getUpdatedate().trim());
        Log.e("name1","Name"+ contactModel.getName());
        viewHolder.lnrpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.TITLEPDFPATH+contactModel.getDocument()));
               context. startActivity(browserIntent);
//                Intent intent = new Intent(context, TitleListPdfweb.class);
//                intent.putExtra("pdf",Constant.TITLEPDFPATH+contactModel.getMedia());
//                context.startActivity(intent);
            }
        });

        viewHolder.imgDownloadPFD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadPDFList(Constant.TITLEPDFPATH + contactModel.getDocument());

            }
        });

    }


    public void downloadPDFList(String path) {
        Toast.makeText(context, "Downloading Start....", Toast.LENGTH_SHORT).show();
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(path);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
    }

    @Override
    public int getItemCount() {
        return contactModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtname, txtUpdatedDate;
        LinearLayout lnrpdf;
        ImageView imgDownloadPFD;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txtname);
            lnrpdf = itemView.findViewById(R.id.lnrpdf);
            imgDownloadPFD = itemView.findViewById(R.id.imgDownloadPFD);
            txtUpdatedDate = itemView.findViewById(R.id.txtUpdatedDate);


        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "testhepdf");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
           new  FileDownloader().downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }

    public class FileDownloader {
        private static final int MEGABYTE = 1024 * 1024;

        public void downloadFile(String fileUrl, File directory) {
            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
