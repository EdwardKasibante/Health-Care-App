package com.example.health_care_app.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.health_care_app.Adapters.UserAdapter;
import com.example.health_care_app.R;
import com.example.health_care_app.UserList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPatientActivity extends AppCompatActivity {
    java.util.List<UserList> List;
    private UserAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);
        dialog=new ProgressDialog(ViewPatientActivity.this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Patients");
        List = new ArrayList<>();
        adapter = new UserAdapter(ViewPatientActivity.this, List);
        recyclerView =findViewById(R.id.info_recycle);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ViewPatientActivity.this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        fetch_data();
    }
    private void fetch_data() {
        dialog.show();
        dialog.setMessage("Fetching patient Information");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                for (DataSnapshot snapshot: ds.getChildren()){
                    List.add(new UserList(
                            snapshot.child("first_name").getValue(String.class),
                            snapshot.child("last_name").getValue(String.class),
                            snapshot.child("gender").getValue(String.class),
                            snapshot.child("phone").getValue(String.class),
                            snapshot.child("TimeAdded").getValue(String.class),
                            snapshot.child("image").getValue(String.class),
                            snapshot.child("DateAdded").getValue(String.class),
                            snapshot.child("address").getValue(String.class),
                            snapshot.child("dob").getValue(String.class),
                            snapshot.getKey(),
                            snapshot.child("addedby").getValue(String.class)
                    ));
                }
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(ViewPatientActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}