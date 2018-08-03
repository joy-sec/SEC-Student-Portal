package com.Gone.Shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateYourComment extends AppCompatActivity {
    private EditText edit_comment;
    private Button btn_submit;
    private ProgressDialog progressDialog;
    private String str_comment;
    String commentId,comment;
    int comment_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_your_comment);
        edit_comment=(EditText)findViewById(R.id.comment);
        btn_submit=(Button)findViewById(R.id.btn_submit);


        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Updating your comment...");


        commentId=getIntent().getStringExtra("commentId");
        comment_id= Integer.parseInt(commentId);
        comment=getIntent().getStringExtra("comment");



        edit_comment.setText(getIntent().getStringExtra("comment"));
        edit_comment.setSelection(getIntent().getStringExtra("comment").length());


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auth=getIntent().getStringExtra("user_update");
                if (auth!=null){
                    if (auth.equals("user_update")){

                        updateUserComment();
                    }
                }
                else {
                    updateComment();
                }


            }
        });




    }


    private String getDate(){

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        String date = df.format(Calendar.getInstance().getTime());

        return date;
    }


    void updateComment(){

        String url = "http://172.245.177.162/~sabbirah/UniShop/update-comment.php";

        str_comment=edit_comment.getText().toString().trim();


        if (str_comment.length() != 0) {

            progressDialog.show();



        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(UpdateYourComment.this, "Your comment updated", Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                startActivity(new Intent(UpdateYourComment.this, AllComment.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateYourComment.this,"Network connection failed!!!",Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                finish();
                //startActivity(new Intent(UpdateYourComment.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("comment",str_comment);
                params.put("comment_date",getDate());
                params.put("comment_id", commentId);
                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sq);

        } else {
            Log.e("ERROR  ::::   ", "  TITLE" + str_comment);

            Toast.makeText(UpdateYourComment.this, "Please enter your comment", Toast.LENGTH_SHORT).show();

        }

    }


    void updateUserComment(){

        String url = "http://172.245.177.162/~sabbirah/UniShop/update-comment.php";

        str_comment=edit_comment.getText().toString().trim();


        if (str_comment.length() != 0) {

            progressDialog.show();



            StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(UpdateYourComment.this, "Your comment updated", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    startActivity(new Intent(UpdateYourComment.this, UserComment.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UpdateYourComment.this,"Network connection failed!!!",Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    finish();
                    //startActivity(new Intent(UpdateYourComment.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
            }) {
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("comment",str_comment);
                    params.put("comment_date",getDate());
                    params.put("comment_id", commentId);
                    return params;
                }

            };

            sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(sq);

        } else {
            Log.e("ERROR  ::::   ", "  TITLE" + str_comment);

            Toast.makeText(UpdateYourComment.this, "Please enter your comment", Toast.LENGTH_SHORT).show();

        }

    }
}