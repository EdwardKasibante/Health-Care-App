package com.example.health_care_app.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.health_care_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
  private EditText email_address;
  private EditText user_password;
  private Button login_btn;
  private FirebaseAuth auth;
  private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_address=findViewById(R.id.email_address);
        user_password=findViewById(R.id.password);
        login_btn=findViewById(R.id.login_btn);
        dialog = new ProgressDialog(LoginActivity.this);
        auth=FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email_address.getText().toString().trim()) || email_address.getText().toString().trim().length() < 5){
                    Snackbar.make(v,"Enter Email Address please",Snackbar.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(user_password.getText().toString().trim()) || user_password.getText().toString().trim().length() < 5){
                    Snackbar.make(v,"Enter Password please",Snackbar.LENGTH_SHORT).show();
                }
                else{
                    dialog.setTitle("Checking account existence......");
                    dialog.setCancelable(true);
                    dialog.show();
                    auth.signInWithEmailAndPassword(email_address.getText().toString().trim(),user_password.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull  Task<AuthResult> task) {

                                    if (task.isSuccessful()){
                                        Intent intent= new Intent(LoginActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        finish();

                                    }
                                    else{
                                        dialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Account not found in the database.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

    }
}