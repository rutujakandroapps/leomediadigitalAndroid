package com.aptiquiz.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.aptiquiz.R;
import com.aptiquiz.activity.PhotoVideoActivity;
import com.aptiquiz.model.ContactModel;
import com.aptiquiz.model.PhotoVideoModel;
import com.leomediadigital.R;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final PhotoVideoModel contactModel = photoVideoModelArrayList.get(position);
        viewHolder.txtdesc.setText(contactModel.getDescription().trim());
        viewHolder.lnrpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,PhotoVideoActivity.class);
                intent.putExtra("descID",contactModel.getDescription_id());
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
        LinearLayout lnrpdf;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtdesc=itemView.findViewById(R.id.txtname);
            lnrpdf=itemView.findViewById(R.id.lnrpdf);

        }
    }
}
