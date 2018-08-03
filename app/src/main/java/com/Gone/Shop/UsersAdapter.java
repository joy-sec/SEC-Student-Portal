package com.Gone.Shop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private ArrayList<UsersModel> postLists;
    private Context context;

    public UsersAdapter(Context context, List<UsersModel> postLists) {
        this.postLists = (ArrayList<UsersModel>)postLists;
        this.context = context;
    }


    @Override
    public UsersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.alluser_card_view,parent,false);
        UsersAdapter.MyViewHolder holder=new UsersAdapter.MyViewHolder(view,context,postLists);
        return holder;
    }

    @Override
    public void onBindViewHolder(UsersAdapter.MyViewHolder holder, int position) {
        final UsersModel newsModel=postLists.get(position);


        holder.name.setText(Html.fromHtml(newsModel.getName()));
        holder.email.setText(Html.fromHtml(newsModel.getEmail()));

        holder.btn_enable.setVisibility(View.GONE);
        holder.btn_disable.setVisibility(View.GONE);

        if (newsModel.getImage().length()>6){
            Picasso.with(context).load(newsModel.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);
        }
        if (newsModel.getStatus().equals("1")){
            holder.btn_disable.setVisibility(View.VISIBLE);
        }
        if (newsModel.getStatus().equals("0")){
            holder.btn_enable.setVisibility(View.VISIBLE);
        }

        holder.btn_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enable(newsModel.getEmail());
            }
        });
        holder.btn_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disable(newsModel.getEmail());
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(newsModel.getEmail());
            }
        });

    }

    @Override
    public int getItemCount() {
        return postLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,email;
        ImageView imageView;
        Button btn_delete,btn_enable,btn_disable;
        ArrayList<UsersModel> postLists=new ArrayList<>();
        Context context;

        public MyViewHolder(View itemView, Context context, ArrayList<UsersModel> postLists) {
            super(itemView);
            this.context=context;
            this.postLists=postLists;

            name=(TextView)itemView.findViewById(R.id.user_name);
            email=(TextView)itemView.findViewById(R.id.user_email);
            btn_enable=(Button)itemView.findViewById(R.id.btn_enable);
            btn_disable=(Button)itemView.findViewById(R.id.btn_disable);
            imageView=(ImageView)itemView.findViewById(R.id.user_image);
            btn_delete=(Button)itemView.findViewById(R.id.btn_delete);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


        }
    }


    public void enable(final String user){

        StringRequest sq = new StringRequest(Request.Method.POST, "http://thelostclan.xyz/lostclan/block-user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(context,"enabled",Toast.LENGTH_LONG).show();

                context.startActivity(new Intent(context,UsersActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"SomeThing went wrong !",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("post_email",user);
                params.put("post_status","disable");

                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sq);

    }

    public void disable(final String user){


        StringRequest sq = new StringRequest(Request.Method.POST, "http://thelostclan.xyz/lostclan/block-user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(context,"Disabled ",Toast.LENGTH_LONG).show();

                context.startActivity(new Intent(context,UsersActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"SomeThing went wrong !",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("post_email",user);
                params.put("post_status","enable");

                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sq);

    }
    public void delete(final String user){


        StringRequest sq = new StringRequest(Request.Method.POST, "http://thelostclan.xyz/lostclan/block-user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(context," User Deleted",Toast.LENGTH_LONG).show();

                context.startActivity(new Intent(context,UsersActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"SomeThing went wrong !",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("post_email",user);
                params.put("post_status","delete");

                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sq);

    }

}