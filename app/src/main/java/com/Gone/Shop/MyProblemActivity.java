package com.Gone.Shop;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyProblemActivity extends AppCompatActivity {

    public  List<AllPostModel> postLists = new ArrayList<>();
    RecyclerView recyclerView;
    MyPostAdapter postAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_problem);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");

        postAdapter=new MyPostAdapter(getApplicationContext(), postLists);
        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(postAdapter);

        post();
    }


    void post(){
        progressDialog.show();
        recyclerView.removeAllViews();
        postLists.clear();
        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        final String user=sharedPreferences.getString("email","null");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thelostclan.xyz/lostclan/my_posts_json.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.length()<3){
                            progressDialog.cancel();
                            Toast.makeText(MyProblemActivity.this,"There is no post",Toast.LENGTH_SHORT).show();
                        }

                        try {
                            JSONArray jsonArray=new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    AllPostModel postModel = new AllPostModel();

                                    postModel.setTitle(jsonObject.getString("postTitle"));
                                    postModel.setName(jsonObject.getString("userName"));
                                    postModel.setDetails(jsonObject.getString("postDetails"));
                                    postModel.setDate(jsonObject.getString("postDate"));
                                    postModel.setLatitude(jsonObject.getString("latitude"));
                                    postModel.setLongitude(jsonObject.getString("longitude"));
                                    postModel.setImage(jsonObject.getString("image"));
                                    postModel.setLocation(jsonObject.getString("location"));
                                    postModel.setId(jsonObject.getString("postID"));
                                    postModel.setDistrict(jsonObject.getString("district"));
                                    postModel.setDivision(jsonObject.getString("division"));
                                    postModel.setStatus(jsonObject.getString("status"));

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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(),"Something Went Wrong !",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("userid", user);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}