package com.example.nazanin.storefirebase.controller.activity.admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.controller.FileManager;
import com.example.nazanin.storefirebase.model.DAO.ProductManager;
import com.example.nazanin.storefirebase.model.DTO.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductSearchActivity extends AppCompatActivity {

    private TextView name,price,stock,description;
    private ImageView image;
    private EditText idAdmin;
    private String id;
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
        id=idAdmin.getText().toString();
        ProductManager productManager = new ProductManager(this);
        productManager.searchProductById(id).addOnCompleteListener(new OnCompleteListener<Product>() {
            @Override
            public void onComplete(@NonNull Task<Product> task) {
                if (task.isSuccessful()){
                    showProductInfo(task.getResult());
                }
                else {
                    Toast.makeText(ProductSearchActivity.this,"product wasn't found",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void showProductInfo(Product product){
        Picasso.get().load(product.getImage()).into(image);
        //image.setImageBitmap(FileManager.getBitmapImage(product.getImage()));
        name.setText(product.getProductName());
        price.setText(String.valueOf(product.getPrice()));
        description.setText(product.getDescription());
        stock.setText(String.valueOf(product.getStock()));
    }
}
