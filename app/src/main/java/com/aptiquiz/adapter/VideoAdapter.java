package com.aptiquiz.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codewaves.youtubethumbnailview.ImageLoader;
import com.codewaves.youtubethumbnailview.ThumbnailView;
//import com.aptiquiz.R;
import com.aptiquiz.model.VideoModel;
import com.leomediadigital.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class VideoAdapter  extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    ArrayList<VideoModel> videoModelArrayList;
    Context context;
    String url;

    public VideoAdapter(Context context, ArrayList<VideoModel> videoModelArrayList){

        this.context=context;
        this.videoModelArrayList=videoModelArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_activititadapter,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        VideoModel videoModel= videoModelArrayList.get(i);

        if (videoModel!=null){
            url=videoModel.getVideolink();
            viewHolder.thumbnailView.loadThumbnail(videoModel.getVideolink());
            viewHolder.thumbnailView.loadThumbnail(url, new ImageLoader() {
                @Override
                public Bitmap load(String url) throws IOException {
                    return Picasso.with(context).load(url).get();
                }
            });

        }
        else {

            Toast.makeText(context,"No video found",Toast.LENGTH_SHORT).show();
        }


        viewHolder.thumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

    }

    @Override
    public int getItemCount() {
        return videoModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        ThumbnailView thumbnailView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailView=itemView.findViewById(R.id.thumbnail);



        }
    }
}
