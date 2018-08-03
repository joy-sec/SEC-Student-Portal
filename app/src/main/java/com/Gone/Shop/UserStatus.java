package com.Gone.Shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Gone.Shop.helper.SQLiteHandler;
import com.Gone.Shop.helper.SessionManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

public class UserStatus extends AppCompatActivity {

    public static List<UserStatusModel> postLists = new ArrayList<>();
    RecyclerView recyclerView;
    UserStatusAdapter postAdapter;
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
        setContentView(R.layout.user_status);



        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        name = user.get("name");
        email = user.get("email");


        postAdapter=new UserStatusAdapter(getApplicationContext(), postLists);
        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(postAdapter);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");


        gettingUserStatus();



    }


    void gettingUserStatus(){

        recyclerView.removeAllViews();
        postLists.clear();

        progressDialog.show();
        String myURL = "http://thelostclan.xyz/UniShop/user_all_status.php";
        StringRequest sq = new StringRequest(Request.Method.POST, myURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray=new JSONArray(response);
                    progressDialog.cancel();

                    if (jsonArray.isNull(0)){
                        progressDialog.cancel();
                        Toast.makeText(UserStatus.this,"There is no post",Toast.LENGTH_LONG).show();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                            UserStatusModel postModel = new UserStatusModel();

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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(UserStatus.this,"Network connection failed!!!",Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_email", email);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sq);
    }



}