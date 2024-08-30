package com.example.nazanin.storefirebase.controller.activity.customer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.controller.adapter.ShoppingCartAdapter;
import com.example.nazanin.storefirebase.controller.fragment.dialogFragments.CreditDialogFragment;
import com.example.nazanin.storefirebase.controller.fragment.dialogFragments.InActiveFragment;
import com.example.nazanin.storefirebase.model.DAO.CustomerManager;
import com.example.nazanin.storefirebase.model.DAO.OrderManager;
import com.example.nazanin.storefirebase.model.DAO.ProductManager;
import com.example.nazanin.storefirebase.model.DAO.ShoppingCartManager;
import com.example.nazanin.storefirebase.model.DTO.Customer;
import com.example.nazanin.storefirebase.model.DTO.ShoppingCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FinalPaymentActivity extends AppCompatActivity implements ShoppingCartAdapter.IShoppingListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private Button pay;
    private ArrayList<ShoppingCart> shoppingCarts;
    private Customer customer;
    private ShoppingCartManager manager;
    private int totalPrice,quantity;
    public TextView shoppingcartQuantity;
    public TextView totalPriceTextview;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_payment);
        customer=getIntent().getExtras().getParcelable("customer");
        manager=new ShoppingCartManager(this);
        manager.getShoppingCartList(customer.getId(), new OnSuccessListener<ArrayList<ShoppingCart>>() {
            @Override
            public void onSuccess(ArrayList<ShoppingCart> shoppingCarts) {
                quantity=shoppingCarts.size();
                if (quantity>0) {
                    shoppingcartQuantity.setText(String.valueOf(quantity));
                }
                ShoppingCartAdapter adapter = new ShoppingCartAdapter(shoppingCarts, FinalPaymentActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(FinalPaymentActivity.this);
                if (quantity>0){
                    pay.setOnClickListener(FinalPaymentActivity.this);
                    pay.setVisibility(View.VISIBLE);
                }

            }
        });
        pay=findViewById(R.id.payButton);
        totalPriceTextview=findViewById(R.id.total);
        shoppingcartQuantity=findViewById(R.id.shoppingCartnum);
        recyclerView = findViewById(R.id.shoppingCartRecyclerview);

    }

    private void openInactiveDialog(){
        InActiveFragment fragment=new InActiveFragment();
        fragment.show(getSupportFragmentManager(),"inactive");
    }

    private void openLowCreditDialog(){
        CreditDialogFragment fragment=new CreditDialogFragment();
        fragment.show(getSupportFragmentManager(),"lowCredit");
    }
   // private int calTotalPayment(){

   // }


    @Override
    public void updateShoppingDetails(final int position) {
        final ShoppingCartManager manager=new ShoppingCartManager(this);
        manager.deleteFromShoppingCart(shoppingCarts.get(position).getId(), new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                shoppingCarts.remove(position);
                manager.getTotalPayment(customer.getId(), new OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer price) {
                        totalPrice = price;
                        quantity=shoppingCarts.size();
                        if (quantity>0) {
                            shoppingcartQuantity.setText(String.valueOf(quantity));
                        }
                        else {
                            shoppingcartQuantity.setText("");
                        }
                        totalPriceTextview.setText(String.valueOf(totalPrice));
                    }
                });

            }
        });

    }

    @Override
    public void updateTotalPaymentDetails() {
        //totalPrice=calTotalPayment();
        totalPriceTextview.setText(String.valueOf(totalPrice));
    }

    @Override
    public void onClick(View v) {
        if (customer.isActive()) {
            //check credit
            if (customer.getCredit() >= totalPrice) {
                OrderManager orderManager = new OrderManager(this);
                //add to order
                for (final ShoppingCart shoppingCartItem : shoppingCarts) {
                    orderManager.addToOrders(shoppingCartItem);
                    ProductViewActivity.orderSaved = true;

                }
                CustomerManager manager = new CustomerManager(this);
                customer.setCredit(customer.getCredit() - totalPrice);
                manager.updateCustomerCredit(customer, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(FinalPaymentActivity.this, "سفارش شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
            else {
                openLowCreditDialog();
            }
        }
        else {
            openInactiveDialog();
        }
    }
}
