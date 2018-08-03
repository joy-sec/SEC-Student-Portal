package com.Gone.Shop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edit_title,edit_description,edit_price;
    private Button btn_submit,btn_chooser,btn_location;
    private ImageView imageView;
    private int PICK_IMAGE_REQUEST=1;
    private Uri filePath;
    private Bitmap bitmap;
    private double lat,lon;
    private int PLACE_PICKER_REQUEST=2;
    private ProgressDialog progressDialog;
    private String uploadImage,str_title,str_details,str_price,str_lat,str_lon,location,str_image;
    String author;
    private Spinner state,city;
    List<String> citis=new ArrayList<String>();
    ArrayAdapter adapter,unit_adapter;
    String postID;
    int post_id;
    String str_dis;
    String str_division,str_district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_add);


        edit_title=(EditText)findViewById(R.id.edit_problem_title);
        edit_description=(EditText)findViewById(R.id.edit_description);
        edit_price=(EditText)findViewById(R.id.price);
        btn_chooser=(Button) findViewById(R.id.btn_image_chooser);
        btn_location= (Button) findViewById(R.id.btn_choose_location);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        imageView=(ImageView)findViewById(R.id.img_image);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Post Submitting");



        state=(Spinner)findViewById(R.id.spinner);
        city= (Spinner) findViewById(R.id.spinner1);
        state.setOnItemSelectedListener(this);
        adapter=ArrayAdapter.createFromResource(this,R.array.State_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        state.setAdapter(adapter);
        city.setOnItemSelectedListener(this);

        String auth=getIntent().getStringExtra("auth");

        if (auth!=null){
            if (auth.equals("update")){

                str_dis=getIntent().getStringExtra("dist");
                state.setSelection(adapter.getPosition(getIntent().getStringExtra("div")));
                city.setSelection(adapter.getPosition(getIntent().getStringExtra("dist")));
                str_title=getIntent().getStringExtra("title");
                str_details=getIntent().getStringExtra("content");
                str_price=getIntent().getStringExtra("price");
                location=getIntent().getStringExtra("loc");

                str_lat=getIntent().getStringExtra("lat");
                str_lon=getIntent().getStringExtra("lon");


                str_image=getIntent().getStringExtra("image");

                postID=getIntent().getStringExtra("id");
                post_id= Integer.parseInt(postID);

                edit_title.setText(getIntent().getStringExtra("title"));
                edit_description.setText(getIntent().getStringExtra("content"));
                btn_location.setText(getIntent().getStringExtra("loc"));
                Picasso.with(this).load(getIntent().getStringExtra("image")).placeholder(R.mipmap.ic_launcher).into(imageView);
                edit_title.setSelection(getIntent().getStringExtra("title").length());
                edit_description.setSelection(getIntent().getStringExtra("content").length());
            }
        }



        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        author=sharedPreferences.getString("name","null");

        btn_chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auth=getIntent().getStringExtra("auth");
                if (auth!=null){
                    if (auth.equals("update")){

                        update();
                    }
                }
                else {
                    submit();

                }

            }
        });




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int pos;
        pos=state.getSelectedItemPosition();
        int iden=parent.getId();
        if(iden==R.id.spinner)
        {

            str_division=state.getSelectedItem().toString();

            switch (pos)
            {
                case 0:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Dhaka_array,android.R.layout.simple_spinner_item
                );
                    break;
                case 1:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Chittagong_array,android.R.layout.simple_spinner_item
                );
                    break;
                case 2:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Rajshashi_array,android.R.layout.simple_spinner_item
                );
                    break;
                case 3:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Sylhet_array,android.R.layout.simple_spinner_item
                );
                    break;
                case 4:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Barishal_array,android.R.layout.simple_spinner_item
                );
                    break;
                case 5:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Khulna_array,android.R.layout.simple_spinner_item
                );
                    break;
                case 6:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Rangpur_array,android.R.layout.simple_spinner_item
                );
                    break;
                case 7:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Mymensingh_array,android.R.layout.simple_spinner_item
                );
                    break;
                default:
                    break;

            }

            city.setAdapter(unit_adapter);
            unit_adapter.setDropDownViewResource(R.layout.spinner_item);

            if (str_dis!=null){
                city.setSelection(unit_adapter.getPosition(str_dis));
                str_dis=null;
            }

        }

        str_district=city.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {



    }
    private void showFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode==PLACE_PICKER_REQUEST && resultCode == RESULT_OK){

            Place place = PlacePicker.getPlace(data, this);
            CharSequence place_name=place.getAddress();
            location= (String) place_name;
            lat= place.getLatLng().latitude;
            lon=place.getLatLng().longitude;

        }

    }

    private void submit(){

        String url = "https://thelostclan.xyz/lostclan/image/adding-post.php";
        str_title=edit_title.getText().toString().trim();
        str_details=edit_description.getText().toString().trim();
        str_price=edit_price.getText().toString().trim();

        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        final String user=sharedPreferences.getString("email","null");
        str_lat= String.valueOf(lat);
        str_lon= String.valueOf(lon);
        if (bitmap!=null && str_title!=null && str_details!=null && str_price!=null && str_lon !=null && str_lat!=null && location!=null){

            uploadImage = getStringImage(bitmap);

            progressDialog.show();


            StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    status();
                    //sendNotification(author+"ask a quens","problem add");
                    Toast.makeText(ProblemAddActivity.this,"Submitted",Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    startActivity(new Intent(ProblemAddActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ProblemAddActivity.this,"SomeThing went wrong !",Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    startActivity(new Intent(ProblemAddActivity.this,ProblemListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
            }) {
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("post_title",str_title);
                    params.put("post_image",uploadImage);
                    params.put("post_details",str_details);
                    params.put("post_price",str_price);
                    params.put("post_date",getDate());
                    params.put("post_latitude",str_lat);
                    params.put("post_longitude",str_lon);
                    params.put("user_id",user);
                    params.put("user_name",author);
                    params.put("post_location",location);
                    params.put("post_division",str_division);
                    params.put("post_district",str_district);
                    params.put("post_status","unsolved");


                    return params;
                }

            };

            sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(sq);
        }

        else {
            Log.e("ERROR  ::::   ","    BITMAP :::: "+bitmap+"  LAT :::"+str_lat+"  LON ::::"+str_lon+"  LOCATION  ::::"+location+"  DETAILS :::"+str_details+"   DETAILS :::"+str_price+"   TITLE"+str_title);

            Toast.makeText(ProblemAddActivity.this,"Please fill up all requirement",Toast.LENGTH_SHORT).show();

        }

    }

    private String getDate(){

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        String date = df.format(Calendar.getInstance().getTime());

        return date;
    }


    void update(){


        if ( lat==0.0 & lon==0.0 ){

        }
        else {
            str_lat= String.valueOf(lat);
            str_lon= String.valueOf(lon);
        }

            progressDialog.show();
            String url = "https://thelostclan.xyz/lostclan/image/update-post.php";

            if (bitmap!=null){
                uploadImage = getStringImage(bitmap);
            }
            else {
                uploadImage=str_image;
            }

            str_title=edit_title.getText().toString().trim();
            str_details=edit_description.getText().toString().trim();
            str_price=edit_price.getText().toString().trim();

            SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
            final String user=sharedPreferences.getString("email","null");

            StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(ProblemAddActivity.this,"Submitted",Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    startActivity(new Intent(ProblemAddActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ProblemAddActivity.this,"SomeThing went wrong !",Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    startActivity(new Intent(ProblemAddActivity.this,ProblemListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
            }) {
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("post_title",str_title);
                    params.put("post_image",uploadImage);
                    params.put("post_details",str_details);
                    params.put("post_price",str_price);
                    params.put("post_date",getDate());
                    params.put("post_latitude",str_lat);
                   params.put("post_longitude",str_lon);
                    params.put("user_id",user);
                    params.put("user_name",author);
                    params.put("post_location",location);
                    params.put("post_division",str_division);
                    params.put("post_district",str_district);
                    params.put("post_id", postID);
                    return params;
                }

            };

            sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(sq);

    }

    void status(){

        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        final String email=sharedPreferences.getString("email","null");

        StringRequest sq = new StringRequest(Request.Method.POST, "http://thelostclan.xyz/lostclan/solve-status.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(ProblemAddActivity.this,"Updated",Toast.LENGTH_LONG).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("postid", "00");
                params.put("post_email",email);
                params.put("post_status","solved");

                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ProblemAddActivity.this);
        requestQueue.add(sq);



    }

    private void location(){

        PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
        try {
            Intent intent=builder.build(ProblemAddActivity.this);
            startActivityForResult(intent,PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }




}
