package com.example.android.easy_book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrdersListAdapter extends BaseAdapter {
    public ArrayList<Product> listProducts;
    private Context context;

    public OrdersListAdapter(Context context,ArrayList<Product> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
    }

    @Override
    public int getCount() {
        return listProducts.size();
    }

    @Override
    public Product getItem(int position) {
        return listProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView
            , ViewGroup parent) {
        View row;
        final OrdersListViewHolder listViewHolder;
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.activity_custom_listview,parent,false);
            listViewHolder = new OrdersListViewHolder();
            listViewHolder.ProductName = row.findViewById(R.id.ProductName);
            listViewHolder.ProductDesc = row.findViewById(R.id.ProductDesc);
            listViewHolder.Product = row.findViewById(R.id.Product);
            listViewHolder.Price = row.findViewById(R.id.Price);
            listViewHolder.btnPlus = row.findViewById(R.id.ib_addnew);
            listViewHolder.edTextQuantity = row.findViewById(R.id.editTextQuantity);
            listViewHolder.btnMinus = row.findViewById(R.id.ib_remove);
            row.setTag(listViewHolder);
        }
        else
        {
            row=convertView;
            listViewHolder= (OrdersListViewHolder) row.getTag();
        }
        final Product products = getItem(position);

        listViewHolder.ProductName.setText(products.ProductName);
        listViewHolder.ProductDesc.setText(products.ProductDesc);
//        listViewHolder.Product.setImageResource(products.ProductImage);

        String image =products.getProductImage();
        Picasso.with(context).load(image).into(listViewHolder.Product);



        listViewHolder.Price.setText(products.ProductPrice+" Egp");
        listViewHolder.edTextQuantity.setText(products.CartQuantity+"");

        listViewHolder.edTextQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        listViewHolder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updateQuantity(position,listViewHolder.edTextQuantity,1);
            }
        });
        //listViewHolder.edTextQuantity.setText("0");

        listViewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuantity(position,listViewHolder.edTextQuantity,-1);

            }
        });


        return row;
    }

    private void updateQuantity(int position, TextView edTextQuantity, int value) {

        Product products = getItem(position);
        if(value > 0)
        {
            products.CartQuantity = products.CartQuantity + 1;
        }
        else
        {
            if(products.CartQuantity > 0)
            {
                products.CartQuantity = products.CartQuantity - 1;
            }

        }
        edTextQuantity.setText(products.CartQuantity+"");
    }




}

