package com.example.nazanin.storefirebase.controller.activity.admin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.controller.adapter.OrdersAdapter;
import com.example.nazanin.storefirebase.model.DAO.CustomerManager;
import com.example.nazanin.storefirebase.model.DAO.OrderManager;
import com.example.nazanin.storefirebase.model.DAO.ProductManager;
import com.example.nazanin.storefirebase.model.DAO.ShoppingCartManager;
import com.example.nazanin.storefirebase.model.DTO.Customer;
import com.example.nazanin.storefirebase.model.DTO.Order;
import com.example.nazanin.storefirebase.model.DTO.ShoppingCart;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewOrdersActivity extends AppCompatActivity implements OrdersAdapter.IOnclickListener {

    private RecyclerView recyclerView;
    private ArrayList<Order> orders;
    private OrderManager orderManager;
    private ShoppingCartManager shoppingCartManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        orderManager=new OrderManager(this);
        shoppingCartManager=new ShoppingCartManager(this);
        orders=orderManager.getAllOrders();
        recyclerView = findViewById(R.id.ordersrecyclerview);
        OrdersAdapter adapter=new OrdersAdapter(orders,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onSendClick(int position) {
        orders.get(position).setConfirm_status(true);
        orders.get(position).setSent_status(true);
        orderManager.updateStatus(orders.get(position));
        shoppingCartManager.giveShoppingCart(orders.get(position).getShoppingCartId(), new OnSuccessListener<ShoppingCart>() {
            @Override
            public void onSuccess(ShoppingCart shoppingCart) {
                shoppingCart.setSent_status(true);
                shoppingCartManager.updateStatus(shoppingCart, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(ViewOrdersActivity.this,"order status successfully updated",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onCancelClick(final int position) {
        orders.get(position).setConfirm_status(true);
        orders.get(position).setSent_status(false);
        shoppingCartManager.giveShoppingCart(orders.get(position).getShoppingCartId(), new OnSuccessListener<ShoppingCart>() {
            @Override
            public void onSuccess(final ShoppingCart shoppingCart) {
                final CustomerManager customerManager=new CustomerManager(ViewOrdersActivity.this);
                customerManager.searchCustomerById(shoppingCart.getCustomer_id(), new OnSuccessListener<Customer>() {
                    @Override
                    public void onSuccess(Customer customer) {
                        customer.setCredit(shoppingCart.getTotalPrice()+customer.getCredit());
                        customerManager.updateCustomerCredit(customer, new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                ProductManager productManager=new ProductManager(ViewOrdersActivity.this);
                                productManager.updateStock(shoppingCart, false);
                                orderManager.updateStatus(orders.get(position));
                            }
                        });

                    }
                });
            }
        });


    }
}
