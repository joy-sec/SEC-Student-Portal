package com.Gone.Shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.Gone.Shop.helper.SQLiteHandler;
import com.Gone.Shop.helper.SessionManager;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateYourStatus extends AppCompatActivity {

    private EditText edit_post;
    private Button btn_submit;

    private ProgressDialog progressDialog;
    private String str_post;
    String postID;
    int post_id;

    private SQLiteHandler db;
    private SessionManager session;
    //String uid;
    String name;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_your_status);


        edit_post = (EditText) findViewById(R.id.edit_problem_title);
        btn_submit = (Button) findViewById(R.id.btn_submit);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Updating your status...");


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        name = user.get("name");
        email = user.get("email");

        postID = getIntent().getStringExtra("id");
        post_id = Integer.parseInt(postID);
        str_post = getIntent().getStringExtra("post");
        edit_post.setText(getIntent().getStringExtra("post"));
        edit_post.setSelection(getIntent().getStringExtra("post").length());


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auth=getIntent().getStringExtra("user_update");
                if (auth!=null){
                    if (auth.equals("user_update")){

                        updateUserStatus();
                    }
                }
                else {
                    updateStatus();
                }



            }
        });


    }

    private String getDate() {

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        String date = df.format(Calendar.getInstance().getTime());

        return date;
    }


    void updateStatus() {

        String url = "http://172.245.177.162/~sabbirah/UniShop/update-post.php";


        str_post = edit_post.getText().toString().trim();

        if (str_post.length() != 0) {

            progressDialog.show();

            StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(UpdateYourStatus.this, "Your status updated", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    startActivity(new Intent(UpdateYourStatus.this, AllStatus.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UpdateYourStatus.this, "Network connection failed!!!", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    finish();
                    //startActivity(new Intent(UpdateYourStatus.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("post", str_post);
                    params.put("post_date", getDate());
                    params.put("user_id", email);
                    params.put("post_id", postID);
                    return params;
                }

            };

            sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(sq);

        } else {
            Log.e("ERROR  ::::   ", "  TITLE" + str_post);

            Toast.makeText(UpdateYourStatus.this, "Please enter your status", Toast.LENGTH_SHORT).show();

        }

    }


    void updateUserStatus() {

        String url = "http://172.245.177.162/~sabbirah/UniShop/update-post.php";


        str_post = edit_post.getText().toString().trim();

        if (str_post.length() != 0) {

            progressDialog.show();

            StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(UpdateYourStatus.this, "Your status updated", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    startActivity(new Intent(UpdateYourStatus.this, UserStatus.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UpdateYourStatus.this, "Network connection failed!!!", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    finish();
                    //startActivity(new Intent(UpdateYourStatus.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("post", str_post);
                    params.put("post_date", getDate());
                    params.put("user_id", email);
                    params.put("post_id", postID);
                    return params;
                }

            };

            sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(sq);

        } else {
            Log.e("ERROR  ::::   ", "  TITLE" + str_post);

            Toast.makeText(UpdateYourStatus.this, "Please enter your status", Toast.LENGTH_SHORT).show();

        }

    }

}