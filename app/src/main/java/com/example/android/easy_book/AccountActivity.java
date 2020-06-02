package com.example.android.easy_book;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class AccountActivity extends AppCompatActivity {
    FirebaseAuth user;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView Accountname;
    TextView Accountgmail;
    TextView TelephoneNumber;
    String name;
    String email;
    String phone;
    FirebaseFirestore db;


    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Accountname=findViewById(R.id.AccountName);
        Accountgmail=findViewById(R.id.gmailtext);
        TelephoneNumber=findViewById(R.id.call);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db=FirebaseFirestore.getInstance();
        if(user!=null){
            db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    for(final DocumentSnapshot snapshot : queryDocumentSnapshots){
                        Accountname.setText(snapshot.getString("Name"));
                        Accountgmail.setText(snapshot.getString("Email"));
                        TelephoneNumber.setText(snapshot.getString("TelephoneNumber"));



                    }
                }
            });

        }else{
            Accountname.setText("common you didn't sign up ");

        }




    }
}
