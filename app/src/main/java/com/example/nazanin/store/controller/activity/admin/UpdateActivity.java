package com.example.nazanin.store.controller.activity.admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UpdateActivity extends AppCompatActivity {

    private EditText name,price,stock,description,idAdmin;
    private ImageView image;
    private Product product;
    private ProductManager productManager;
    private int id;
    private Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;
    private boolean imageUpdated;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        idAdmin=findViewById(R.id.id);
        name=findViewById(R.id.name);
        price=findViewById(R.id.price);
        stock=findViewById(R.id.stock);
        description=findViewById(R.id.description);
        image=findViewById(R.id.productImage);
    }

    public void search(View view) {
        id=Integer.parseInt(idAdmin.getText().toString());
        productManager=new ProductManager(this);
        product=productManager.searchProductById(id);
        showProductInfo();
    }
    private void showProductInfo(){
        image.setImageBitmap(FileManager.getBitmapImage(product.getImage()));
        name.setText(product.getProductName());
        price.setText(String.valueOf(product.getPrice()));
        description.setText(product.getDescription());
        stock.setText(String.valueOf(product.getStock()));
    }

    public void update(View view) {
        productManager=new ProductManager(this);
        product.setId(id);
        if (imageUpdated) {
            product.setImage(FileManager.saveImageToStorage(bitmap, this));
        }
        product.setProductName(name.getText().toString());
        product.setDescription(description.getText().toString());
        product.setPrice(Integer.parseInt(price.getText().toString()));
        product.setStock(Integer.parseInt(stock.getText().toString()));
        productManager.update(product);
        Toast.makeText(this,"کالای مورد نظر ویرایش شد",Toast.LENGTH_SHORT).show();
        reload();
    }
    private void reload(){
        idAdmin.setText("");
        image.setImageBitmap(null);
        name.setText("");
        description.setText("");
        price.setText("");
        stock.setText("");
        image.setImageBitmap(null);
    }

    public void attachPhoto(View view) {
        Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_LOAD_IMG && resultCode==RESULT_OK && null!=data){
            Uri imageSelected=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageSelected);
                image.setImageBitmap(bitmap);
                imageUpdated=true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
        }
    }
}


