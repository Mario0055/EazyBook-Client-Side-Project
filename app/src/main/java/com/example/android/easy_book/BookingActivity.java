package com.example.android.easy_book;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BookingActivity extends AppCompatActivity implements View.OnClickListener {
    EditText selectDate, selectTime, selectGuest, selectRequest;
    Button btn_book;
    DatePickerDialog picker;
    AwesomeValidation awesomeValidation;
    TextView BookingRestaurantName;
    TextView BookingRestaurantDetails;
    TextView BookingRestaurantPhoneNummber;
    TextView BookingRestaurantLocation;
    ImageView BookingRestaurantCoverImage;
    EditText InputReviewEditText;
    TextView Review1;
    TextView Review2;
    Button SendReviewButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ImageView img1;
    ImageView img2;
    ImageView img3;
    TextView ReviewText;
    TextView RestaurantReview;
    EditText GuestName;
    EditText GuestPhoneNumber;
    FirebaseFirestore db;
    public static ArrayList<MenuImages> RestaurantMenuImages = new ArrayList<>();
    String CurrentRestaurantID;
    String useless ="";

     String CurrentOrder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        selectDate = (EditText) findViewById(R.id.date);
        selectGuest = (EditText) findViewById(R.id.guest_no);
        selectRequest = (EditText) findViewById(R.id.special_req);
        selectTime = (EditText) findViewById(R.id.time);
        btn_book = (Button) findViewById(R.id.btn_book);
        BookingRestaurantName = findViewById(R.id.BookingRestaurantname);
        BookingRestaurantDetails = findViewById(R.id.BookingRestaurantDetails);
        BookingRestaurantPhoneNummber = findViewById(R.id.BookingRestaurantPhoneNumber);
        BookingRestaurantLocation = findViewById(R.id.BookingRestaurantLocation);
        BookingRestaurantCoverImage=findViewById(R.id.BookingRestaurantCoverImage);
        Review1=findViewById(R.id.RestaurantReview1);
        Review2=findViewById(R.id.RestaurantReview2);
        ReviewText=findViewById(R.id.ReviewText);
        RestaurantReview=findViewById(R.id.RestaurantReview);
        GuestName=findViewById(R.id.BookingActivityGuestName);
        GuestPhoneNumber=findViewById(R.id.BookingActivityGuestPhoneNumber);

        InputReviewEditText=findViewById(R.id.ReviewInputEditText);
        SendReviewButton=findViewById(R.id.BookingRestaurantSendReviewButton);
        img1 = findViewById(R.id.menu1_btn);
        img2 = findViewById(R.id.menu2_btn);
        img3 = findViewById(R.id.menu3_btn);





        final Intent intent = getIntent();
        final String RestaurantID = intent.getStringExtra("RestaurantID");
        db = FirebaseFirestore.getInstance();
        db.collection("Restaurants").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (final DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    if (snapshot.getId().toString().equals(RestaurantID)) {
                        Review1.setText(snapshot.getString("Review1"));
                        Review2.setText(snapshot.getString("Review2"));
                        BookingRestaurantName.setText(snapshot.getString("RestaurantName"));
                        String Details = snapshot.getString("Category1") + " , " + snapshot.getString("Category2") + " , " + snapshot.getString("Category3");
                        BookingRestaurantDetails.setText(Details);
                        BookingRestaurantPhoneNummber.setText(snapshot.getString("PhoneNumber"));
                        BookingRestaurantLocation.setText(snapshot.getString("Location"));
                        RestaurantMenuImages.clear();
                        final MenuImages menuImages = new MenuImages(snapshot.getString("MenuImage1"), snapshot.getString("MenuImage2"),
                                snapshot.getString("MenuImage3"));
                        String Image=snapshot.getString("RestaurantCoverPage");
                        Picasso.with(BookingActivity.this).load(Image).into(BookingRestaurantCoverImage);

                        RestaurantMenuImages.add(menuImages);
                        Picasso.with(BookingActivity.this).load(menuImages.getImageMenu1()).into(img1);
                        Picasso.with(BookingActivity.this).load(menuImages.getImageMenu2()).into(img2);
                        Picasso.with(BookingActivity.this).load(menuImages.getImageMenu3()).into(img3);
                        img1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(BookingActivity.this);
                                View mView = getLayoutInflater().inflate(R.layout.full_image_view_layout, null);
                                PhotoView photoView = mView.findViewById(R.id.imageFullView1);
                                mBuilder.setView(mView);
                                AlertDialog mDialog = mBuilder.create();
                                Picasso.with(BookingActivity.this).load(menuImages.getImageMenu1()).into(photoView);
                                mDialog.show();


                            }
                        });
                        img2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(BookingActivity.this);
                                View mView = getLayoutInflater().inflate(R.layout.full_image_view_layout, null);
                                PhotoView photoView = mView.findViewById(R.id.imageFullView1);
                                mBuilder.setView(mView);
                                AlertDialog mDialog = mBuilder.create();
                                Picasso.with(BookingActivity.this).load(menuImages.getImageMenu2()).into(photoView);
                                mDialog.show();
                            }
                        });
                        img3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(BookingActivity.this);
                                View mView = getLayoutInflater().inflate(R.layout.full_image_view_layout, null);
                                PhotoView photoView = mView.findViewById(R.id.imageFullView1);
                                mBuilder.setView(mView);
                                AlertDialog mDialog = mBuilder.create();
                                Picasso.with(BookingActivity.this).load(menuImages.getImageMenu3()).into(photoView);
                                mDialog.show();
                            }
                        });
                        SendReviewButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!InputReviewEditText.getText().toString().equals("")){
                                    add_field("Review" ,InputReviewEditText.getText().toString().trim(),"Restaurants");
                                    ReviewText.setVisibility(View.VISIBLE);
                                    RestaurantReview.setText(snapshot.getString("Review"));
                                    RestaurantReview.setVisibility(View.VISIBLE);

                                }else{
                                    InputReviewEditText.setHint("Please Write Your Review Here!");

                                }
                            }
                        });







                    }

                }
            }
        });



        selectDate.setOnClickListener(this);
        selectTime.setOnClickListener(this);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.guest_no, RegexTemplate.NOT_EMPTY, R.string.empty_number);
        awesomeValidation.addValidation(this, R.id.date, RegexTemplate.NOT_EMPTY, R.string.empty_date);
        awesomeValidation.addValidation(this, R.id.time, RegexTemplate.NOT_EMPTY, R.string.empty_time);
        awesomeValidation.addValidation(this, R.id.special_req, RegexTemplate.NOT_EMPTY, R.string.empty_request);
        awesomeValidation.addValidation(this, R.id.BookingActivityGuestName, RegexTemplate.NOT_EMPTY, R.string.empty_name);
        awesomeValidation.addValidation(this, R.id.BookingActivityGuestPhoneNumber, RegexTemplate.NOT_EMPTY, R.string.empty_PhoneNumber);
        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()) {
                    String str1 = selectGuest.getText().toString();
                    String str2 = selectDate.getText().toString();
                    String str3 =String.valueOf(selectTime.getText().toString().trim());
                    String str4 = selectRequest.getText().toString();
                    String str5= GuestName.getText().toString().trim();
                    String str6= GuestPhoneNumber.getText().toString().trim();


                    final Map<String , Object> RestaurantReservation=new HashMap<>();
                    RestaurantReservation.put("GuestNumber", str1);
                    RestaurantReservation.put("Date", str2);
                    RestaurantReservation.put("GuestBookingTime ", str3);
                    RestaurantReservation.put("SpecialRequest", str4);
                    RestaurantReservation.put("GuestName", str5);
                    RestaurantReservation.put("GuestPhoneNumber", str6);
                    RestaurantReservation.put("Order", useless);

                    db.collection("Reservation").add(RestaurantReservation).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Reservation Successful","DocumentSnap Shot Add with ID :" +documentReference.getId());

                            CurrentOrder=documentReference.getId();
                            Log.e("BookingAcctivity-ID1",CurrentOrder);
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("CurrentOrder",CurrentOrder);
                            editor.commit();


                            Toast.makeText(BookingActivity.this, "Reservation Successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Oh Shit --->","Error Adding Document" , e);
                        }
                    });

                    Intent intent = new Intent(getApplicationContext(), ConfirmBookingActivity.class);
                    intent.putExtra("message1", str1);
                    intent.putExtra("message2", str2);
                    intent.putExtra("message3", str3);
                    intent.putExtra("message4", str4);

                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "ValidationFaild", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        if (v == selectDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            selectDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == selectTime) {

            // Get Current Time
           final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
           mMinute = c.get(Calendar.MINUTE);

           // Launch Time Picker Dialog
           TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                   new TimePickerDialog.OnTimeSetListener() {

                       @Override
                       public void onTimeSet(TimePicker view, int hourOfDay,
                                             int minute) {

                          selectTime.setText(hourOfDay + ":" + minute);
                       }
                   }, mHour, mMinute, false);
           timePickerDialog.show();
       }
    }


    public void SendImage(View view) {
        Intent intent = new Intent(BookingActivity.this, PanoramaActivity.class);
        intent.putExtra("resId", R.drawable.restaurant);
        startActivity(intent);
    }

    public void add_field (final String key, final Object value, final String collection_ref) {
        FirebaseFirestore.getInstance().collection(collection_ref).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            WriteBatch batch = db.batch();

                            for (DocumentSnapshot document : task.getResult()) {
                                DocumentReference docRef = document.getReference();
                                Map<String, Object> new_map = new HashMap<>();
                                new_map.put(key, value);
                                batch.update(docRef, new_map);
                            }
                            batch.commit();
                        } else {
                            // ... "Error adding field -> " + task.getException()
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // ... "Failure getting documents -> " + e
                    }
                });
    }


}
