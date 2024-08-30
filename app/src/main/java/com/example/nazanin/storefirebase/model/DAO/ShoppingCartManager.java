package com.example.nazanin.storefirebase.model.DAO;


import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.nazanin.storefirebase.model.DTO.ShoppingCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.firestore.AggregateQuerySnapshot;
//import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
//import com.google.firebase.firestore.AggregateField;
//import com.google.firebase.firestore.AggregateQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCartManager {
    private Context context;
    private FirebaseFirestore firestore;
    private ShoppingCart shoppingCart;
    private ArrayList<ShoppingCart> shoppingCarts;

    public ShoppingCartManager(Context context){
        this.context=context;
        firestore = FirebaseFirestore.getInstance();
    }


    public void addToShoppingCart(final ShoppingCart shoppingCart, OnCompleteListener<Void> listener){
        isDuplicate(shoppingCart, new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean duplicate) {
                if (!duplicate){
                    Map<String,Object> shopping_cart = new HashMap<>();
                    shopping_cart.put("customer_id", shoppingCart.getCustomer_id());
                    shopping_cart.put("product_id", shoppingCart.getProduct_id());
                    shopping_cart.put("quantity", shoppingCart.getQuantity());
                    shopping_cart.put("total_price", shoppingCart.getTotalPrice());
                    shopping_cart.put("confirm_status", shoppingCart.isConfirm_status() ? 1 : 0);
                    shopping_cart.put("sent_status", shoppingCart.isSent_status() ? 1 : 0);
                    DocumentReference reference = firestore.collection("shopping_cart").document();
                    shoppingCart.setId(reference.getId());
                    reference.set(shopping_cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(context,"added to shopping cart successfully",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context,"problem while updating the shopping cart",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(context,"duplicate record found",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void isDuplicate(ShoppingCart shoppingCart, final OnSuccessListener<Boolean> listener){

        firestore.collection("shopping_cart").whereEqualTo("confirm_status",0)
                .whereEqualTo("sent_status",0).whereEqualTo("customer_id",shoppingCart.getCustomer_id())
                .whereEqualTo("product_id",shoppingCart.getProduct_id()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().size() == 0){
                    listener.onSuccess(false);
                }
                else{
                    listener.onSuccess(true);
                }
            }
        });
    }

    public void getShoppingCartCount(String id,final OnSuccessListener<Integer> listener){
        firestore.collection("shopping_cart").whereEqualTo("customer_id",id).whereEqualTo("confirm_status",0)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    listener.onSuccess(task.getResult().size());
                }
            }
        });
    }

    public void getShoppingCartList(String customer_id, final OnSuccessListener<ArrayList<ShoppingCart>> listener){
        shoppingCarts=new ArrayList<>();
        firestore.collection("shopping_cart").whereEqualTo("customer_id",customer_id)
                .whereEqualTo("confirm_status",0).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        shoppingCarts.add(snapshot.toObject(ShoppingCart.class));
                    }
                    listener.onSuccess(shoppingCarts);
                }
                else {
                    Toast.makeText(context,"problem while getting the shopping carts",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteFromShoppingCart(String id, OnCompleteListener listener){
        firestore.collection("shopping_cart").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"shopping cart deleted successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,"problem while deleting the shopping cart",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updatePaymentDetails(ShoppingCart shoppingCart,OnCompleteListener listener) {
        Map<String, Object> values = new HashMap<>();
        values.put("quantity", shoppingCart.getQuantity());
        values.put("total_price", shoppingCart.getTotalPrice());
        firestore.collection("shopping_cart").document(shoppingCart.getCustomer_id())
                .update(values).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"shopping cart updated successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,"problem while updating the shopping cart",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getTotalPayment(String id, final OnSuccessListener<Integer> listener){
        firestore.collection("shopping_cart").whereEqualTo("customer_id",id)
                .whereEqualTo("confirm_status",0).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int sum = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        int total = Integer.parseInt(document.getString("total_price"));
                        sum = sum + total;
                    }
                    listener.onSuccess(sum);
                }
            }
        });

//        AggregateQuery aggregateQuery = query.aggregate(AggregateField.sum("total_price"));
//        aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
//            @Override
//            public void onComplete(Task<AggregateQuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    AggregateQuerySnapshot snapshot = task.getResult();
//                    listener.onSuccess((Integer) snapshot.get(AggregateField.sum("total_price")));
//                }
//            }
//        });
    }

    public void giveShoppingCart(String id, final OnSuccessListener<ShoppingCart> listener){
        firestore.collection("shopping_cart").whereEqualTo("id",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        shoppingCart = snapshot.toObject(ShoppingCart.class);
                    }
                    listener.onSuccess(shoppingCart);
                }
                else {
                    Toast.makeText(context,"problem while getting the shopping cart",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateStatus(Transaction transaction, String id,Boolean confirmStatus,Boolean sentStatus){
        Map<String, Object> values = new HashMap<>();
        values.put("confirm_status",confirmStatus ? 1:0);
        values.put("sent_status",sentStatus ? 1:0);
        DocumentReference reference = firestore.collection("shopping_cart").document(id);
        transaction.update(reference,values);
    }
}
