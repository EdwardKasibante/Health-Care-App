package com.example.health_care_app.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.health_care_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private TextView user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth= FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        user_email=findViewById(R.id.user_email);
        user_email.setText(firebaseUser.getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId= item.getItemId();
        if (itemId == R.id.action_logout){
            AlertDialog.Builder builder= new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Logout Confirmation");
            builder.setMessage("Are you sure you want to logout");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent= new Intent(HomeActivity.this,SplashActivity.class);
                    startActivity(intent);
                    finish();
                    auth.signOut();
                    Toast.makeText(HomeActivity.this, "You have been logged out", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }
        else if (itemId == R.id.action_help){

        }
        else if (itemId == R.id.action_share){

        }
        else{

        }
            return super.onOptionsItemSelected(item);
    }

    public void openAll(View view) {
        startActivity(new Intent(HomeActivity.this, ViewPatientActivity.class));
    }

    public void openAdd(View view) {
        startActivity(new Intent(HomeActivity.this, AddNewPatientActivity.class));
    }
}