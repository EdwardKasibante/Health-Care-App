package com.example.health_care_app.Screens;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.health_care_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddNewPatientActivity extends AppCompatActivity {
    private EditText firstName,LastName, address,userPhone;
    private TextView save_info;
    private TextView finish,birthDate;
    private Spinner spinner,genderSpinner;
    private  String dateOfBirth;
    private ArrayList<String> district,gender;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String dt;
    private static final int MY_CAMERA_PERMISSION_CODE = 12;
    String verification_id;
    private CircleImageView pickImage;
    private Bitmap bitmap;
    private CircleImageView imageView;
   private ProgressDialog progressDialog ;
   private Uri imageUri;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog dialog;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_patient);
        birthDate=findViewById(R.id.dob);
        genderSpinner=findViewById(R.id.genderSpinner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gender=new ArrayList<>();
        gender.add("Select Gender");
        gender.add("Male");
        gender.add("Female");

        auth= FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        genderSpinner.setAdapter(new ArrayAdapter<>(AddNewPatientActivity.this,
                android.R.layout.simple_spinner_dropdown_item,gender
        ));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Patients");
        storageReference = FirebaseStorage.getInstance().getReference().child("PatientsImages");
        dialog = new ProgressDialog(AddNewPatientActivity.this);


        firstName=findViewById(R.id.firstName);
        LastName=findViewById(R.id.otherName);
        userPhone=findViewById(R.id.userPhone);
        address=findViewById(R.id.address);
        pickImage=findViewById(R.id.pickImage);
        imageView =findViewById(R.id.userImage);
        save_info=findViewById(R.id.save_info);

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddNewPatientActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
//                Toast.makeText(Profile.this, "DOB", Toast.LENGTH_SHORT).show();
            }
        });

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ActivityCompat.checkSelfPermission(AddNewPatientActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        }
                    }
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(AddNewPatientActivity.this);
                }catch (Exception e){
                    Toast.makeText(AddNewPatientActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("tag", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                int currentYear=Calendar.getInstance().get(Calendar.YEAR);
                if (year > currentYear){
                    Toast.makeText(AddNewPatientActivity.this, "Invalid date of birth selected", Toast.LENGTH_SHORT).show();
                    birthDate.setError("Invalid Date");
                }
                else {
                    birthDate.setError(null);
                    String date = year + "-" + month + "-" + day;
                    birthDate.setText(date);
                    dateOfBirth= year+"-"+month+"-"+day;
                }
            }
        };

        save_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String gender = (String) genderSpinner.getSelectedItem();
                if (imageUri == null){
                    Snackbar.make(v,"Image Not Provided",Snackbar.LENGTH_LONG).show();
                }
                else if (firstName.getText().toString().length()<2  ||  firstName.getText().toString().isEmpty()){
                    firstName.setError("First Name is Incomplete");
                }
                else if (LastName.getText().toString().length()<2  ||  LastName.getText().toString().isEmpty()){
                    LastName.setError("Last Name is Incomplete");
                }

                else if (address.getText().toString().length()<2  ||  LastName.getText().toString().isEmpty()){
                    address.setError("Enter Your Address");
                }
                else if (userPhone.getText().toString().length()<9  ||  userPhone.getText().toString().isEmpty()){
                    address.setError("Phone Number is not valid");
                }
                else if (birthDate.getText().toString().isEmpty()){
                    birthDate.setError("Select Date of Birth");
                    birthDate.requestFocus();
                }
                else  if (gender =="Select Gender" || gender.isEmpty() || gender==null){
                    Toast.makeText(AddNewPatientActivity.this, "Select Gender", Toast.LENGTH_SHORT).show();
                }
                else {

                    save_patient_now(gender);


                }

            }
        });

    }

    private void save_patient_now(String gender) {
        dialog.show();
        dialog.setMessage("Saving Profile Imag....");
        final  StorageReference imageRef = storageReference.child(""+imageUri.getLastPathSegment());

        imageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<UploadTask.TaskSnapshot> task) {
                dialog.setMessage("Getting the download Uri");
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Calendar calendar = Calendar.getInstance();
                        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                        Date date=new Date();
                        calendar.setTime(date);
                        int hour=calendar.get(Calendar.HOUR_OF_DAY);
                        int minute=calendar.get(Calendar.MINUTE);
                        int second=calendar.get(Calendar.SECOND);
                        String time=String.valueOf(hour+":"+minute+":"+second);
                        DatabaseReference finalRef=databaseReference.push();
                        finalRef.child("first_name").setValue(firstName.getText().toString().trim());
                        finalRef.child("last_name").setValue(LastName.getText().toString().trim());
                        finalRef.child("address").setValue(address.getText().toString().trim());
                        finalRef.child("image").setValue(String.valueOf(uri));
                        finalRef.child("gender").setValue(gender);
                        finalRef.child("TimeAdded").setValue(time);
                        finalRef.child("addedby").setValue(String.valueOf(firebaseUser.getEmail()));
                        finalRef.child("DateAdded").setValue(currentDate);
                        finalRef.child("dob").setValue(birthDate.getText().toString());
                        finalRef.child("phone").setValue(userPhone.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                Toast.makeText(AddNewPatientActivity.this, "User Patient Created Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddNewPatientActivity.this,HomeActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNewPatientActivity.this,"Error "+e.getMessage(), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(AddNewPatientActivity.this,"Error "+e.getMessage(), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewPatientActivity.this,"Error "+e.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                int progress= (int)((snapshot.getBytesTransferred()/snapshot.getTotalByteCount())*100);
                dialog.setMessage("Uploading Patient Image at "+progress+"%");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK ) {
                Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);
                imageUri = resultUri;
            }

        }
    }


}

