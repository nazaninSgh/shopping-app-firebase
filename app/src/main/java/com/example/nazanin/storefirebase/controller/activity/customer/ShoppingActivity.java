package com.example.nazanin.storefirebase.controller.activity.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.controller.FileManager;
import com.example.nazanin.storefirebase.controller.fragment.dialogFragments.InActiveFragment;
import com.example.nazanin.storefirebase.model.DAO.ShoppingCartManager;
import com.example.nazanin.storefirebase.model.DTO.Customer;
import com.example.nazanin.storefirebase.model.DTO.Product;
import com.example.nazanin.storefirebase.model.DTO.ShoppingCart;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ShoppingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView productName,description,price;
    private ImageView productImage;
    private Button addToShoppingCart;
    private Product product;
    private Customer customer;
    private ShoppingCart shoppingCart;
    private android.support.v7.widget.Toolbar toolbar;
    private LinearLayout infolayout;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping);
        product=getIntent().getExtras().getParcelable("selectedProduct");
        customer=getIntent().getExtras().getParcelable("customer");
        //find
        toolbar=findViewById(R.id.home_toolbar);
        productImage=findViewById(R.id.productImage);
        productName=findViewById(R.id.name);
        price=findViewById(R.id.price);
        description=findViewById(R.id.description);
        infolayout=findViewById(R.id.infoLayout);
        addToShoppingCart=findViewById(R.id.addToShoppingCart);
        if (product.getStock()>0){
            addToShoppingCart.setOnClickListener(this);
            addToShoppingCart.setText("افزودن به سبد خرید");
            addToShoppingCart.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_add_shopping_cart_black_24dp,0);
        }
        else {
            addToShoppingCart.setText("ناموجود");
            addToShoppingCart.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_clear_black_24dp,0);
        }
        showProductInfo();

    }

    private void showProductInfo(){
       productImage.setImageBitmap(FileManager.getBitmapImage(product.getImage()));
       productName.setText(product.getProductName());
       description.setText(product.getDescription());
       price.setText(String.valueOf(product.getPrice()));
    }

    private void openInactiveDialog(){
        InActiveFragment fragment=new InActiveFragment();
        fragment.show(getSupportFragmentManager(),"inactive");
    }

    @Override
    public void onClick(View v) {
        if (customer.isActive()) {
            ShoppingCartManager manager = new ShoppingCartManager(ShoppingActivity.this);
            shoppingCart = new ShoppingCart();
            shoppingCart.setCustomer_id(customer.getId());
            shoppingCart.setProduct_id(product.getId());
            shoppingCart.setQuantity(1);
            shoppingCart.setTotalPrice(product.getPrice());
            manager.addToShoppingCart(shoppingCart);
            Intent intent = new Intent(this, FinalPaymentActivity.class);
            intent.putExtra("product", product);
            intent.putExtra("customer", customer);
            startActivity(intent);
            finish();
        }
        else {
            openInactiveDialog();
        }
    }
}
