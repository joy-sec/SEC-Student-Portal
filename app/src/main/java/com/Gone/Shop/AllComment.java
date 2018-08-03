package com.Gone.Shop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.Gone.Shop.helper.SQLiteHandler;
import com.Gone.Shop.helper.SQLiteHandlerForUserStatus;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

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


public class AllComment extends AppCompatActivity{

    private TextView post,date,total_comment;
    Button name;
    private EditText comment;
    private Button btn_submit;
    ImageButton imageView;

    private ProgressDialog progressDialog;
    private String user_email,user_name,writter_name,writter_email,writter_image;
    String status_details,status_total_comment,status_time;
    String postID;
    int post_id;
    String user_comment;

    private Context context;
    public static List<AllCommentModel> postLists = new ArrayList<>();
    RecyclerView recyclerView;
    AllCommentAdapter postAdapter;


    private SQLiteHandlerForUserStatus dbb;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_comment_box);
        setContentView(R.layout.all_comment);

        postAdapter=new AllCommentAdapter(getApplicationContext(), postLists);
        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(postAdapter);

        name=(Button)findViewById(R.id.post_writter);
        post=(TextView)findViewById(R.id.post_title);
        //date=(TextView)findViewById(R.id.date_time);
        total_comment=(TextView)findViewById(R.id.post_total_comment);
        comment=(EditText)findViewById(R.id.comment);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        imageView=(ImageButton)findViewById(R.id.post_writter_image);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");



        //new task
        // SqLite database handler
        dbb = new SQLiteHandlerForUserStatus(getApplicationContext());
        // Fetching user details from SQLite
        HashMap<String, String> user = dbb.getUserDetails();
        writter_name= user.get("name");
        writter_email = user.get("email");
        writter_image= user.get("image");
        postID = user.get("statusid");
        status_details= user.get("status");
        status_total_comment = user.get("totalcomment");
        status_time= user.get("time");
        post_id= Integer.parseInt(postID);
        name.setText(Html.fromHtml(writter_name));
        post.setText(Html.fromHtml(status_details));
        total_comment.setText("Total Answer("+Html.fromHtml(status_total_comment+")"));
        //date.setText(Html.fromHtml(status_time));
        Picasso.with(context).load(writter_image).placeholder(R.mipmap.ic_launcher).into(imageView);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Fetching user details from SQLite
        HashMap<String, String> user1 = db.getUserDetails();
        user_name= user1.get("name");
        user_email = user1.get("email");
        //new task end
        Log.e("tag",writter_email+user_email);



      /*  postID=getIntent().getStringExtra("id");
        post_id= Integer.parseInt(postID);
        name.setText(Html.fromHtml(getIntent().getStringExtra("writter_name")));
        post.setText(Html.fromHtml(getIntent().getStringExtra("post")));
        total_comment.setText("Total Answer("+Html.fromHtml(getIntent().getStringExtra("total_comment")+")"));
        //date.setText(Html.fromHtml(getIntent().getStringExtra("time")));
        writter_email=getIntent().getStringExtra("writter_email");
        writter_name=getIntent().getStringExtra("writter_name");
        writter_image=getIntent().getStringExtra("writter_image");

        Picasso.with(context).load(getIntent().getStringExtra("writter_image")).placeholder(R.mipmap.ic_launcher).into(imageView);

        user_email=getIntent().getStringExtra("user_email");
        user_name=getIntent().getStringExtra("user_name");

*/

        gettingAllComment();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comment_submit();
                sendNotification(user_email+" comment on your post","comment",writter_email);
            }


        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AllComment.this, MyProfileActivity.class);
                intent.putExtra("forum", "forum");
                intent.putExtra("email", writter_email);
               /*
                intent.putExtra("writter_email",writter_email);
                intent.putExtra("writter_name",writter_name);
                intent.putExtra("writter_image", writter_image);*/
                startActivity(intent);
            }


        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AllComment.this, MyProfileActivity.class);
                intent.putExtra("forum", "forum");
                intent.putExtra("email", writter_email);
               /*
                intent.putExtra("writter_email",writter_email);
                intent.putExtra("writter_name",writter_name);
                intent.putExtra("writter_image",writter_image);*/
                startActivity(intent);
            }


        });


    }


    private void comment_submit(){

        String url = "http://172.245.177.162/~sabbirah/UniShop/adding-comment.php";
        user_comment=comment.getText().toString().trim();


        if (user_comment.length()!=0){

            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Your comment is uploading...");
            progressDialog.show();


            StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(AllComment.this, "Your comment uploaded", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    finish();
                    startActivity(new Intent(AllComment.this,AllComment.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    finish();
                    Toast.makeText(AllComment.this,"Network connection failed!!!",Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    //startActivity(new Intent(AllComment.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }
            }) {
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("user_comment",user_comment);
                    params.put("post_id",postID);
                    params.put("user_email",user_email);
                    params.put("comment_date",getDate());

                    return params;
                }

            };

            sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(sq);
        }

        else {
            Log.e("ERROR  ::::   ", "  COMMENTS :::" + user_comment);

            Toast.makeText(AllComment.this,"Please enter your comment",Toast.LENGTH_SHORT).show();

        }

    }




    private String getDate(){

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        String date = df.format(Calendar.getInstance().getTime());

        return date;
    }

    void gettingAllComment(){

        recyclerView.removeAllViews();
        postLists.clear();

        progressDialog.show();
        String myURL = "http://172.245.177.162/~sabbirah/UniShop/all_comments.php";

        progressDialog.show();


        StringRequest sq = new StringRequest(Request.Method.POST, myURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    progressDialog.cancel();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            AllCommentModel postModel = new AllCommentModel();

                            postModel.setId(jsonObject.getString("commentID"));
                            postModel.setComment(jsonObject.getString("userComment"));
                            postModel.setPostID(jsonObject.getString("postID"));
                            postModel.setUserEmail(jsonObject.getString("userEmail"));
                            postModel.setUserName(jsonObject.getString("name"));
                            postModel.setUserImage(jsonObject.getString("image"));
                            postModel.setCommentDate(jsonObject.getString("commentDate"));
                            postModel.setUserRating(jsonObject.getString("comment_user_rating"));
                            postModel.setWritterEmail(writter_email);


                            //  postModel.setLongitude(jsonObject.getString("longitude"));
                            //   postModel.setImage(jsonObject.getString("image"));
                            //  postModel.setLocation(jsonObject.getString("location"));
                            //  postModel.setStatus(jsonObject.getString("status"));
                            //   postModel.setDistrict(jsonObject.getString("district"));
                            //  postModel.setDivision(jsonObject.getString("division"));

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
                    finish();
                    Toast.makeText(AllComment.this,"Network connection failed!!!",Toast.LENGTH_LONG).show();

                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("post_id", postID);

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(sq);
        }
    void sendNotification(final String mgs, final String title,final String email){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://thelostclan.xyz/UniShop/send-notification-one.php",
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
                params.put("email",email);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    }
