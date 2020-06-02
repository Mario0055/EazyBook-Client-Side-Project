package com.example.android.easy_book;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.android.easy_book.R.id;
import static com.example.android.easy_book.R.layout;
import static com.example.android.easy_book.R.string;

public class SignupActivity extends AppCompatActivity  {
    Button btnSignup;
    EditText name, password, email, dob, phoneno, confirmpass;
    TextView lnkLogin;
    AwesomeValidation awesomeValidation;
    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    String userID;


    TextView SignUpData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_signup);
        name = (EditText) findViewById(id.txtName);
        password = (EditText) findViewById(id.txtPwd);
        confirmpass = (EditText) findViewById(id.txtPwdConfirm);
        email = (EditText) findViewById(id.txtEmail);
        dob = (EditText) findViewById(id.txtDate);
        phoneno = (EditText) findViewById(id.txtPhone);
        btnSignup = (Button) findViewById(id.btnSignUp);
        lnkLogin= (TextView) findViewById(id.lnkLogin);
        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        /*if(fAuth.getCurrentUser()!= null){
            Intent intent=new Intent(SignupActivity.this,MainActivity.class);
            startActivity(intent);

        }     */

        SignUpData=findViewById(id.SignUpData);

        //Initialize Validation Style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, id.txtName, RegexTemplate.NOT_EMPTY, string.invalid_name);
        awesomeValidation.addValidation(this, id.txtPwd, ".{6,}", string.invalid_password);
        awesomeValidation.addValidation(this, id.txtEmail, Patterns.EMAIL_ADDRESS, string.invalid_email);
        awesomeValidation.addValidation(this, id.txtPhone, "[0]{1}[1]{1}[0-9]{9}$", string.invalid_mobile);
        awesomeValidation.addValidation(this, id.txtDate, "[0-9]{2}[/]{1}[0-9]{2}[/]{1}[0-9]{4}$", string.invalid_date);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    final String emailText=email.getText().toString().trim();
                    final String PwText=password.getText().toString().trim();
                    final String NameText=name.getText().toString().trim();
                    final String DateOfBirth= dob.getText().toString().trim();
                    final String PhoneNumber=phoneno.getText().toString().trim();
                    fAuth.createUserWithEmailAndPassword(emailText,PwText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                
                                FirebaseUser fuser=fAuth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignupActivity.this, "Verfication Email Has Been Sent", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Oh Shit -->", "onFailure: Email not sent " + e.getMessage());
                                    }
                                });
                                Toast.makeText(SignupActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference=fstore.collection("users").document(userID);
                                userID=fAuth.getCurrentUser().getUid();
                                final Map<String , Object> user=new HashMap<>();
                                user.put("Name", NameText);
                                user.put("Email", emailText);
                                user.put("Password ", PwText);
                                user.put("TelephoneNumber", PhoneNumber);
                                user.put("DateOfBirth", DateOfBirth);




                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(ContentValues.TAG, "User Profile is Created for"+userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Oh Shit -- >", "onFailure: " +e.toString()); 
                                    }
                                });
                                     Intent intent = new Intent(getApplicationContext() ,SigninActivity.class);
                                     startActivity(intent);


                            }else{
                                Toast.makeText(SignupActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });




                }



            }

        });
        lnkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignupActivity.this,SigninActivity.class);
                startActivity(intent);
            }

        });


    }


}

//Toast.makeText(getApplicationContext(), "Form Validation Succefully...", Toast.LENGTH_SHORT).show();
