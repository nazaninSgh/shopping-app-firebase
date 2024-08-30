package com.example.nazanin.storefirebase.model.DAO;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.nazanin.storefirebase.controller.activity.customer.FinalPaymentActivity;
import com.example.nazanin.storefirebase.model.DTO.Order;
import com.example.nazanin.storefirebase.model.DTO.Product;
import com.example.nazanin.storefirebase.model.DTO.ShoppingCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderManager {
    private Context context;
    private FirebaseFirestore firestore;
    private ArrayList<Order> orders=new ArrayList<>();
    private SimpleDateFormat dateFormat;

    public OrderManager(Context context){
        this.context=context;
        firestore = FirebaseFirestore.getInstance();
        dateFormat=new SimpleDateFormat("yyyy-MM-dd");
    }

    private Order createOrder(ShoppingCart shoppingCart,DocumentReference reference){
        Order order = new Order();
        order.setShoppingCartId(shoppingCart.getId());
        order.setConfirm_status(shoppingCart.isConfirm_status());
        order.setSent_status(shoppingCart.isSent_status());
        order.setDate(dateFormat.format(new Date()));
        order.setId(reference.getId());
        return order;
    }
    public void addToOrders(final ShoppingCart shoppingCart){
        final DocumentReference reference = firestore.collection("orders").document();
        final Order order = createOrder(shoppingCart,reference);
        firestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // add order
                transaction.set(reference,order);
                ShoppingCartManager shoppingCartManager = new ShoppingCartManager(context);
                // update cart status
                shoppingCartManager.updateStatus(transaction,order.getShoppingCartId(),true,false);
                ProductManager productManager = new ProductManager(context);
                //update stock count
                productManager.updateStock(transaction,shoppingCart, true);
                return null;
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"order confirmed successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,"problem while saving the order",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ArrayList<Order> getAllOrders(final OnSuccessListener<ArrayList<Order>> listener){

        firestore.collection("orders").whereEqualTo("confirm_status",0).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot snapshot:task.getResult()){
                                orders.add(snapshot.toObject(Order.class));
                                listener.onSuccess(orders);
                            }
                        }
                        else {
                            String localizedMessage = task.getException().getLocalizedMessage();
                            Log.e("indexx",localizedMessage);
                            Toast.makeText(context,localizedMessage,Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        return orders;
    }

    public void updateStatus(final Order order){

        final Map<String, Object> values = new HashMap<>();
        values.put("confirm_status",order.isConfirm_status() ? 1:0);
        values.put("sent_status",order.isSent_status() ? 1:0);
        final DocumentReference reference = firestore.collection("orders").document(order.getId());
        firestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                transaction.update(reference,values);
                ShoppingCartManager manager = new ShoppingCartManager(context);
                manager.updateStatus(transaction,order.getShoppingCartId(),true,true);
                return null;
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"status updated successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,"problem while updating the status",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
