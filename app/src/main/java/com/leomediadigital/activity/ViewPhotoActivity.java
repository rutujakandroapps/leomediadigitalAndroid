package com.leomediadigital.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.leomediadigital.model.PhotoVideoModel;
import com.leomediadigital.model.UpcomingStarModel;
import com.leomediadigital.util.Constant;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ViewPhotoActivity extends AppCompatActivity {

    String str_image;
    ImageView  imgshare,news_image, imgDownloadPFD, imgback;
    ProgressDialog mProgressDialog;
    File file;
    String dirPath, fileName;
    ViewPager viewpager;
//    private ZoomageView news_image;
//    TouchImageView img;

    ArrayList<PhotoVideoModel> imagesArrayList = new ArrayList<>();
    String pos;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_viewxml);
        intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");
        imagesArrayList = (ArrayList<PhotoVideoModel>) args.getSerializable("image");

//        str_image = getIntent().getStringExtra("image");
        imgback = findViewById(R.id.imgback);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        news_image = findViewById(R.id.image);
        imgDownloadPFD = findViewById(R.id.downnload);
        imgshare = findViewById(R.id.share);


        AndroidNetworking.initialize(getApplicationContext());
        //Folder Creating Into Phone Storage
        dirPath = Environment.getExternalStorageDirectory() + "/LEOMEDIA";

        fileName = "image.jpeg";

        //file Creating With Folder & Fle Name
        file = new File(dirPath, fileName);

//        Picasso.with(this)
//                .load(str_image.replace(" ", "%20"))
//                .into(news_image);
        //news_image.setOnTouchListener();

//        imgshare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                shareItem(str_image);
//
//            }
//        });
//
//        imgDownloadPFD.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                downloadIMG(str_image);
//
//            }
//        });
//
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


    }

    public void downloadPDFList(String path) {
        //Prepare to download image
        URL url;
        InputStream in;

        BufferedInputStream buf;
        BufferedOutputStream out;
        try {
            url = new URL(path);
            in = url.openStream();

            out = new BufferedOutputStream(new FileOutputStream("testImage.jpg"));
            int i;

            while ((i = in.read()) != -1) {
                out.write(i);
            }
            out.close();
            in.close();

            buf = new BufferedInputStream(in);
            Bitmap bMap = BitmapFactory.decodeStream(buf);
            imgshare.setImageBitmap(bMap);
            if (in != null) {
                in.close();
            }
            if (buf != null) {
                buf.close();
            }
        } catch (Exception e) {
            Log.e("Error reading file", e.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
        }
        return false;
    }


    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ViewPhotoActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Download Image");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
//            image.setImageBitmap(result);
            // Close progressdialog
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewpager.setAdapter(new SlidingImageAdapter(ViewPhotoActivity.this));
        String a = intent.getStringExtra("pos");
        viewpager.setCurrentItem(Integer.parseInt(a));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void shareItem(String url) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Picasso.with(getApplicationContext()).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(i, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void downloadIMG(String url) {


        AndroidNetworking.download(url, dirPath, fileName)
                .build()
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        Toast.makeText(ViewPhotoActivity.this, "DownLoad Complete", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
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
                Picasso.with(context).load(Constant.IMGPATH_PHOTO_VIDEO+a).placeholder( R.drawable.progress_animation).noFade().into(photoView);
            }catch (Exception e){
                e.printStackTrace();
            }

            imgshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    shareItem(Constant.IMGPATH_PHOTO_VIDEO+imagesArrayList.get(position-1).getPhotos());

                }
            });

            imgDownloadPFD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadIMG(Constant.IMGPATH_PHOTO_VIDEO+imagesArrayList.get(position).getPhotos());

                }
            });

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






