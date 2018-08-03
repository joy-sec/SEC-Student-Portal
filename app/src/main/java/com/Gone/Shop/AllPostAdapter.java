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



public class AllPostAdapter extends RecyclerView.Adapter<AllPostAdapter.MyViewHolder> {

    private ArrayList<AllPostModel> postLists;
    private Context context;

    public AllPostAdapter(Context context, List<AllPostModel> postLists) {
        this.postLists = (ArrayList<AllPostModel>)postLists;
        this.context = context;
    }


    @Override
    public AllPostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.card_view,parent,false);
        MyViewHolder holder=new MyViewHolder(view,context,postLists);
        return holder;
    }

    @Override
    public void onBindViewHolder(AllPostAdapter.MyViewHolder holder, int position) {
        final AllPostModel newsModel=postLists.get(position);
        holder.title.setText(Html.fromHtml(newsModel.getTitle()));
//        holder.des.setText(Html.fromHtml(newsModel.getDetails()));
        holder.price.setText(Html.fromHtml(newsModel.getprice()));
        holder.location.setText(Html.fromHtml(newsModel.getLocation()));
        holder.btn_delete.setVisibility(View.GONE);
        Picasso.with(context).load(newsModel.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

        SharedPreferences sharedPreferences=context.getSharedPreferences("login",MODE_PRIVATE);
        String user= sharedPreferences.getString("email",null);

        if (user.equals("joy.appincubator@gmail.com")){
            holder.btn_delete.setVisibility(View.VISIBLE);
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_post(newsModel.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return postLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,des,price,location;
        ImageView imageView;
        Button btn_delete;
        ArrayList<AllPostModel> postLists=new ArrayList<>();
        Context context;

        public MyViewHolder(View itemView, Context context, ArrayList<AllPostModel> postLists) {
            super(itemView);
            this.context=context;
            this.postLists=postLists;
            title=(TextView)itemView.findViewById(R.id.card_title);
           // des=(TextView)itemView.findViewById(R.id.card_des);
            price=(TextView)itemView.findViewById(R.id.price);
            location=(TextView)itemView.findViewById(R.id.card_location);
            imageView=(ImageView)itemView.findViewById(R.id.card_image);
            btn_delete=(Button)itemView.findViewById(R.id.btn_delete);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();
            AllPostModel newsModel=this.postLists.get(position);
            Intent intent=new Intent(this.context,AllPostDetailsActivity.class);
            intent.putExtra("title",newsModel.getTitle());
            intent.putExtra("content",newsModel.getDetails());
            intent.putExtra("price",newsModel.getprice());
            intent.putExtra("image",newsModel.getImage());
            intent.putExtra("loc",newsModel.getLocation());
            intent.putExtra("time",newsModel.getDate());
            intent.putExtra("lat",newsModel.getLatitude());
            intent.putExtra("lon",newsModel.getLongitude());
            intent.putExtra("status",newsModel.getStatus());
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
                context.startActivity(new Intent(context,ProblemListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,"Something went wrong !",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,ProblemListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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
}
