package com.example.android.easy_book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.okhttp.internal.DiskLruCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {
    ListView lvSummary;
    TextView orderTotal;
    Double Total=0d;
    String Order="";
    double price;
    String receipt="";
    String quantity, name2;
    Button btn_order;
    FirebaseFirestore db;
    String SendingOrderToAdmin;
    String CurrentOrder;

    ArrayList<Product> productOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        db=FirebaseFirestore.getInstance();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        CurrentOrder=pref.getString("CurrentOrder","null");
        Log.e("SharedPreference :" ,CurrentOrder);



        btn_order = (Button) findViewById(R.id.btn_order);
        lvSummary = findViewById(R.id.lvSummary);
        orderTotal = findViewById(R.id.oneString);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> currentorder = new HashMap<>();
                currentorder.put("Order",SendingOrderToAdmin);
               db.collection("Reservation").document(CurrentOrder).set(currentorder, SetOptions.merge());

                Toast.makeText(getApplicationContext(), "Booked & Ordered Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SummaryActivity.this,MainActivity.class);

                startActivity(intent);

            }
        });

        getOrderItemData();

    }
    @SuppressLint("SetTextI18n")
    private void getOrderItemData() {
        Bundle extras = getIntent().getExtras();
        if(extras !=null )
        {
            String orderItems = extras.getString("orderItems",null);
            if(orderItems!=null && orderItems.length()>0)
            {
                try{
                    JSONArray jsonOrderItems = new JSONArray(orderItems);
                    for(int i=0;i<jsonOrderItems.length();i++)
                    {
                        JSONObject jsonObject = new JSONObject(jsonOrderItems.getString(i));
                        Product product = new Product(
                                jsonObject.getString("ProductName")
                                ,jsonObject.getString("ProductDesc")
                                ,jsonObject.getDouble("ProductPrice")
                                ,jsonObject.getString("ProductImage")
                        );
                        product.CartQuantity = jsonObject.getInt("CartQuantity");

                        /* Calculate Total */
                        Total = Total + (product.CartQuantity * product.ProductPrice);
                        receipt = receipt + product.CartQuantity + " " + product.ProductName + "\n";
                        productOrders.add(product);
                    }

                    if(productOrders.size() > 0)
                    {
                        OrdersListAdapter listAdapter = new OrdersListAdapter(this,productOrders);
                        lvSummary.setAdapter(listAdapter);
                        orderTotal.setText("Receipt: "+ "\n" +receipt + "\n"+  "Total: "+ Total);
                        SendingOrderToAdmin=orderTotal.getText().toString();
                        orderTotal.setMovementMethod(new ScrollingMovementMethod());

                    }
                    else
                    {
                        showMessage("Empty");
                    }
                }
                catch (Exception e)
                {
                    showMessage(e.toString());
                }
            }

        }
    }
    public void showMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
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
