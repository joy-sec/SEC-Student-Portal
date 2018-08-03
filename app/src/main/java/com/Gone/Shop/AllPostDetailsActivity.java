package com.Gone.Shop;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

public class AllPostDetailsActivity extends AppCompatActivity {

    private TextView title,details,price,location,date,status,division,district;
    private ImageView imageView;
    double lat,lon;
    private String str_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_post_details);
        title=(TextView)findViewById(R.id.post_title);
        details=(TextView)findViewById(R.id.post_details);
        price=(TextView)findViewById(R.id.price);
        imageView=(ImageView)findViewById(R.id.imageView);
        location=(TextView)findViewById(R.id.btn_location);
        date=(TextView)findViewById(R.id.date_time);
        status=(TextView)findViewById(R.id.btn_status);

        division=(TextView)findViewById(R.id.text_division);
        district=(TextView)findViewById(R.id.text_district);

        str_status=getIntent().getStringExtra("status");

        division.setText(getIntent().getStringExtra("division"));
        district.setText(getIntent().getStringExtra("district"));
        title.setText(Html.fromHtml(getIntent().getStringExtra("title")));
        details.setText(Html.fromHtml(getIntent().getStringExtra("content")));
        price.setText(Html.fromHtml(getIntent().getStringExtra("price")));
        location.setText(Html.fromHtml(getIntent().getStringExtra("loc")));

        date.setText(Html.fromHtml(getIntent().getStringExtra("time")));

        if (str_status!=null){

            if (str_status.equals("unsolved"))
            {
                status.setText("Unsolved");
                status.setBackgroundColor(Color.RED);
            }
            else if (str_status.equals("solved")){
                status.setText("Solved");
                status.setBackgroundColor(Color.GREEN);
            }
        }


        lat= Double.parseDouble(getIntent().getStringExtra("lat"));
        lon= Double.parseDouble(getIntent().getStringExtra("lon"));

        /*SharedPreferences sharedPreferences=getSharedPreferences("map",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putFloat("lat", (float) lat);
        editor.putFloat("lon", (float) lon);
        editor.commit();

        Intent intent=new Intent(AllPostDetailsActivity.this,MapsActivity.class);
        intent.putExtra("lat",lat);
        intent.putExtra("lon",lon);
        //startActivity(intent);
        */

        Picasso.with(AllPostDetailsActivity.this).load(getIntent().getStringExtra("image")).into(imageView);

    }

}
