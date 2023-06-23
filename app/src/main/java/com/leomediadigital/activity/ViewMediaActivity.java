package com.leomediadigital.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.leomediadigital.R;
import com.leomediadigital.model.MediaPartner;
import com.leomediadigital.model.PhotoVideoModel;
import com.leomediadigital.util.Constant;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ViewMediaActivity extends AppCompatActivity {

    String str_image;
    ImageView news_image,imgshare,imgback;

    ArrayList<MediaPartner> imagesArrayList = new ArrayList<>();
    String pos;
    Intent intent;
    ViewPager viewpager;
    String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_media);
//        str_image = getIntent().getStringExtra("image");
        Toolbar toolbar =findViewById(R.id.toolbar);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        imgback =findViewById(R.id.imgback);

        intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");
        imagesArrayList = (ArrayList<MediaPartner>) args.getSerializable("image");

        news_image = findViewById(R.id.image);
        imgshare=findViewById(R.id.share);

//        Picasso.with(this)
//                .load( str_image.replace(" ", "%20"))
//                .into(news_image);

//        imgshare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//              shareItem(Constant.IMGPATHMEDIA+a);
//
//            }
//        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


    }



    public void shareItem(String url) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Picasso.with(getApplicationContext()).load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(i, "Share Image"));
            }
            @Override public void onBitmapFailed(Drawable errorDrawable) { }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewpager.setAdapter(new SlidingImageAdapter(ViewMediaActivity.this));
        String a = intent.getStringExtra("pos");
        viewpager.setCurrentItem(Integer.parseInt(a));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class SlidingImageAdapter extends PagerAdapter {
        Context context;

        public SlidingImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view1 = layoutInflater.inflate(R.layout.photo_sliding_layout, null);

             final String a =imagesArrayList.get(position).getPhotos();
            ImageView photoView = view1.findViewById(R.id.imgDisplay);
//            imgDownloadPFD = view1.findViewById(R.id.downnload);
//            imgshare = view1.findViewById(R.id.share);
//            imgback = view1.findViewById(R.id.imgback);
            try {
                Picasso.with(context).load(Constant.IMGPATHMEDIA+a).placeholder( R.drawable.progress_animation).noFade().into(photoView);
            }catch (Exception e){
                e.printStackTrace();
            }

            imgshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    shareItem(Constant.IMGPATHMEDIA+imagesArrayList.get(position-1).getPhotos());

                }
            });

//            imgDownloadPFD.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    downloadIMG(Constant.IMGPATH_PHOTO_VIDEO+a);
//
//                }
//            });

//            imgback.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    onBackPressed();
//                }
//            });
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

//        public void shareItem(String url) {
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());
//            Picasso.with(getApplicationContext()).load(url).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    Intent i = new Intent(Intent.ACTION_SEND);
//                    i.setType("image/*");
//                    i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
//                    startActivity(Intent.createChooser(i, "Share Image"));
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable errorDrawable) {
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//                }
//            });
//        }
//
//        public Uri getLocalBitmapUri(Bitmap bmp) {
//            Uri bmpUri = null;
//            try {
//                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
//                FileOutputStream out = new FileOutputStream(file);
//                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
//                out.close();
//                bmpUri = Uri.fromFile(file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return bmpUri;
//        }
    }
}

