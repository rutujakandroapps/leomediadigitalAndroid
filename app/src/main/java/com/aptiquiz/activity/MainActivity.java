package com.aptiquiz.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

//import com.aptiquiz.R;
import com.aptiquiz.util.SharedPref;
import com.leomediadigital.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout lnrOurMediaPartner;
    LinearLayout lnrArticles;
    LinearLayout lnrFilmsPersonality;
    LinearLayout lnrContacts;
    LinearLayout lnrPhotoVideo;
    LinearLayout lnrUpcomingStar;
    LinearLayout lnrlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lnrOurMediaPartner = findViewById(R.id.lnrOurMediaPartner);
        lnrFilmsPersonality = findViewById(R.id.lnrFilmsPersonality);
        lnrContacts = findViewById(R.id.inrtitle);
        lnrPhotoVideo = findViewById(R.id.lnrPhotoVideo);
        lnrUpcomingStar = findViewById(R.id.lnrUpcomingStar);
        lnrlogout = findViewById(R.id.lnrlogout);

        lnrOurMediaPartner.setOnClickListener(this);
        lnrFilmsPersonality.setOnClickListener(this);
        lnrContacts.setOnClickListener(this);
        lnrPhotoVideo.setOnClickListener(this);
        lnrUpcomingStar.setOnClickListener(this);
        lnrlogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.lnrOurMediaPartner:
                Intent mediaIntent = new Intent(MainActivity.this,OurMediaPartenerActivity.class);
                startActivity(mediaIntent);
                break;
            case R.id.lnrFilmsPersonality:
                 Intent interviewsIntent = new Intent(MainActivity.this,FilmsPersonalitiesActivity.class);
                startActivity(interviewsIntent);
                break;
            case R.id.inrtitle:
                Intent contactIntet = new Intent(MainActivity.this, TitleActivity.class);
                startActivity(contactIntet);
                break;
            case R.id.lnrPhotoVideo:
                Intent photoVideoIntent = new Intent(MainActivity.this,PhotovideoFolder.class);
                startActivity(photoVideoIntent);
                break;
            case R.id.lnrUpcomingStar:
                Intent upcomingStarIntent = new Intent(MainActivity.this,UpcominStarActivity.class);
                startActivity(upcomingStarIntent);
                break;
            case R.id.lnrlogout:
                AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
                alertdialog.setTitle("Log Out");
                alertdialog.setMessage("Do you want to Log Out?");
                alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPref.setLogin(MainActivity.this,false);
                        Intent logOutIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(logOutIntent);
                        finish();
                    }
                });

                alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert=alertdialog.create();
                alertdialog.show();
                break;

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
