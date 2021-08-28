package com.example.health_care_app.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.health_care_app.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private ImageView img;
    private TextView txt;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=findViewById(R.id.splash_img);
        txt=findViewById(R.id.txt);
        auth=FirebaseAuth.getInstance();
        getSupportActionBar().hide();

        img.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.from_top));
        txt.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.from_bottom));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (auth.getCurrentUser() !=null)
                {
                    Intent intent= new Intent(SplashActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }
}