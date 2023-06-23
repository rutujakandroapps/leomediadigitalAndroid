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
import com.leomediadigital.model.UpcomingStarModel;

import java.util.ArrayList;

public class UpcominStarAdapter extends RecyclerView.Adapter<UpcominStarAdapter.ViewHolder>{


    ArrayList<UpcomingStarModel> upcomingStarModelArrayList;
    Context context;
    ArrayList<String> namelist;

    public UpcominStarAdapter(Context context,ArrayList<UpcomingStarModel> upcomingStarModelArrayList,  ArrayList<String> namelist)

    {
        this.context=context;
        this.upcomingStarModelArrayList=upcomingStarModelArrayList;
        this.namelist=namelist;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcominstar_adapter,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

       final UpcomingStarModel upcomingStarModel = upcomingStarModelArrayList.get(position);
       viewHolder.name.setText(upcomingStarModel.getName());
       // String url= Constant.IMGPATHUPCOMINSTR +upcomingStarModel.getImage();
       // Picasso.with(context).load(url).into(viewHolder.imgstar);
        viewHolder.lnr_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DisplayUpcomingStar.class);
                i.putExtra("name", upcomingStarModel.getName());
            //    i.putExtra("file_name",upcomingStarModel.getFile_name());
                i.putExtra("desc",upcomingStarModel.getDescription());
                i.putExtra("id",upcomingStarModel.getId());
                Log.e("id","imaggeidd" + upcomingStarModel.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return upcomingStarModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,description;
        ImageView imgstar;
        LinearLayout lnr_click;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lnr_click = (LinearLayout) itemView.findViewById(R.id.lnr_click);
            name=itemView.findViewById(R.id.txtname);
            //imgstar=itemView.findViewById(R.id.star_img);
        }
    }
}
