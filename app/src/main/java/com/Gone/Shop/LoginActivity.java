package com.Gone.Shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    EditText uemail,upass;
    Button login,signUP;
    SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    String author;
    private String token;



    private static final String TAG = LoginActivity.class.getSimpleName();
    private SessionManager session;
    private SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        uemail=(EditText)findViewById(R.id.user);
        upass=(EditText)findViewById(R.id.pass);
        login=(Button)findViewById(R.id.btn_login);
        signUP=(Button)findViewById(R.id.btn_sign_up);

        sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        token=sharedPreferences.getString("token","null");

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Login..");

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();


            }
        });


        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
    }

    void signIn(){
        progressDialog.show();
        final String userEmail = uemail.getText().toString();
        final String userPass = upass.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thelostclan.xyz/lostclan/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.cancel();

                        if(response.equals("s")){

                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putBoolean("login",true);
                            editor.putString("email",userEmail);
                            editor.commit();
                            post();
                            author=userEmail;
                            Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();

                        }
                        else if (response.equals("b")){
                            Toast.makeText(getApplicationContext(),"You are Ban !",Toast.LENGTH_LONG).show();
                        }
                        else if (response.equals("n")){
                            Toast.makeText(getApplicationContext(),"Email / Password Has Been Wrong !",Toast.LENGTH_LONG).show();
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
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_email",userEmail);
                params.put("user_pass",userPass);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    void post(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thelostclan.xyz/lostclan/user-profile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Login Response: " + response.toString());
                        try {
                            JSONObject jsonObject=new JSONObject(response);


                            SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("name",jsonObject.getString("name"));
                            editor.commit();
                            author=jsonObject.getString("name");

                            String name=jsonObject.getString("name");
                            String email=jsonObject.getString("email");
                            // Inserting row in users table
                            db.deleteUsers();
                            db.addUser(name, email);

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Login Error: " + error.getMessage());
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("post_email",author);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
