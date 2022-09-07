package com.example.nazanin.store.controller.activity.admin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nazanin.store.R;
import com.example.nazanin.store.controller.FileManager;
import com.example.nazanin.store.model.DAO.ProductManager;
import com.example.nazanin.store.model.DTO.Product;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductSearchActivity extends AppCompatActivity {

    private TextView name,price,stock,description;
    private ImageView image;
    private EditText idAdmin;
    private int id;
    private Product product;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        idAdmin=findViewById(R.id.id);
        name=findViewById(R.id.name);
        price=findViewById(R.id.price);
        stock=findViewById(R.id.stock);
        description=findViewById(R.id.description);
        image=findViewById(R.id.productImage);
    }

    public void search(View view) {
        id=Integer.parseInt(idAdmin.getText().toString());
        ProductManager productManager = new ProductManager(this);
        product=productManager.searchProductById(id);
        if (product!=null) {
            showProductInfo();
        }
        else {
            Toast.makeText(this,"کالای مورد نظر یافت نشد",Toast.LENGTH_SHORT).show();
        }
    }
    private void showProductInfo(){
        image.setImageBitmap(FileManager.getBitmapImage(product.getImage()));
        name.setText(product.getProductName());
        price.setText(String.valueOf(product.getPrice()));
        description.setText(product.getDescription());
        stock.setText(String.valueOf(product.getStock()));
    }
}
