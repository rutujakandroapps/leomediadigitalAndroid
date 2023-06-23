package com.aptiquiz.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptiquiz.util.Constant;
import com.leomediadigital.R;
import com.squareup.picasso.Picasso;

public class DisplayStar extends AppCompatActivity {

    TextView stardesc;
    ImageView imgstr,imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaystar);
        stardesc = findViewById(R.id.strdesc);
        imgstr=findViewById(R.id.strimg);
        imgback =findViewById(R.id.imgback);
        String desc = (getIntent().getStringExtra("desc"));
        stardesc.setText(desc);
        String imgurl=(getIntent().getStringExtra("img"));
        Picasso.with(getApplicationContext()).load(Constant.IMGPATHUPCOMINSTR+imgurl).into(imgstr);


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
