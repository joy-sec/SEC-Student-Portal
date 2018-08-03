package com.Gone.Shop;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    List<UsersModel> userList=new ArrayList<>();
    UsersAdapter usersAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        usersAdapter =new UsersAdapter(this,userList);
        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(usersAdapter);


        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");


        fetchData();
    }


    public void fetchData(){


        recyclerView.removeAllViews();
        userList.clear();

        progressDialog.show();
        String myURL = "http://thelostclan.xyz/lostclan/all-user-json.php";

        JsonArrayRequest mainArrayReq= new JsonArrayRequest(myURL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response.isNull(0)){
                    progressDialog.cancel();
                    Toast.makeText(UsersActivity.this,"There is no user",Toast.LENGTH_LONG).show();
                }

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = (JSONObject) response.get(i);
                        UsersModel postModel = new UsersModel();

                        postModel.setName(jsonObject.getString("name"));
                        postModel.setImage(jsonObject.getString("image"));
                        postModel.setEmail(jsonObject.getString("email"));
                        postModel.setStatus(jsonObject.getString("user_status"));

                        System.out.println(jsonObject.getString("name"));

                        userList.add(postModel);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        usersAdapter.notifyItemChanged(i);
                        progressDialog.cancel();

                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(UsersActivity.this,"Something went wrong !",Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(mainArrayReq);

    }
    }
