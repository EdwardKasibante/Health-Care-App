package com.example.health_care_app.Screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.health_care_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientDetailsActivity extends AppCompatActivity {
    private CircleImageView imageView;
    private TextView name,born,date;
    private TextView user_data;
    private String key;
    private DatabaseReference reference;
    private EditText diease,conditions, recommendations;
    private Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        imageView = findViewById(R.id.user_image);
        name = findViewById(R.id.show_name);
        user_data = findViewById(R.id.user_data);

        diease = findViewById(R.id.diease);
        conditions = findViewById(R.id.conditions);
        recommendations = findViewById(R.id.recommendations);
        save = findViewById(R.id.save);

        date = findViewById(R.id.date);
        born = findViewById(R.id.dob);
        name.setText(getIntent().getStringExtra("fullname"));
        born.setText(getIntent().getStringExtra("dob"));
        date.setText(getIntent().getStringExtra("date") +" - "+getIntent().getStringExtra("time"));
        user_data.setText(getIntent().getStringExtra("gender") +" - "+getIntent().getStringExtra("phone"));
        Glide.with(PatientDetailsActivity.this)
                .load(getIntent().getStringExtra("image"))
                .into(imageView);
        key=getIntent().getStringExtra("key");

        reference = FirebaseDatabase.getInstance().getReference().child("reports").child(key);

        queryReports();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(diease.getText().toString().trim())){
                    Toast.makeText(PatientDetailsActivity.this, "Specify Disease", Toast.LENGTH_SHORT).show();
                }
               else if (TextUtils.isEmpty(conditions.getText().toString().trim())){
                    Toast.makeText(PatientDetailsActivity.this, "Specify Conditions", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(recommendations.getText().toString().trim())){
                    Toast.makeText(PatientDetailsActivity.this, "Specify Recommendations", Toast.LENGTH_SHORT).show();
            }
            else{
                    reference.child("disease").setValue(diease.getText().toString());
                    reference.child("description").setValue(conditions.getText().toString());
                    reference.child("patientId").setValue(key);
                    reference.child("recommendation").setValue(recommendations.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(PatientDetailsActivity.this, "Patient Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PatientDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                }
            }
        });

    }

    private void queryReports() {
        reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull  DataSnapshot snapshot) {
              if (snapshot.exists()){
                  Toast.makeText(PatientDetailsActivity.this, "Well", Toast.LENGTH_SHORT).show();
                  if((int)snapshot.getChildrenCount() > 0){
                      recommendations.setText(snapshot.child("recommendation").getValue(String.class));
                      diease.setText(snapshot.child("disease").getValue(String.class));
                      conditions.setText(snapshot.child("description").getValue(String.class));
                  }
                  else{
                      Toast.makeText(PatientDetailsActivity.this, "No Medical Result Found", Toast.LENGTH_SHORT).show();
                  }
              }
              else{
                  Toast.makeText(PatientDetailsActivity.this, "No", Toast.LENGTH_SHORT).show();
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
    }

}