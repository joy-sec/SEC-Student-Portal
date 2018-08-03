package com.Gone.Shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pappu-pc on 6/20/2017.
 */

public class ChatRoomThreadAdapter extends RecyclerView.Adapter<ChatRoomThreadAdapter.ViewHolder> {
    private String userId;
    private int SELF = 100;
    private int other=101;
    private static String today;
    private String senderEmail;

    public ChatRoomThreadAdapter(String senderEmail, String userId, Context mContext, ArrayList<MessageModel> messageArrayList) {
        this.senderEmail=senderEmail;
        this.userId = userId;
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;
        Calendar calendar=Calendar.getInstance();
        today=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    private Context mContext;
    private ArrayList<MessageModel> messageArrayList;

    @Override
    public ChatRoomThreadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
        }


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageModel messageModel=messageArrayList.get(position);
       holder.message.setText(messageModel.getMessage());
       String timestamp;
       // if (messageModel.getTo() != null)
           timestamp = messageModel.getFrom() + ", " + messageModel.getTime();

        holder.timestamp.setText(timestamp);

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }
    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageArrayList.get(position);
        if (message.getFrom().equals(userId)) {
            return SELF;
        }

       return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView message, timestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        }
    }
    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
}
