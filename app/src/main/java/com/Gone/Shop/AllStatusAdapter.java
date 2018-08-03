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

import com.Gone.Shop.helper.SQLiteHandlerForUserStatus;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.Gone.Shop.helper.SQLiteHandler;
import com.Gone.Shop.helper.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AllStatusAdapter extends RecyclerView.Adapter<AllStatusAdapter.MyViewHolder> {

    private ArrayList<AllStatusModel> postLists;
    private Context context;
    private SQLiteHandler db;
    private SessionManager session;
    //String uid;
    String name;
    String email;
    String user_email;


    private static final String TAGG = AllStatusAdapter.class.getSimpleName();
    private SQLiteHandlerForUserStatus dbb;



    public AllStatusAdapter(Context context, List<AllStatusModel> postLists) {
        this.postLists = (ArrayList<AllStatusModel>)postLists;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(context).inflate(R.layout.all_status_card_view,parent,false);
        MyViewHolder holder=new MyViewHolder(view,context,postLists);
        return holder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final AllStatusModel newsModel = postLists.get(position);
        if((newsModel.getPost().length()>200)){
            holder.post.setText(Html.fromHtml(newsModel.getPost().substring(0,200)+"......CONTINUE READING......"));
        }
        else if((newsModel.getPost().length()<200)){
            holder.post.setText(Html.fromHtml(newsModel.getPost()));
        }
        holder.post_user_name.setText(Html.fromHtml(newsModel.getPostUserName()));
        holder.btn_comment.setText("Answer("+Html.fromHtml(newsModel.getTotalComment()+")"));
        holder.btn_delete.setVisibility(View.GONE);
        holder.btn_edit.setVisibility(View.GONE);
        Picasso.with(context).load(newsModel.getPostUserImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);


        // SqLite database handler
        db = new SQLiteHandler(context.getApplicationContext());
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        name = user.get("name");
        email = user.get("email");



        if (email.equals(newsModel.getEmail())) {
            holder.btn_delete.setVisibility(View.VISIBLE);
            holder.btn_edit.setVisibility(View.VISIBLE);
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_status(newsModel.getId());
            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateYourStatus.class);
                intent.putExtra("id", newsModel.getId());
                intent.putExtra("post", newsModel.getPost());
                intent.putExtra("time", newsModel.getDate());
                intent.putExtra("email", newsModel.getEmail());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });
     /*  holder.btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllComment.class);
                intent.putExtra("id", newsModel.getId());
                intent.putExtra("post", newsModel.getPost());
                intent.putExtra("time", newsModel.getDate());
                intent.putExtra("total_comment", newsModel.getTotalComment());
                intent.putExtra("writter_email", newsModel.getEmail());
                intent.putExtra("writter_name", newsModel.getPostUserName());
                intent.putExtra("writter_image", newsModel.getPostUserImage());
                intent.putExtra("user_email", email);
                intent.putExtra("user_name", name);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });*/



        holder.post_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyProfileActivity.class);
                intent.putExtra("forum", "forum");
                intent.putExtra("email", newsModel.getEmail());
               /* intent.putExtra("id", newsModel.getId());
                intent.putExtra("post", newsModel.getPost());
                intent.putExtra("time", newsModel.getDate());
                intent.putExtra("total_comment", newsModel.getTotalComment());
                intent.putExtra("writter_email", newsModel.getEmail());
                intent.putExtra("writter_name", newsModel.getPostUserName());
                intent.putExtra("writter_image", newsModel.getPostUserImage());
                intent.putExtra("user_email", email);
                intent.putExtra("user_name", name);*/
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyProfileActivity.class);
                intent.putExtra("forum", "forum");
                intent.putExtra("email", newsModel.getEmail());
               /*intent.putExtra("id", newsModel.getId());
                intent.putExtra("post", newsModel.getPost());
                intent.putExtra("time", newsModel.getDate());
                intent.putExtra("total_comment", newsModel.getTotalComment());
                intent.putExtra("writter_email", newsModel.getEmail());
                intent.putExtra("writter_name", newsModel.getPostUserName());
                intent.putExtra("writter_image", newsModel.getPostUserImage());
                intent.putExtra("user_email", email);
                intent.putExtra("user_name", name);*/

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });


    }



    @Override
    public int getItemCount() {
        return postLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView post;
        Button post_user_name;
        ImageButton imageView;
        Button btn_edit,btn_delete,btn_comment;
        ArrayList<AllStatusModel> postLists=new ArrayList<>();
        Context context;

        public MyViewHolder(View itemView, Context context, ArrayList<AllStatusModel> postLists) {
            super(itemView);
            this.context=context;
            this.postLists=postLists;
            post=(TextView)itemView.findViewById(R.id.card_title);
            post_user_name=(Button)itemView.findViewById(R.id.card_name);
            imageView=(ImageButton)itemView.findViewById(R.id.card_image);
            btn_delete=(Button)itemView.findViewById(R.id.btn_delete);
            btn_edit=(Button)itemView.findViewById(R.id.btn_edit);
            btn_comment=(Button)itemView.findViewById(R.id.btn_comment);

            btn_comment.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();
            AllStatusModel newsModel=this.postLists.get(position);

            Intent intent = new Intent(context, AllComment.class);
            intent.putExtra("id", newsModel.getId());
            intent.putExtra("post", newsModel.getPost());
            intent.putExtra("time", newsModel.getDate());
            intent.putExtra("total_comment", newsModel.getTotalComment());
            intent.putExtra("writter_email", newsModel.getEmail());
            intent.putExtra("writter_name", newsModel.getPostUserName());
            intent.putExtra("writter_image", newsModel.getPostUserImage());
            intent.putExtra("user_email", email);
            intent.putExtra("user_name", name);



            //new task
            // SQLite database handler
            dbb = new SQLiteHandlerForUserStatus(context.getApplicationContext());
            String p_name=newsModel.getPostUserName();
            String p_email=newsModel.getEmail();
            String p_image=newsModel.getPostUserImage();
            String p_status_id=newsModel.getId();
            String p_status=newsModel.getPost();
            String p_total_comment=newsModel.getTotalComment();
            String p_time=newsModel.getDate();
            // Inserting row in users table
            dbb.deleteUsers();
            dbb.addUser(p_name, p_email, p_image, p_status_id, p_status, p_total_comment, p_time);
            //new task end



            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            finish2();

        }
    }
    void delete_status(final String pos){


        StringRequest sq = new StringRequest(Request.Method.POST, "http://172.245.177.162/~sabbirah/UniShop/delete_post.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(context,"Your status deleted successfully!!!",Toast.LENGTH_LONG).show();
                finish();
                context.startActivity(new Intent(context,AllStatus.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,"Network connection failed!!!",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,AllStatus.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("postid", pos);

                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sq);

    }

public void finish(){
    context.startActivity(new Intent(context,AllStatus.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
}
    public void finish2(){
        context.startActivity(new Intent(context,AllComment.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }




}
