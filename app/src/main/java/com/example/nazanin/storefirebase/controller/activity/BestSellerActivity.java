package com.example.nazanin.storefirebase.controller.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.controller.FileManager;
import com.example.nazanin.storefirebase.model.DAO.ProductManager;
import com.example.nazanin.storefirebase.model.DTO.Product;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BestSellerActivity extends AppCompatActivity {

    private ListView productList;
    private ArrayList<Product> products=new ArrayList<>();
    private CustomListview customListview;
    private ProductManager manager;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_seller);
        productList=findViewById(R.id.productListview);
        manager=new ProductManager(this);
        manager.getBestSeller(new OnSuccessListener<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> bestSellerProducts) {
                products = bestSellerProducts;
                customListview=new CustomListview();
                productList.setAdapter(customListview);
            }
        });

    }

    public class CustomListview extends BaseAdapter {

        ImageView imageView;
        TextView name,price,description,exist;

        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=null;
            if (convertView==null) {
                view = getLayoutInflater().inflate(R.layout.product_list_row, null);
            }
            else {
                view=convertView;
            }

            imageView=view.findViewById(R.id.productImage);
            name=view.findViewById(R.id.name);
            description=view.findViewById(R.id.description);
            price=view.findViewById(R.id.price);
            exist=view.findViewById(R.id.exist);
            exist.setVisibility(View.GONE);
            imageView.setImageBitmap(FileManager.getBitmapImage(products.get(position).getImage()));
            name.setText(products.get(position).getProductName());
            description.setText("توضیحات: "+products.get(position).getDescription());
            price.setText("قیمت: "+products.get(position).getPrice()+" تومان ");
            return view;
        }
    }
}
