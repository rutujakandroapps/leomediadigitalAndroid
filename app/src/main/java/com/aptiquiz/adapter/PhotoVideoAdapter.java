package com.aptiquiz.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
//import com.aptiquiz.R;
import com.aptiquiz.activity.ViewPhotoActivity;
import com.aptiquiz.model.PhotoVideoModel;
import com.aptiquiz.util.Constant;
import com.leomediadigital.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoVideoAdapter extends RecyclerView.Adapter<PhotoVideoAdapter.ViewHolder>{

    ArrayList<PhotoVideoModel> photoVideoModelArrayList;
    Context context;

    public PhotoVideoAdapter(Context context,ArrayList<PhotoVideoModel> photoVideoModelArrayList)
    {

        this.context=context;
        this.photoVideoModelArrayList=photoVideoModelArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photp_videoadapter,parent,false);
       ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
       final PhotoVideoModel photoVideoModel = photoVideoModelArrayList.get(position);
        String url= Constant.IMGPATH_PHOTO_VIDEO +photoVideoModel.getPhotos();
        Picasso.with(context).load(url).placeholder( R.drawable.progress_animation ).into(viewHolder.imgphoto);
        viewHolder.imgphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ViewPhotoActivity.class);
                intent.putExtra("image",Constant.IMGPATH_PHOTO_VIDEO+photoVideoModel.getPhotos());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return photoVideoModelArrayList.size();
    }


    public  class  ViewHolder extends RecyclerView.ViewHolder {


        ImageView imgvideolink,imgphoto;
        TextView txtdesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgphoto =itemView.findViewById(R.id.strimg);


        }
    }

}
