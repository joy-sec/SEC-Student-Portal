package com.Gone.Shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by joy on 4/22/17.
 */

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyViewHolder> {
    private ArrayList<AllPostModel> postLists;
    private Context context;
    private int id;

    public MyPostAdapter(Context context, List<AllPostModel> posLists) {
        this.postLists= (ArrayList<AllPostModel>) posLists;
        this.context = context;
    }

    @Override
    public MyPostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.my_post_card_view,parent,false);
        MyPostAdapter.MyViewHolder holder=new MyViewHolder(view,context,postLists);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyPostAdapter.MyViewHolder holder, final int position) {
        final AllPostModel newsModel=postLists.get(position);
        holder.title.setText(Html.fromHtml(newsModel.getTitle()));
        holder.des.setText(Html.fromHtml(newsModel.getDetails()));
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ProblemAddActivity.class);
                intent.putExtra("id",newsModel.getId());
                intent.putExtra("auth","update");
                intent.putExtra("title",newsModel.getTitle());
                //intent.putExtra("URL",newsModel.getUrl());
                intent.putExtra("content",newsModel.getDetails());
                //intent.putExtra("date",newsModel.getDate());
                intent.putExtra("image",newsModel.getImage());
                intent.putExtra("loc",newsModel.getLocation());
                intent.putExtra("lat",newsModel.getLatitude());
                intent.putExtra("lon",newsModel.getLongitude());
                intent.putExtra("div",newsModel.getDivision());
                intent.putExtra("dist",newsModel.getDistrict());


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



            }
        });
        Picasso.with(context).load(newsModel.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_post(newsModel.getId());

            }
        });

        if (newsModel.getStatus().equals("solved")){
            holder.solved.setVisibility(View.GONE);
        }
        else {
            holder.solved.setVisibility(View.VISIBLE);
        }
        holder.solved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                status(newsModel.getId(),newsModel.getStatus());
                holder.solved.setVisibility(View.GONE);

            }
        });
    }



    @Override
    public int getItemCount() {
        return postLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,des;
        ImageView imageView;
        Button edit,delete,solved;
        ArrayList<AllPostModel> postLists=new ArrayList<>();
        Context context;

        public MyViewHolder(View itemView, Context context, ArrayList<AllPostModel> postLists) {
            super(itemView);
            this.context=context;
            this.postLists=postLists;
            title=(TextView)itemView.findViewById(R.id.card_title);
            des=(TextView)itemView.findViewById(R.id.card_des);
            imageView=(ImageView)itemView.findViewById(R.id.card_image);
            delete=(Button)itemView.findViewById(R.id.btn_delete);
            solved=(Button)itemView.findViewById(R.id.btn_solved);
            edit=(Button)itemView.findViewById(R.id.btn_edit);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();

            AllPostModel newsModel=this.postLists.get(position);
            Intent intent=new Intent(this.context,AllPostDetailsActivity.class);
            intent.putExtra("title",newsModel.getTitle());
            //intent.putExtra("URL",newsModel.getUrl());
            intent.putExtra("content",newsModel.getDetails());
            //intent.putExtra("date",newsModel.getDate());
            intent.putExtra("image",newsModel.getImage());
            intent.putExtra("loc",newsModel.getLocation());
            intent.putExtra("time",newsModel.getDate());
            intent.putExtra("status",newsModel.getStatus());
            intent.putExtra("lat",newsModel.getLatitude());
            intent.putExtra("lon",newsModel.getLongitude());
            intent.putExtra("division",newsModel.getDivision());
            intent.putExtra("district",newsModel.getDistrict());

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }

    void delete_post(final String pos){


        StringRequest sq = new StringRequest(Request.Method.POST, "http://thelostclan.xyz/lostclan/delete_post.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,MyProblemActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,"Something went wrong !",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,MyProblemActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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

    void status(final String id,final String status){

        SharedPreferences sharedPreferences=context.getSharedPreferences("login",MODE_PRIVATE);
        final String email=sharedPreferences.getString("email","null");

        StringRequest sq = new StringRequest(Request.Method.POST, "http://thelostclan.xyz/lostclan/solve-status.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Solved",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,MyProblemActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong !",Toast.LENGTH_LONG).show();

                context.startActivity(new Intent(context,MyProblemActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("postid", id);
                params.put("post_email",email);
                params.put("post_status",status);

                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sq);



    }
}
