package com.example.android.easy_book;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;

import java.util.ArrayList;

public class OrdersListActivity extends AppCompatActivity {

    private ListView listView;
    private OrdersListAdapter listAdapter;
    Button btnPlaceOrder;
    FirebaseFirestore db;
    String CurrentOrder;


    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Product> productOrders = new ArrayList<>();
    ArrayList<String> lOrderItems = new ArrayList<>(); // store json object string for each product


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);
        getProduct();


        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
    }

    private void placeOrder()
    {
        productOrders.clear();
        lOrderItems.clear();
        for(int i=0;i<listAdapter.listProducts.size();i++)
        {
            if(listAdapter.listProducts.get(i).CartQuantity > 0)
            {
                Product products = new Product(
                        listAdapter.listProducts.get(i).ProductName
                        ,listAdapter.listProducts.get(i).ProductDesc
                        ,listAdapter.listProducts.get(i).ProductPrice
                        ,listAdapter.listProducts.get(i).ProductImage
                );
                products.CartQuantity = listAdapter.listProducts.get(i).CartQuantity;
                productOrders.add(products);
                lOrderItems.add(products.getJsonObject());
            }
        }
        //  showMessage("Order Item Count : "+productOrders.size());

        JSONArray jsonArray = new JSONArray(lOrderItems);
        // jsonArray.toString();
        openSummary(jsonArray.toString());
    }

    private void placeOrder(String orderItems)
    {
        //send json data to server
    }

    public void openSummary(String orderItems)
    {
        Intent summaryIntent = new Intent(this,SummaryActivity.class);
        summaryIntent.putExtra("orderItems",orderItems);
        startActivity(summaryIntent);
    }

    public void showMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    public void getProduct() {
        db=FirebaseFirestore.getInstance();

        db.collection("Menu").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for(final DocumentSnapshot snapshot : queryDocumentSnapshots){
                    Product Item1=new Product(snapshot.getString("MenuItem1Name") ,
                            snapshot.getString("MenuItem1Description")
                            ,Double.valueOf(snapshot.getString("MenuItem1Price")) ,snapshot.getString("MenuItem1Image"));
                    Product Item2=new Product(snapshot.getString("MenuItem2Name") ,
                            snapshot.getString("MenuItem2Description")
                            ,Double.valueOf(snapshot.getString("MenuItem2Price")) ,snapshot.getString("MenuItem2Image"));
                    Product Item3=new Product(snapshot.getString("MenuItem3Name") ,
                            snapshot.getString("MenuItem3Description")
                            ,Double.valueOf(snapshot.getString("MenuItem3Price")) ,snapshot.getString("MenuItem3Image"));
                    Product Item4=new Product(snapshot.getString("MenuItem4Name") ,
                            snapshot.getString("MenuItem4Description")
                            ,Double.valueOf(snapshot.getString("MenuItem4Price")) ,snapshot.getString("MenuItem4Image"));


                    Product Dessert1 =new Product(snapshot.getString("Dessert1Name") ,snapshot.getString(
                            "Dessert1Description") ,Double.valueOf(snapshot.getString("Dessert1Price")) ,
                            snapshot.getString("Dessert1Image"));
                    Product Dessert2 =new Product(snapshot.getString("Dessert2Name") ,snapshot.getString(
                            "Dessert2Description") ,Double.valueOf(snapshot.getString("Dessert2Price")) ,
                            snapshot.getString("Dessert2Image"));
                    Product Dessert3 =new Product(snapshot.getString("Dessert3Name") ,snapshot.getString(
                            "Dessert3Description") ,Double.valueOf(snapshot.getString("Dessert3Price")) ,
                            snapshot.getString("Dessert3Image"));

                    Product Drink1=new Product(snapshot.getString("Drink1Name") ,snapshot.getString("Drink1Description")
                    ,Double.valueOf(snapshot.getString("Drink1Price") ),snapshot.getString("Drink1Image"));
                    Product Drink2=new Product(snapshot.getString("Drink2Name") ,snapshot.getString("Drink2Description")
                            ,Double.valueOf(snapshot.getString("Drink2Price") ),snapshot.getString("Drink2Image"));
                    Product Drink3=new Product(snapshot.getString("Drink3Name") ,snapshot.getString("Drink3Description")
                            ,Double.valueOf(snapshot.getString("Drink3Price") ),snapshot.getString("Drink3Image"));

                    CurrentOrder=snapshot.getId();
                    Intent intent=new Intent(OrdersListActivity.this,SummaryActivity.class);
                    intent.putExtra("CurrentOrder" ,CurrentOrder);






                    products.add(Item1);
                    products.add(Item2);
                    products.add(Item3);
                    products.add(Item4);

                    products.add(Dessert1);
                    products.add(Dessert2);
                    products.add(Dessert3);

                    products.add(Drink1);
                    products.add(Drink2);
                    products.add(Drink3);
                    listView = (ListView) findViewById(R.id.customListView);


                    listAdapter = new OrdersListAdapter(OrdersListActivity.this,products);
                    listAdapter.notifyDataSetChanged();
                    listView.setAdapter(listAdapter);


                }
            }
        });


    }
}