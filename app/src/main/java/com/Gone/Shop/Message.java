package com.Gone.Shop;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.Gone.Shop.helper.SQLiteHandler;
import com.Gone.Shop.helper.SessionManager;
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
import java.util.Map;

public class Message extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static ArrayList<User_model> postLists = new ArrayList<>();
    private ProgressDialog pDialog;
    MessageAdapter adapter;
    private SQLiteHandler db;
    //String uid;
    String name;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please wait..");
        recyclerView= (RecyclerView) findViewById(R.id.message_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new MessageAdapter(postLists,getApplicationContext());
        recyclerView.setAdapter(adapter);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        name = user.get("name");
        email = user.get("email");
    }
    void getData(){

        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://thelostclan.xyz/UniShop/chatRoom-json.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        recyclerView.removeAllViews();
                        postLists.clear();
                        pDialog.cancel();


                        try {
                            JSONArray jsonArray=new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    User_model postModel = new User_model();

                                    postModel.setName(jsonObject.getString("name"));
                                    postModel.setUserEmail(jsonObject.getString("user_email"));
                                    postModel.setLast_message(jsonObject.getString("last_message"));
                                    postModel.setCount(jsonObject.getInt("count"));
                                    postModel.setTime(jsonObject.getString("time"));

                                    postLists.add(postModel);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } finally {
                                    adapter.notifyItemChanged(i);
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
                        pDialog.cancel();
                        Toast.makeText(getApplicationContext(),"Something Went Wrong !",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("post_sender_email", email);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
