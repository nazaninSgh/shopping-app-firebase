package com.example.nazanin.storefirebase.controller.activity.customer;

import android.content.Context;
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
        shoppingCarts = manager.getShoppingCartList(customer.getId());
        pay=findViewById(R.id.payButton);
        totalPriceTextview=findViewById(R.id.total);
        shoppingcartQuantity=findViewById(R.id.shoppingCartnum);
        recyclerView = findViewById(R.id.shoppingCartRecyclerview);
        quantity=shoppingCarts.size();
        if (quantity>0) {
            shoppingcartQuantity.setText(String.valueOf(quantity));
        }
        ShoppingCartAdapter adapter = new ShoppingCartAdapter(shoppingCarts, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        if (quantity>0){
            pay.setOnClickListener(this);
            pay.setVisibility(View.VISIBLE);
        }

    }

    private void openInactiveDialog(){
        InActiveFragment fragment=new InActiveFragment();
        fragment.show(getSupportFragmentManager(),"inactive");
    }

    private void openLowCreditDialog(){
        CreditDialogFragment fragment=new CreditDialogFragment();
        fragment.show(getSupportFragmentManager(),"lowCredit");
    }
    private int calTotalPayment(){
       return manager.getTotalPayment(customer.getId());
    }


    @Override
    public void updateShoppingDetails(int position) {
        ShoppingCartManager manager=new ShoppingCartManager(this);
        manager.deleteFromShoppingCart(shoppingCarts.get(position).getId());
        shoppingCarts.remove(position);
        totalPrice=calTotalPayment();
        quantity=shoppingCarts.size();
        if (quantity>0) {
            shoppingcartQuantity.setText(String.valueOf(quantity));
        }
        else {
            shoppingcartQuantity.setText("");
        }
        totalPriceTextview.setText(String.valueOf(totalPrice));
    }

    @Override
    public void updateTotalPaymentDetails() {
        totalPrice=calTotalPayment();
        totalPriceTextview.setText(String.valueOf(totalPrice));
    }

    @Override
    public void onClick(View v) {
        if (customer.isActive()) {
            //check credit
            if (customer.getCredit() >= totalPrice) {
                OrderManager orderManager = new OrderManager(this);
                //add to order
                for (ShoppingCart shoppingCartItem : shoppingCarts) {
                    orderManager.addToOrders(shoppingCartItem);
                    ProductViewActivity.orderSaved = true;
                    ShoppingCartManager shoppingCartManager = new ShoppingCartManager(this);
                    shoppingCartItem.setConfirm_status(true);
                    shoppingCartManager.updateStatus(shoppingCartItem);
                    ProductManager productManager = new ProductManager(this);
                    productManager.updateStock(shoppingCartItem);
                }
                CustomerManager manager = new CustomerManager(this);
                customer.setCredit(customer.getCredit() - totalPrice);
                manager.updateCustomerCredit(customer);
                Toast.makeText(this, "سفارش شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                finish();
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
