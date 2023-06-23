package com.aptiquiz.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import com.aptiquiz.R;
import com.aptiquiz.activity.TitleListPdfweb;
import com.aptiquiz.model.ContactModel;
import com.aptiquiz.util.Constant;
import com.leomediadigital.R;

import java.util.ArrayList;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder> {

    ArrayList<ContactModel> contactModelArrayList;
    Context context;


    public TitleAdapter(Context context, ArrayList<ContactModel> contactModelArrayList)
    {
        this.contactModelArrayList =contactModelArrayList;
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final ContactModel contactModel = contactModelArrayList.get(position);
        viewHolder.txtdesc.setText(contactModel.getDescription().trim());
        viewHolder.txtUpdatedDate.setText(contactModel.getCreatedate().trim());
        viewHolder.lnrpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, TitleListPdfweb.class);
                intent.putExtra("pdf",Constant.TITLEPDFPATH+contactModel.getMedia());
                context.startActivity(intent);

            }
        });

        viewHolder.imgDownloadPFD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadPDFList(Constant.TITLEPDFPATH+contactModel.getMedia());

            }
        });

    }


    public void downloadPDFList(String path){
        Toast.makeText(context, "Downloading Start....", Toast.LENGTH_SHORT).show();
        DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(path);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
    }

    @Override
    public int getItemCount() {
        return contactModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {

       TextView txtdesc,txtUpdatedDate;
       LinearLayout lnrpdf;
       ImageView imgDownloadPFD;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdesc=itemView.findViewById(R.id.txtname);
            lnrpdf=itemView.findViewById(R.id.lnrpdf);
            imgDownloadPFD=itemView.findViewById(R.id.imgDownloadPFD);
            txtUpdatedDate=itemView.findViewById(R.id.txtUpdatedDate);


        }
    }
}
