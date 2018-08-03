package com.Gone.Shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Gone.Shop.app.AppControllers;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.Gone.Shop.helper.SQLiteHandler;
import com.Gone.Shop.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllStatus extends AppCompatActivity {

    public static List<AllStatusModel> postLists = new ArrayList<>();
    RecyclerView recyclerView;
    AllStatusAdapter postAdapter;
    private ProgressDialog progressDialog;
    String user_email;


    private EditText edit_post;
    private Button btn_submit;
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
        setContentView(R.layout.all_status);

        edit_post=(EditText)findViewById(R.id.edit_problem_title);
        btn_submit=(Button)findViewById(R.id.btn_submit);







        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        name = user.get("name");
        email = user.get("email");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    submitStatus();
                sendNotification(name+" asked a question?","problem add");


            }
        });



        postAdapter=new AllStatusAdapter(getApplicationContext(), postLists);
        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(postAdapter);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");


        gettingAllStatus();



    }


    void gettingAllStatus(){

        recyclerView.removeAllViews();
        postLists.clear();

        progressDialog.show();
        String myURL = "http://thelostclan.xyz/UniShop/all_posts.php";

        JsonArrayRequest mainArrayReqs= new JsonArrayRequest(myURL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response.isNull(0)){
                    progressDialog.cancel();
                    Toast.makeText(AllStatus.this,"There is no post",Toast.LENGTH_LONG).show();
                }

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = (JSONObject) response.get(i);
                        AllStatusModel postModel = new AllStatusModel();

                        postModel.setId(jsonObject.getString("postID"));
                        postModel.setEmail(jsonObject.getString("userID"));
                        postModel.setPost(jsonObject.getString("post"));
                        postModel.setDate(jsonObject.getString("postDate"));
                        postModel.setPostUserName(jsonObject.getString("name"));
                        postModel.setPostUserImage(jsonObject.getString("image"));
                        postModel.setTotalComment(jsonObject.getString("total_comment"));

                        postLists.add(postModel);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {

                        postAdapter.notifyItemChanged(i);
                        progressDialog.cancel();

                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(AllStatus.this,"Network connection failed!!!",Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(mainArrayReqs);

    }
    private void submitStatus(){

        String url = "http://thelostclan.xyz/UniShop/adding-post.php";
        str_post=edit_post.getText().toString().trim();


        if (str_post.length()!=0){


            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Your status is uploading...");
            progressDialog.show();



            StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(AllStatus.this,"Your status uploaded",Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    startActivity(new Intent(AllStatus.this,AllStatus.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AllStatus.this,"Network connection failed!!!",Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    finish();
                   // startActivity(new Intent(AllStatus.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
            }) {
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("post",str_post);
                    params.put("post_date",getDate());
                    params.put("user_id",email);

                    return params;
                }

            };

            sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(sq);
        }

        else {
            Log.e("ERROR  ::::   ","  TITLE"+str_post);

            Toast.makeText(AllStatus.this,"Please enter your status",Toast.LENGTH_SHORT).show();

        }

    }




    private String getDate(){

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        String date = df.format(Calendar.getInstance().getTime());

        return date;

    }
    public void userStatus(View view){
        startActivity(new Intent(AllStatus.this,UserStatus.class));
    }
    void sendNotification(final String mgs, final String title){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://thelostclan.xyz/UniShop/send-notification-all.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // System.out.println(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("message",mgs);
                params.put("title",title);
                params.put("email","pappu");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}