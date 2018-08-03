package com.Gone.Shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.Gone.Shop.helper.SQLiteHandler;
import com.Gone.Shop.helper.SessionManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private LinearLayout linearLayout,messageLayout;
    public String user,token,name;
    private SQLiteHandler db;
    CustomMenu rightMenu;
    CustomMenu customMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user_name = db.getUserDetails();

        name = user_name.get("name");
      //  Log.e("name",name);



        sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        linearLayout=(LinearLayout)findViewById(R.id.userlist_layout);
        messageLayout= (LinearLayout) findViewById(R.id.lay101);
        token=sharedPreferences.getString("token","null");
        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Message.class));
            }
        });




        linearLayout.setVisibility(View.GONE);

        user=sharedPreferences.getString("email","null");



        boolean login=sharedPreferences.getBoolean("login",false);


        if (login==true){
            //store token
            storeToken(user,"1");

        }
        else {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }

        if (user.equals("joy.appincubator@gmail.com")){

            linearLayout.setVisibility(View.VISIBLE);
        }

    }

    public void addProblem1(View v) {
        startActivity(new Intent(MainActivity.this, ProblemAddActivity.class));
    }
    public void shop(View view){
        startActivity(new Intent(MainActivity.this,ProblemListActivity.class));
    }
    public void userStatus(View view){
        startActivity(new Intent(MainActivity.this,UserStatus.class));
    }
    public void myProblemList1(View view){

        startActivity(new Intent(MainActivity.this,MyProblemActivity.class));
    }
    public void profile1(View view){

        startActivity(new Intent(MainActivity.this,MyProfileActivity.class));
    }
    public void qus(View view){

        startActivity(new Intent(MainActivity.this,AllStatus.class));
    }

    public void shop1(View view){

        startActivity(new Intent(MainActivity.this,ProblemListActivity.class));
    }

    public void alluser(View view){
        startActivity(new Intent(MainActivity.this,UsersActivity.class));
    }
    public void logout(View view){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("login",false);
        editor.commit();
        storeToken(user,"0");
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }
    void storeToken(final String email, final String status) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        String url = "http://www.thelostclan.xyz/UniShop/store-token.php";
        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("post_email", email);
                params.put("post_status",status);
                params.put("post_token", token);
                params.put("post_name",name);


                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sq);
    }






}
