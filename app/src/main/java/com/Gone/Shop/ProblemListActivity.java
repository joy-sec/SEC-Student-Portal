package com.Gone.Shop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemListActivity extends Activity implements AdapterView.OnItemSelectedListener {

    public static List<AllPostModel> postLists = new ArrayList<>();
    private Button filter;
    RecyclerView recyclerView;
    AllPostAdapter postAdapter;
    private ProgressDialog progressDialog;
    private Spinner state,city;
    List<String> citis=new ArrayList<String>();
    ArrayAdapter adapter,unit_adapter;
    String str_division,str_district;
    CustomMenu rightMenu;
    CustomMenu customMenu;
    SharedPreferences sharedPreferences;

    private static final String TAG = ProblemListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_list);


        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        customMenu = new CustomMenu(this);
        customMenu.setContentView(R.layout.activity_problem_list);

        customMenu.setRightShadow(R.drawable.shadow_right);
        rightMenu  = new CustomMenu(this);
        rightMenu.setContentView(R.layout.activity_drawer);
        customMenu.setRightMenu(rightMenu);
        customMenu.setRightMenu(rightMenu);
        setContentView(customMenu);





        filter=(Button)findViewById(R.id.filter);
        postAdapter=new AllPostAdapter(getApplicationContext(), postLists);
        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(postAdapter);


        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");

        state=(Spinner)findViewById(R.id.spinner);
        city= (Spinner) findViewById(R.id.spinner1);
        state.setOnItemSelectedListener(this);
        adapter=ArrayAdapter.createFromResource(this,R.array.State_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        state.setAdapter(adapter);
        city.setOnItemSelectedListener(this);
        fetchingData();



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter();
            }
        });


    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int pos;
        pos=state.getSelectedItemPosition();
        int iden=parent.getId();
        if(iden==R.id.spinner)
        {

            str_division=state.getSelectedItem().toString();
            switch (pos)
            {
                case 0:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Dhaka_array_filter,android.R.layout.simple_spinner_item
                );
                    break;
                case 1:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Chittagong_array_filter,android.R.layout.simple_spinner_item
                );
                    break;
                case 2:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Rajshashi_array_filter,android.R.layout.simple_spinner_item
                );
                    break;
                case 3:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Sylhet_array_filter,android.R.layout.simple_spinner_item
                );
                    break;
                case 4:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Barishal_array_filter,android.R.layout.simple_spinner_item
                );
                    break;
                case 5:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Khulna_array_filter,android.R.layout.simple_spinner_item
                );
                    break;
                case 6:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Rangpur_array_filter,android.R.layout.simple_spinner_item
                );
                    break;
                case 7:unit_adapter=ArrayAdapter.createFromResource(
                        this,R.array.Mymensingh_array_filter,android.R.layout.simple_spinner_item
                );
                    break;
                default:
                    break;

            }

            city.setAdapter(unit_adapter);
            unit_adapter.setDropDownViewResource(R.layout.spinner_item);
        }

        str_district=city.getSelectedItem().toString();




    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
        void fetchingData(){

            recyclerView.removeAllViews();
            postLists.clear();

            progressDialog.show();
            String myURL = "https://thelostclan.xyz/lostclan/all_posts_json.php";

            JsonArrayRequest mainArrayReq= new JsonArrayRequest(myURL, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    if (response.isNull(0)){
                        progressDialog.cancel();
                        Toast.makeText(ProblemListActivity.this,"There is no post",Toast.LENGTH_LONG).show();
                    }

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = (JSONObject) response.get(i);
                                    AllPostModel postModel = new AllPostModel();

                                    postModel.setTitle(jsonObject.getString("postTitle"));
                                    postModel.setName(jsonObject.getString("userName"));
                                    postModel.setDetails(jsonObject.getString("postDetails"));
                                    postModel.setprice(jsonObject.getString("price"));
                                    postModel.setDate(jsonObject.getString("postDate"));
                                    postModel.setLatitude(jsonObject.getString("latitude"));
                                    postModel.setLongitude(jsonObject.getString("longitude"));
                                    postModel.setImage(jsonObject.getString("image"));
                                    postModel.setLocation(jsonObject.getString("location"));
                                    postModel.setStatus(jsonObject.getString("status"));
                                    postModel.setDistrict(jsonObject.getString("district"));
                                    postModel.setDivision(jsonObject.getString("division"));
                                    postModel.setId(jsonObject.getString("postID"));

                                    postLists.add(postModel);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } finally {
                                    postAdapter.notifyItemChanged(i);
                                    progressDialog.cancel();

                                }
                            }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.cancel();
                    Toast.makeText(ProblemListActivity.this,"Something went wrong !",Toast.LENGTH_LONG).show();

                }
            });

            AppController.getInstance().addToRequestQueue(mainArrayReq);

    }


    void filter(){

        progressDialog.show();

        recyclerView.removeAllViews();
        postLists.clear();

        String url = "https://thelostclan.xyz/lostclan/search-posts.php";

        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        final String user=sharedPreferences.getString("email","null");

        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.length()<3){
                    progressDialog.cancel();
                    Toast.makeText(ProblemListActivity.this,"There is no post",Toast.LENGTH_LONG).show();
                }

                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            AllPostModel postModel = new AllPostModel();

                            postModel.setTitle(jsonObject.getString("postTitle"));
                            postModel.setName(jsonObject.getString("userName"));
                            postModel.setDetails(jsonObject.getString("postDetails"));
                            postModel.setprice(jsonObject.getString("price"));
                            postModel.setDate(jsonObject.getString("postDate"));
                            postModel.setLatitude(jsonObject.getString("latitude"));
                            postModel.setLongitude(jsonObject.getString("longitude"));
                            postModel.setImage(jsonObject.getString("image"));
                            postModel.setLocation(jsonObject.getString("location"));
                            postModel.setId(jsonObject.getString("postID"));
                            postModel.setDistrict(jsonObject.getString("district"));
                            postModel.setDivision(jsonObject.getString("division"));
                            postModel.setStatus(jsonObject.getString("status"));

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
                Toast.makeText(ProblemListActivity.this,"SomeThing went wrong !",Toast.LENGTH_LONG).show();
                progressDialog.cancel();

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("post_division",str_division);
                params.put("post_district",str_district);

                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sq);

    }
    private void rightMenu() {
        if (customMenu.getState() == CustomMenu.State.CLOSE_MENU) {
            customMenu.openRightMenuIfPossible();
        } else if (customMenu.getState() == CustomMenu.State.RIGHT_MENU_OPENS) {
            customMenu.closeMenu();
        } else {
            Log.e(TAG, "CustomMenu State:" + customMenu.getState());
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btn_left_menu:
//                leftMenu();
//                break;
            case R.id.btn_right_menu:
                rightMenu();
                break;
        }
    }
    public void addProblem(View v){
        startActivity(new Intent(ProblemListActivity.this,ProblemAddActivity.class));
    }
    public void problemList(View view){

        startActivity(new Intent(ProblemListActivity.this,ProblemListActivity.class));
    }
    public void myProblemList(View view){

        startActivity(new Intent(ProblemListActivity.this,MyProblemActivity.class));
    }
    public void profile(View view){

        startActivity(new Intent(ProblemListActivity.this,MyProfileActivity.class));
    }
    public void forum(View view){

        startActivity(new Intent(ProblemListActivity.this,AllStatus.class));
    }

    public void log(View view){
        sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("login",false);
        editor.commit();
        startActivity(new Intent(ProblemListActivity.this,LoginActivity.class));
        finish();
    }
}
