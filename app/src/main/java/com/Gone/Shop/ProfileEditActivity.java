package com.Gone.Shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileEditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText name,division,district,blood,password,uni,idcard,contact,birth;
    private ImageView circleImageView;
    private Button update;
    private Bitmap bitmap;
    private Uri filePath;
    private ProgressDialog progressDialog;
    String str_name,str_dist,str_div,str_blood,str_image,str_password,str_uni,str_idcard,str_contact,str_birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        name=(EditText)findViewById(R.id.edit_name);
        password= (EditText) findViewById(R.id.password);
        division=(EditText)findViewById(R.id.edit_division);
        district=(EditText)findViewById(R.id.edit_district);
        blood=(EditText)findViewById(R.id.edit_blood);
        uni=(EditText)findViewById(R.id.edit_uni);
        idcard=(EditText)findViewById(R.id.edit_id);
        contact=(EditText)findViewById(R.id.edit_contact);
        birth=(EditText)findViewById(R.id.edit_birth);
        update=(Button)findViewById(R.id.btn_submit);

        circleImageView=(ImageView)findViewById(R.id.edit_image);

        name.setText(getIntent().getStringExtra("name"));
        division.setText(getIntent().getStringExtra("divi"));
        district.setText(getIntent().getStringExtra("dist"));
        blood.setText(getIntent().getStringExtra("blood"));
        uni.setText(getIntent().getStringExtra("uni"));
        idcard.setText(getIntent().getStringExtra("id"));
        contact.setText(getIntent().getStringExtra("contact"));
        birth.setText(getIntent().getStringExtra("birth"));

        str_image=getIntent().getStringExtra("img");

        System.out.println("IMAFE :::"+str_image);

        if (str_image.length()>10){
            Picasso.with(ProfileEditActivity.this).load(str_image).error(R.mipmap.ic_launcher_round).into(circleImageView);
        }

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void showFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void submit(){

        progressDialog.show();
        String url = "https://thelostclan.xyz/lostclan/profile/edit-profile.php";
        str_password=password.getText().toString();
        str_name=name.getText().toString();
        str_dist=district.getText().toString();
        str_div=division.getText().toString();
        str_blood=blood.getText().toString();
        str_uni=uni.getText().toString();
        str_idcard=idcard.getText().toString();
        str_contact=contact.getText().toString();
        str_birth=birth.getText().toString();
        if (bitmap!=null){
            str_image=getStringImage(bitmap);
        }

        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        final String user=sharedPreferences.getString("email","null");
        Log.e("imahe",str_image);

        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(ProfileEditActivity.this,"Submitted",Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                startActivity(new Intent(ProfileEditActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileEditActivity.this,"SomeThing went wrong !",Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                startActivity(new Intent(ProfileEditActivity.this,ProblemListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("post_name",str_name);
                params.put("post_image",str_image);
                params.put("post_passward",str_password);
                params.put("post_blood",str_blood);
                params.put("post_district",str_dist);
                params.put("post_division",str_div);
                params.put("post_email",user);
                params.put("post_uni",str_uni);
                params.put("post_id",str_idcard);
                params.put("post_contact",str_contact);
                params.put("post_birth",str_birth);

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
        finish();
    }
}
