package com.Gone.Shop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Gone.Shop.helper.SQLiteHandler;

import java.util.ArrayList;

/**
 * Created by pappu on 7/11/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private String user;
    private ArrayList<User_model> postLists;
    private Context context;

    public MessageAdapter(ArrayList<User_model> postLists, Context context) {
        this.postLists = postLists;
        this.context = context;

    }

    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.message_list,parent,false);
        MessageAdapter.MyViewHolder holder=new MessageAdapter.MyViewHolder(view,context,postLists);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User_model model=postLists.get(position);
        holder.name.setText(model.getName());
        holder.last_message.setText(model.getLast_message());
        holder.time.setText(model.getTime());
        if (model.getCount() > 0) {
            holder.count.setText(String.valueOf(model.getCount()));
            holder.count.setVisibility(View.VISIBLE);
            holder.last_message.setTextAppearance(context, R.style.largeText);
            holder.last_message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        } else {
            holder.count.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return postLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,last_message,time,count;
        ArrayList<User_model> postLists=new ArrayList<>();
        Context context;
        public MyViewHolder(View itemView, Context context, ArrayList<User_model> postLists) {
            super(itemView);
            this.context=context;
            this.postLists=postLists;
            name= (TextView) itemView.findViewById(R.id.message_name);
            last_message= (TextView) itemView.findViewById(R.id.message);
            time= (TextView) itemView.findViewById(R.id.timestamp);
            count= (TextView) itemView.findViewById(R.id.count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            Intent intent;
            User_model newsModel=this.postLists.get(position);
            intent=new Intent(context, ChatRoom.class);



           intent.putExtra("check",true);
            intent.putExtra("email",newsModel.getUserEmail());

           System.out.println(newsModel.getUserEmail());

           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);



        }
    }


}
