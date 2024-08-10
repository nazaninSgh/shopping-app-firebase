package com.example.nazanin.storefirebase.controller.activity.admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.controller.FileManager;
import com.example.nazanin.storefirebase.model.DAO.CategoryManager;
import com.example.nazanin.storefirebase.model.DAO.ProductManager;
import com.example.nazanin.storefirebase.model.DTO.Product;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class InsertActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText name,price,stock,description;
    private TextView errorImage;
    private Spinner categoryChoicesSpinner;
    private ImageView image;
    private Product product;
    private ProductManager productManager;
    private CategoryManager categoryManager;
    private String[] categoryChoices={"کتاب","کالای دیجیتال","آرایشی و بهداشتی","ورزشی","خانه و آشپزخانه"};
    private static int RESULT_LOAD_IMG = 1;
    private Uri imageUri;
    private Bitmap bitmap;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        product=new Product();
        categoryChoicesSpinner=findViewById(R.id.choosecategory);
        name=findViewById(R.id.name);
        price=findViewById(R.id.price);
        stock=findViewById(R.id.stock);
        description=findViewById(R.id.description);
        image=findViewById(R.id.productImage);
        errorImage=findViewById(R.id.error);
        final ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,categoryChoices);
        categoryChoicesSpinner.setAdapter(spinnerAdapter);
        categoryChoicesSpinner.setOnItemSelectedListener(this);
    }

    public void insert(View view) {

        if (noFieldIsEmpty()){
            FileManager.saveImageToStorage(imageUri, this, new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String imageUrl) {
                    productManager=new ProductManager(InsertActivity.this);
                    product.setImage(imageUrl);
                    product.setProductName(name.getText().toString());
                    product.setDescription(description.getText().toString());
                    product.setPrice(Integer.parseInt(price.getText().toString()));
                    product.setStock(Integer.parseInt(stock.getText().toString()));
                    productManager.insert(product);
                    Toast.makeText(InsertActivity.this,"product added successfully",Toast.LENGTH_SHORT).show();
                    reload();
                }
            });
        }
    }
    private void reload(){
        name.setText("");
        description.setText("");
        price.setText("");
        stock.setText("");
        image.setImageBitmap(null);
        errorImage.setVisibility(View.INVISIBLE);
    }

    private boolean noFieldIsEmpty(){
        if (image.getDrawable()==null){
            errorImage.setVisibility(View.VISIBLE);
            return false;
        }
        if (TextUtils.isEmpty(name.getText())){
            name.setError("لطفا نام کالا را وارد کنید");
            return false;
        }
        if (TextUtils.isEmpty(description.getText())){
            description.setError("لطفا توضیحات کالا را وارد کنید");
            return false;
        }
        if (TextUtils.isEmpty(price.getText())){
            price.setError("لطفا قیمت کالا را وارد کنید");
            return false;
        }
        if (TextUtils.isEmpty(stock.getText())){
            stock.setError("لطفا موجودی کالا را وارد کنید");
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categoryManager=new CategoryManager(this);
        String categoryName=parent.getItemAtPosition(position).toString();
        product.setCategory_id(String.valueOf(position));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void attachPhoto(View view) {
        //Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_LOAD_IMG && resultCode==RESULT_OK && null!=data){
            imageUri=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        if (requestCode==RESULT_LOAD_IMG && resultCode==RESULT_OK && null!=data){
//            Uri imageSelected=data.getData();
//            try {
//                bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageSelected);
//                image.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        else {
//        }
    }
}
