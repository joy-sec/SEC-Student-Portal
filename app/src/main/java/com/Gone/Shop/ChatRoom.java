package com.Gone.Shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Gone.Shop.helper.SQLiteHandler;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatRoom extends Activity {
    String ownerEmail,toEmail;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<MessageModel> messages=new ArrayList<>();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Button btn_send;
    EditText edt_message;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        edt_message= (EditText) findViewById(R.id.chat_message);
        btn_send= (Button) findViewById(R.id.btn_send);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        ownerEmail= user.get("email");
        toEmail =getIntent().getStringExtra("email");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        fetchMessages();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Constants.PUSH_NOTIFICATION)) {
                    //Getting message data
                    String from = intent.getStringExtra("from");
                    String message = intent.getStringExtra("message");
                    String time=intent.getStringExtra("time");
                    if(from.equals(toEmail)){
                        //processing the message to add it in current thread
                        processMessage(from, message,time);
                    }


                }
            }
        };
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edt_message.getText().toString().isEmpty()){
                    String str_message=edt_message.getText().toString();
                    String str_to=toEmail;
                    String str_from=ownerEmail;
                    String str_time=getTimeStamp();
                    edt_message.setText("");
                    send(str_message,str_to,str_from,str_time);
                }


            }
        });
    }
    public void send(final String message, final String to, final String from, final String time){
        String url = "http://thelostclan.xyz/UniShop/send-message.php";
        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                processMessage(from,message,time);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("post_mgs",message);
                params.put("post_to",to);
                params.put("post_from",from);
                params.put("post_time",time);
                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sq);


    }
    public void fetchMessages() {
        String url = "http://thelostclan.xyz/UniShop/messages-josn.php";
        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String message = obj.getString("message");
                        String to = obj.getString("to");
                        String from = obj.getString("from");
                        String time=obj.getString("time");
                        MessageModel messagObject = new MessageModel();
                        messagObject.setMessage(message);
                        messagObject.setTo(to);
                        messagObject.setFrom(from);
                        messagObject.setTime(time);
                        //messagObject.setTime(obj.getString("created_at"));
                        // System.out.println(message+to+from);

                        messages.add(messagObject);
                    }
                    Collections.reverse(messages);

                    adapter = new ChatRoomThreadAdapter(toEmail,ownerEmail,getApplicationContext(), messages);
                    recyclerView.setAdapter(adapter);
                    scrollToBottom();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("post_to",toEmail);
                params.put("post_from",ownerEmail);



                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sq);


    }
    private void processMessage(String from, String message, String time) {
        MessageModel messagObject = new MessageModel();
        messagObject.setMessage(message);
        messagObject.setFrom(from);
        messagObject.setTime(time);
        messages.add(messagObject);
        adapter.notifyDataSetChanged();
        scrollToBottom();
    }
    private void scrollToBottom() {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1)
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
    }
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.PUSH_NOTIFICATION));
    }
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
    public static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
    void back_press(){
        String url = "http://thelostclan.xyz/UniShop/chat-back.php";
        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("post_to",toEmail);
                params.put("post_from",ownerEmail);
                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sq);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back_press();
        finish();
        //Intent intent=new Intent(getApplicationContext(),Message.class);
        //startActivity(intent);
    }


}
