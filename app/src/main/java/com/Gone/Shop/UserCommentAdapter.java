package com.Gone.Shop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by joy on 4/21/17.
 */

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.MyViewHolder> {

    private ArrayList<UserCommentModel> postLists;
    private Context context;
    private SQLiteHandler db;
    private SessionManager session;
    //String uid;
    String name;
    String email;
    String postID;
    public UserCommentAdapter(Context context, List<UserCommentModel> postLists) {
        this.postLists = (ArrayList<UserCommentModel>)postLists;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       // View view= LayoutInflater.from(context).inflate(R.layout.card_view,parent,false);
        View view= LayoutInflater.from(context).inflate(R.layout.all_comment_card_view,parent,false);
        MyViewHolder holder=new MyViewHolder(view,context,postLists);
        return holder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final UserCommentModel newsModel=postLists.get(position);
        holder.name.setText(Html.fromHtml(newsModel.getUserName()));
        holder.comment.setText(Html.fromHtml(newsModel.getComment()));
       // holder.location.setText(Html.fromHtml(newsModel.getLocation()));
        holder.btn_delete.setVisibility(View.GONE);
        holder.btn_edit.setVisibility(View.GONE);
        //holder.btn_rating.setVisibility(View.GONE);
        holder.wrong.setVisibility(View.GONE);
        holder.right.setVisibility(View.GONE);

        Picasso.with(context).load(newsModel.getUserImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);


        // SqLite database handler
        db = new SQLiteHandler(context.getApplicationContext());
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        name = user.get("name");
        email = user.get("email");
        postID=newsModel.getPostID();

        //String user_email="a@gmail.com";
        if (email.equals(newsModel.getUserEmail())){
            holder.btn_delete.setVisibility(View.VISIBLE);
            holder.btn_edit.setVisibility(View.VISIBLE);

        }
        int ratingScore= Integer.parseInt(newsModel.getUserRating());
        if ((email.equals(newsModel.getWritterEmail())) && (ratingScore==0)){
            //holder.btn_rating.setVisibility(View.VISIBLE);
            if(newsModel.getWritterEmail().equals(newsModel.getUserEmail())) {
                holder.wrong.setVisibility(View.GONE);
            }
            else{
                holder.wrong.setVisibility(View.VISIBLE);
            }
        }
        if ((email.equals(newsModel.getWritterEmail())) && (ratingScore!=0)){
            holder.right.setVisibility(View.VISIBLE);
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_user_comment(newsModel.getId());
            }
        });
       holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UpdateYourComment.class);
                intent.putExtra("user_update", "user_update");
                intent.putExtra("commentId",newsModel.getId());
                intent.putExtra("comment",newsModel.getComment());
                intent.putExtra("commentDate",newsModel.getCommentDate());
                intent.putExtra("userEmail",newsModel.getUserEmail());
                intent.putExtra("userName",newsModel.getUserName());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MyProfileActivity.class);

                intent.putExtra("forum", "forum");
                intent.putExtra("email", newsModel.getUserEmail());

               /* intent.putExtra("commentId",newsModel.getId());
                intent.putExtra("comment",newsModel.getComment());
                intent.putExtra("commentDate",newsModel.getCommentDate());
                intent.putExtra("userEmail",newsModel.getUserEmail());
                intent.putExtra("userName",newsModel.getUserName());*/

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MyProfileActivity.class);

                intent.putExtra("forum", "forum");
                intent.putExtra("email", newsModel.getUserEmail());

                /*intent.putExtra("commentId",newsModel.getId());
                intent.putExtra("comment",newsModel.getComment());
                intent.putExtra("commentDate",newsModel.getCommentDate());
                intent.putExtra("userEmail",newsModel.getUserEmail());
                intent.putExtra("userName",newsModel.getUserName());*/

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



            }
        });
     /* holder.btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating_user(newsModel.getId(), newsModel.getUserEmail());

            }
        });*/
        holder.wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_rating_user(newsModel.getId(),newsModel.getUserEmail());
                holder.right.setVisibility(View.VISIBLE);
                holder.wrong.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public int getItemCount() {
        return postLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView comment;
        Button name;
        ImageButton imageView;
        Button btn_edit,btn_delete,btn_rating;
        ArrayList<UserCommentModel> postLists=new ArrayList<>();
        Context context;
        ImageButton right,wrong;

        public MyViewHolder(View itemView, Context context, ArrayList<UserCommentModel> postLists) {
            super(itemView);
            this.context=context;
            this.postLists=postLists;
            name=(Button)itemView.findViewById(R.id.card_name);
            comment=(TextView)itemView.findViewById(R.id.card_title);
            imageView=(ImageButton)itemView.findViewById(R.id.card_image);

            btn_delete=(Button)itemView.findViewById(R.id.btn_delete);
            btn_edit=(Button)itemView.findViewById(R.id.btn_edit);
            //btn_rating=(Button)itemView.findViewById(R.id.btn_comment);
            right=(ImageButton)itemView.findViewById(R.id.right);
            wrong=(ImageButton)itemView.findViewById(R.id.wrong);

            //itemView.setOnClickListener(this);

        }

      /*  @Override
        public void onClick(View v) {

            int position=getAdapterPosition();
            AllCommentModel newsModel=this.postLists.get(position);

            Intent intent=new Intent(this.context,MainActivity.class);
            intent.putExtra("title",newsModel.getUserName());
            intent.putExtra("content",newsModel.get());
            intent.putExtra("time",newsModel.getDate());


            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);


        }*/
    }
    void delete_user_comment(final String pos){


        StringRequest sq = new StringRequest(Request.Method.POST, "http://172.245.177.162/~sabbirah/UniShop/delete_comment.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(context,"Your comment deleted successfully!!!",Toast.LENGTH_LONG).show();
                finish();
                context.startActivity(new Intent(context,UserComment.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,"Network connection failed!!!",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,UserComment.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("commentid", pos);
                params.put("post_id", postID);


                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sq);

    }


    void user_rating_user(final String id, final String user_email){


        StringRequest sq = new StringRequest(Request.Method.POST, "http://172.245.177.162/~sabbirah/UniShop/rating_user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(context,"User rating completed",Toast.LENGTH_LONG).show();
                finish();
                context.startActivity(new Intent(context,UserComment.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,"Network connection failed!!!",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,UserComment.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("comment_id", id);
                params.put("user_email", user_email);

                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sq);

    }

    public void finish(){
        context.startActivity(new Intent(context,UserComment.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }


}
