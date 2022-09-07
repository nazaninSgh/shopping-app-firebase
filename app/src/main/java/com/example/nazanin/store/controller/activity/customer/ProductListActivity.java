package com.example.nazanin.store.controller.activity.customer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nazanin.store.R;
import com.example.nazanin.store.controller.FileManager;
import com.example.nazanin.store.controller.fragment.viewPagerFragments.ProductViewpagerFragment;
import com.example.nazanin.store.model.DAO.ProductManager;
import com.example.nazanin.store.model.DTO.Customer;
import com.example.nazanin.store.model.DTO.Product;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView productList;
    private ArrayList<Product> products;
    private int category_id;
    private Button desc,asc;
    private ProductManager manager;
    private Customer customer;
    private CustomListview customListview;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        category_id=getIntent().getIntExtra("category_id",0);
        desc=findViewById(R.id.desc);
        asc=findViewById(R.id.asc);
        productList=findViewById(R.id.productListview);
        desc.setOnClickListener(this);
        asc.setOnClickListener(this);
        manager=new ProductManager(this);
        products=manager.getProductsInfo(category_id);
        customListview=new CustomListview();
        productList.setAdapter(customListview);
        if (getIntent().hasExtra("customer")) {
            customer = getIntent().getExtras().getParcelable("customer");
            productList.setOnItemClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        products.clear();
        switch (v.getId()){
            case R.id.desc:
                products=manager.sortDesc(category_id);
                break;
            case R.id.asc:
                products=manager.sortAsc(category_id);
                break;
        }
        customListview.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this, ShoppingActivity.class);
        intent.putExtra("selectedProduct",products.get(position));
        intent.putExtra("customer",customer);
        startActivity(intent);
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
            if (products.get(position).getStock()<=0){

                exist.setVisibility(View.VISIBLE);
            }
            return view;
        }
    }
}
