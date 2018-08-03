package com.Gone.Shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText name,email,pass,division,dist,blood,uni,idcard,contact_no,date_of_birth;
    String userName,userEmail,userPass,userDivision,userDist,userBlood,ck,userUni,userIdcard,userContact_no,userDate_of_birth,str_image;
    ImageView profile_image;
    private Button signUp;
    private ProgressDialog progressDialog;
    private Bitmap bitmap;
    private Uri filePath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();

        name=(EditText)findViewById(R.id.name);
        email= (EditText) findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.pass);
        division=(EditText)findViewById(R.id.division);
        dist=(EditText)findViewById(R.id.zilla);
        blood=(EditText)findViewById(R.id.blood);
        uni=(EditText)findViewById(R.id.university);
        idcard=(EditText)findViewById(R.id.idcard);
        contact_no=(EditText)findViewById(R.id.contact);
        date_of_birth=(EditText)findViewById(R.id.birth_date);
        signUp=(Button)findViewById(R.id.btn_sign_up);
        //profile_image= (ImageView) findViewById(R.id.profile_image);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");
       /* profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();

            }
        });*/


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    post_send();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }});


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

    void post_send() throws JSONException {

        progressDialog.show();
        userName=name.getText().toString().trim();
        userEmail=email.getText().toString().trim();
        userDivision=division.getText().toString().trim();
        userDist=dist.getText().toString().trim();
        userBlood=blood.getText().toString().trim();
        userPass=pass.getText().toString().trim();
        userUni=uni.getText().toString().trim();
        userIdcard=idcard.getText().toString().trim();
        userContact_no=contact_no.getText().toString().trim();
        userDate_of_birth=date_of_birth.getText().toString().trim();

        //System.out.println(userBlood+userPass+userEmail+userName+userDist+userDivision);
       // if(bitmap!=null){
          //  str_image=getStringImage(bitmap);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thelostclan.xyz/lostclan/registration.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.cancel();
                            if (response.equals("same")){
                                Toast.makeText(getApplicationContext(),"Email Already Exist",Toast.LENGTH_LONG).show();
                            }
                            else if (response.equals("p")){
                                Toast.makeText(getApplicationContext(),"Please fill all values",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"Something went wrong ! Try again",Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("user_name",userName);
                    params.put("user_password",userPass);
                   // params.put("post_image",str_image);
                    params.put("user_email", userEmail);
                    params.put("user_division",userDivision);
                    params.put("user_district",userDist);
                    params.put("user_blood",userBlood);
                    params.put("user_uni",userUni);
                    params.put("user_id",userIdcard);
                    params.put("contact_no",userContact_no);
                    params.put("date_of_birth",userDate_of_birth);

                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
       // }
        //else {
            //Toast.makeText(RegistrationActivity.this,"SELECT AN IMAGE",Toast.LENGTH_LONG).show();
       // }




        }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
