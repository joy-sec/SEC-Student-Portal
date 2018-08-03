package com.Gone.Shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class forum extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

    }
    public void cmnt(View view){

        startActivity(new Intent(forum.this,comment.class));
    }
}
