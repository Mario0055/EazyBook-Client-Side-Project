package com.example.android.easy_book;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmBookingActivity extends AppCompatActivity {
    public final static String MESSAGE_KEY = "ganeshannt.senddata.message_key";
    TextView text1, text2, text3, text4;
    Button btn_confirm;
    AwesomeValidation awesomeValidation;
    String CurrentOrder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);



        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    openDialog();
                }
            }
        });

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        Intent intent = getIntent();
        String str1 = intent.getStringExtra("message1");
        text1.setText(str1);
        String str2 = intent.getStringExtra("message2");
        text2.setText(str2);
        String str3 = intent.getStringExtra("message3");
        text3.setText(str3);
        String str4 = intent.getStringExtra("message4");
        text4.setText(str4);



    }



    public void openDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Ordering")
                .setMessage("Do you want to place your order?")
                .setPositiveButton("Yes", null)
                .setNegativeButton("No",null)
                .show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrdersListActivity.class);
                startActivity(intent);
            }
        });
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Booked Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
