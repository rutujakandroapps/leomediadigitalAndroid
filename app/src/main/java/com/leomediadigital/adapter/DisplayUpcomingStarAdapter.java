package com.leomediadigital.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.leomediadigital.R;
import com.leomediadigital.activity.ViewUpcomingStar;
import com.leomediadigital.model.UpcomingStarModel;
import com.leomediadigital.util.Constant;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class DisplayUpcomingStarAdapter  extends RecyclerView.Adapter<DisplayUpcomingStarAdapter.ViewHolder>{

    ArrayList<UpcomingStarModel> upcomingStarModelArrayList;
    Context context;

    public DisplayUpcomingStarAdapter( Context context,  ArrayList<UpcomingStarModel> upcomingStarModelArrayList)
    {
        this.context=context;
        this.upcomingStarModelArrayList =upcomingStarModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.displayupcoming_star_adapter,viewGroup,false);
      ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final UpcomingStarModel upcomingStarModel=upcomingStarModelArrayList.get(i);
        String url= Constant.IMGPATHUPCOMINSTR +upcomingStarModel.getImage();
        Picasso.with(context).load(url).placeholder( R.drawable.progress_animation ).into(viewHolder.imgupcomingstar);

        viewHolder.imgupcomingstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewUpcomingStar.class);
                Bundle args = new Bundle();
                String pos = String.valueOf(i);
                args.putSerializable("img",(Serializable) upcomingStarModelArrayList);
                intent.putExtra("Bundle",args);
                intent.putExtra("pos",pos);
//                i.putExtra("img",Constant.IMGPATHUPCOMINSTR+upcomingStarModel.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return upcomingStarModelArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgupcomingstar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgupcomingstar=itemView.findViewById(R.id.strimg);
        }
    }
}
