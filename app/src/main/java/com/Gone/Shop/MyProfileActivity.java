package com.Gone.Shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    TextView text_name,text_email,text_div,text_dist,text_blood,text_solved,text_unsolved,text_uni,text_id,text_contact,text_birth,text_rating;
    String sname,semail,sdivi,sdist,sblood,ssolved,sunsolved,simage,suni,sid,scontact,sbirth,srating;
    CircleImageView circleImageView;
    private Button edit,sendMessage;
    private ProgressDialog progressDialog;
    String forum_user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        text_name= (TextView) findViewById(R.id.name);
        text_email= (TextView) findViewById(R.id.email);
        text_blood=(TextView)findViewById(R.id.text_blood);
        text_dist=(TextView)findViewById(R.id.text_district);
        text_div=(TextView)findViewById(R.id.text_division);
        text_solved=(TextView)findViewById(R.id.text_solved);
        text_unsolved=(TextView)findViewById(R.id.text_unsolved);
        text_uni=(TextView)findViewById(R.id.uni);
        text_id=(TextView)findViewById(R.id.card);
        text_contact=(TextView)findViewById(R.id.ucontact);
        text_birth=(TextView)findViewById(R.id.ubirth);
        text_rating=(TextView)findViewById(R.id.urating);
        edit=(Button)findViewById(R.id.btn_profile_edit);
        sendMessage= (Button) findViewById(R.id.btn_send_message);
        circleImageView=(CircleImageView)findViewById(R.id.image);

        edit.setVisibility(View.GONE);



        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MyProfileActivity.this,ProfileEditActivity.class);

                intent.putExtra("name",sname);
                intent.putExtra("divi",sdivi);
                intent.putExtra("dist",sdist);
                intent.putExtra("blood",sblood);
                intent.putExtra("img",simage);
                intent.putExtra("uni",suni);
                intent.putExtra("id",sid);
                intent.putExtra("contact",scontact);
                intent.putExtra("birth",sbirth);
                intent.putExtra("rating",srating);

                startActivity(intent);

            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyProfileActivity.this,ChatRoom.class);
                intent.putExtra("check",true);
                intent.putExtra("email",semail);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        String forum_user=getIntent().getStringExtra("forum");
        if (forum_user!=null){
            if (forum_user.equals("forum")){
                forum_user_email=getIntent().getStringExtra("email");
                getProfileDataForForumUser(forum_user_email);
            }
        }
        else if(forum_user==null){
            getprofiledata();
        }

    }











    void getprofiledata()
    {

        progressDialog.show();
        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        final String user=sharedPreferences.getString("email","null");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thelostclan.xyz/lostclan/user-profile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            sname = jsonObject.getString("name");
                            semail=jsonObject.getString("email");
                            sdivi=jsonObject.getString("division");
                            sdist=jsonObject.getString("district");
                            sblood=jsonObject.getString("blood");
                            ssolved=jsonObject.getString("solved");
                            sunsolved=jsonObject.getString("unsolved");
                            simage=jsonObject.getString("image");
                            suni=jsonObject.getString("university");
                            sid=jsonObject.getString("idcard");
                            scontact=jsonObject.getString("contact_no");
                            sbirth=jsonObject.getString("date_of_birth");
                            srating=jsonObject.getString("user_rating");


                            text_email.setText(semail);
                            text_name.setText(sname);
                            text_blood.setText(sblood);
                            text_div.setText(sdivi);
                            text_dist.setText(sdist);
                            text_unsolved.setText(sunsolved);
                            text_solved.setText(ssolved);
                            text_uni.setText(suni);
                            text_id.setText(sid);
                            text_contact.setText(scontact);
                            text_birth.setText(sbirth);
                            text_rating.setText(srating);
                            if (simage.length()>10){
                                Picasso.with(MyProfileActivity.this).load(simage).error(R.mipmap.ic_launcher_round).into(circleImageView);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {

                            progressDialog.cancel();
                            edit.setVisibility(View.VISIBLE);
                            sendMessage.setVisibility(View.GONE);
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(),"Something went wrong !",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("post_email", user);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }














    void getProfileDataForForumUser(final String forum_user_email)
    {

        progressDialog.show();
        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        final String user=sharedPreferences.getString("email","null");
        if(forum_user_email.equals(user)){
            sendMessage.setVisibility(View.GONE);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thelostclan.xyz/lostclan/user-profile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            sname = jsonObject.getString("name");
                            semail=jsonObject.getString("email");
                            sdivi=jsonObject.getString("division");
                            sdist=jsonObject.getString("district");
                            sblood=jsonObject.getString("blood");
                            ssolved=jsonObject.getString("solved");
                            sunsolved=jsonObject.getString("unsolved");
                            simage=jsonObject.getString("image");
                            suni=jsonObject.getString("university");
                            sid=jsonObject.getString("idcard");
                            scontact=jsonObject.getString("contact_no");
                            sbirth=jsonObject.getString("date_of_birth");
                            srating=jsonObject.getString("user_rating");



                            text_email.setText(semail);
                            text_name.setText(sname);
                            text_blood.setText(sblood);
                            text_div.setText(sdivi);
                            text_dist.setText(sdist);
                            text_unsolved.setText(sunsolved);
                            text_solved.setText(ssolved);
                            text_uni.setText(suni);
                            text_id.setText(sid);
                            text_contact.setText(scontact);
                            text_birth.setText(sbirth);
                            text_rating.setText(srating);
                            if (simage.length()>10){
                                Picasso.with(MyProfileActivity.this).load(simage).error(R.mipmap.ic_launcher_round).into(circleImageView);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {

                            progressDialog.cancel();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(),"Something went wrong !",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("post_email", forum_user_email);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}