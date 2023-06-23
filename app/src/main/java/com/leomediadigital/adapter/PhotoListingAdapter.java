package com.leomediadigital.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leomediadigital.R;
import com.leomediadigital.activity.DisplayUpcomingStar;
import com.leomediadigital.activity.PhotoVideoActivity;
import com.leomediadigital.model.PhotoVideoModel;
import com.leomediadigital.util.Constant;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class PhotoListingAdapter extends RecyclerView.Adapter<PhotoListingAdapter.ViewHolder> {

    ArrayList<PhotoVideoModel> photoVideoModelArrayList;
    Context context;


    public PhotoListingAdapter( Context context, ArrayList<PhotoVideoModel> photoVideoModelArrayList)
    {

      this.context=context;
      this.photoVideoModelArrayList=photoVideoModelArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photolisting_adapter,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        final PhotoVideoModel contactModel = photoVideoModelArrayList.get(position);
        String url= Constant.IMGPATHUPCOMINSTR + contactModel.getPhotos();
        Picasso.with(context).load(url).placeholder( R.drawable.progress_animation ).into( viewHolder.image);

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PhotoVideoActivity.class);
                intent.putExtra("id",contactModel.getId());
                Log.e("id", "id: "+ contactModel.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoVideoModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtdesc;
        ImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtdesc=itemView.findViewById(R.id.txtname);
            image =itemView.findViewById(R.id.image);

        }
    }
}
