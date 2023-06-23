package com.leomediadigital.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.leomediadigital.R;
import com.leomediadigital.model.UpcomingStarModel;
import com.leomediadigital.util.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewUpcomingStar  extends AppCompatActivity {


    String str_image;
    ImageView imgback,news_image;
    ViewPager viewpager;
    ArrayList<UpcomingStarModel> imagesArrayList = new ArrayList<>();
    String pos;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_upcomingstar);
        intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");
        imagesArrayList = (ArrayList<UpcomingStarModel>) args.getSerializable("img");
//        str_image = getIntent().getStringExtra("img");
        imgback = findViewById(R.id.imgback);
        viewpager = findViewById(R.id.viewpager);
        news_image = findViewById(R.id.image);
//
//        Picasso.with(this)
//                .load(str_image.replace(" ", "%20"))
//                .into(news_image);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewpager.setAdapter(new SlidingImageAdapter(ViewUpcomingStar.this));
        String a = intent.getStringExtra("pos");
        viewpager.setCurrentItem(Integer.parseInt(a));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class SlidingImageAdapter extends PagerAdapter {
        LayoutInflater layoutInflater;
        Context context;

        public SlidingImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view1 = layoutInflater.inflate(R.layout.slidingimages_layout, null);

            String a =imagesArrayList.get(position).getImage();
            ImageView photoView = view1.findViewById(R.id.imgDisplay);
            try {
                Picasso.with(context).load(Constant.IMGPATHUPCOMINSTR+a).placeholder( R.drawable.progress_animation).noFade().into(photoView);
            }catch (Exception e){
                e.printStackTrace();
            }
            ViewPager vp = (ViewPager) view;
            vp.addView(view1, 0);
            return view1;

        }

        @Override
        public int getCount() {
            return imagesArrayList.size();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return  view.equals(object);
        }

    }
}
