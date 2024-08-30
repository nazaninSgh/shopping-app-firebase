package com.example.nazanin.storefirebase.controller.activity.admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

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
        orderManager.getAllOrders(new OnSuccessListener<ArrayList<Order>>() {
            @Override
            public void onSuccess(ArrayList<Order> allorders) {
                orders = allorders;
                recyclerView = findViewById(R.id.ordersrecyclerview);
                OrdersAdapter adapter=new OrdersAdapter(orders,ViewOrdersActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(ViewOrdersActivity.this);
            }
        });

    }

    @Override
    public void onSendClick(int position) {
        orders.get(position).setConfirm_status(true);
        orders.get(position).setSent_status(true);
        orderManager.updateStatus(orders.get(position));

    }

    @Override
    public void onCancelClick(final int position) {
        orders.get(position).setConfirm_status(true);
        orders.get(position).setSent_status(false);
        final CustomerManager customerManager=new CustomerManager(ViewOrdersActivity.this);
        shoppingCartManager.giveShoppingCart(orders.get(position).getShoppingCartId(), new OnSuccessListener<ShoppingCart>() {
            @Override
            public void onSuccess(final ShoppingCart shoppingCart) {

                customerManager.searchCustomerById(shoppingCart.getCustomer_id(), new OnSuccessListener<Customer>() {
                    @Override
                    public void onSuccess(final Customer customer) {
                        customer.setCredit(shoppingCart.getTotalPrice()+customer.getCredit());
                        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
                            @Override
                            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                customerManager.updateCustomersCredit(transaction, customer);

                                orderManager.updateStatus(orders.get(position));

                                ProductManager productManager=new ProductManager(ViewOrdersActivity.this);
                                productManager.updateStock(transaction,shoppingCart, false);
                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Handle success
                                Toast.makeText(ViewOrdersActivity.this, "Order canceled successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                                Toast.makeText(ViewOrdersActivity.this, "Error canceling order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

    }
}
