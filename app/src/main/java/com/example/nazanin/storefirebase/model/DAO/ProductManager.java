package com.example.nazanin.storefirebase.model.DAO;



import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;


import com.example.nazanin.storefirebase.model.DTO.Product;
import com.example.nazanin.storefirebase.model.DTO.ShoppingCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductManager {
    private Context context;
    private FirebaseFirestore firestore;
    private ArrayList<Product> products;
    private SimpleDateFormat dateFormat;

    public ProductManager(Context context) {
        this.context = context;
        products = new ArrayList<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        firestore = FirebaseFirestore.getInstance();
    }

    public ArrayList<Product> getNewestProducts(String category_id, final OnSuccessListener<ArrayList<Product>> listener){
        firestore.collection("products").whereEqualTo("category_id",category_id)
                .whereGreaterThan("stock",0).orderBy("stock").orderBy("datetime",Query.Direction.DESCENDING).limit(4)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot:task.getResult()){
                        products.add(snapshot.toObject(Product.class));
                        listener.onSuccess(products);
                    }
                }
                else {
                    String localizedMessage = task.getException().getLocalizedMessage();
                    Log.e("indexx",localizedMessage);
                    Toast.makeText(context,localizedMessage,Toast.LENGTH_SHORT).show();
                }
            }
        });
        return products;
    }

    public ArrayList<Product> getProductsInfo(String category_id, final OnSuccessListener<ArrayList<Product>> listener){
        firestore.collection("products").whereEqualTo("category_id",category_id).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot snapshot:task.getResult()){
                                products.add(snapshot.toObject(Product.class));
                                listener.onSuccess(products);
                            }
                        }
                        else {
                            String localizedMessage = task.getException().getLocalizedMessage();
                            Log.e("indexx",localizedMessage);
                            Toast.makeText(context,localizedMessage,Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        return products;
    }

    public Task<Product> searchProductById(String id){
        final TaskCompletionSource<Product> taskCompletionSource = new TaskCompletionSource<>();
        firestore.collection("products").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Product product = document.toObject(Product.class);
                            taskCompletionSource.setResult(product);
                        } else {
                            taskCompletionSource.setException(task.getException() != null ?
                                    task.getException() : new Exception("Product not found"));
                        }
                    }
                });


        return taskCompletionSource.getTask();
    }

    public void insert(Product product){
        product.setDate(dateFormat.format(new Date()));
        product.setSalesCount(0);
        DocumentReference reference = firestore.collection("products").document();
        product.setId(reference.getId());
        reference.set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"inserted successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,"problem while inserting",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void update(Product product){
        Map<String, Object> data = new HashMap<>();
        data.put("image",product.getImage());
        data.put("name",product.getProductName());
        data.put("description",product.getDescription());
        data.put("stock",product.getStock());
        data.put("price",product.getPrice());
        data.put("category_id",product.getCategory_id());
        firestore.collection("products").document(product.getId())
                .update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"updated successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,"problem while updating",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void delete(String id){
        firestore.collection("products").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"deleted successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,"problem while deleting",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ArrayList<Product> sortDesc(String category_id, final OnSuccessListener<ArrayList<Product>> listener){
        firestore.collection("products").whereEqualTo("category_id",category_id)
                .orderBy("price",Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        products.add(snapshot.toObject(Product.class));

                        //Toast.makeText(context,products.get(1).getProductName()+"d",Toast.LENGTH_SHORT).show();
                        listener.onSuccess(products);
                    }
                }
                else {
                    String localizedMessage = task.getException().getLocalizedMessage();
                    Log.e("indexx",localizedMessage);
                    Toast.makeText(context,localizedMessage,Toast.LENGTH_SHORT).show();
                }
            }
        });
        return products;
    }

    public ArrayList<Product> sortAsc(String category_id, final OnSuccessListener<ArrayList<Product>> listener){
        firestore.collection("products").whereEqualTo("category_id",category_id)
                .orderBy("price",Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        products.add(snapshot.toObject(Product.class));
                        //Toast.makeText(context,products.get(1).getProductName(),Toast.LENGTH_SHORT).show();
                        listener.onSuccess(products);
                    }
                }
                else {
                    String localizedMessage = task.getException().getLocalizedMessage();
                    Log.e("indexx",localizedMessage);
                    Toast.makeText(context,localizedMessage,Toast.LENGTH_SHORT).show();
                }
            }
        });
        return products;
    }

    public void getBestSeller(final OnSuccessListener<ArrayList<Product>> listener){
        firestore.collection("products").orderBy("sales_count",Query.Direction.DESCENDING).limit(4)
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot:task.getResult()){
                        products.add(snapshot.toObject(Product.class));
                        listener.onSuccess(products);
                    }
                }
                else {
                    String localizedMessage = task.getException().getLocalizedMessage();
                    Log.e("indexx",localizedMessage);
                    Toast.makeText(context,localizedMessage,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateStock(final ShoppingCart shoppingCart, final Boolean outflow){

        ProductManager productManager=new ProductManager(context);
        productManager.searchProductById(shoppingCart.getProduct_id()).addOnCompleteListener(new OnCompleteListener<Product>() {
            @Override
            public void onComplete(@NonNull Task<Product> task) {
                if (task.isSuccessful()) {
                    final DocumentReference productRef = firestore.collection("products").document(shoppingCart.getProduct_id());
                    firestore.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(productRef);
                            int currentStock = snapshot.getLong("stock").intValue();
                            int currentSalesCount = snapshot.getLong("sales_count").intValue();
                            // Calculate the new stock and sales count values
                            int newStock = outflow ? currentStock - shoppingCart.getQuantity() : currentStock + shoppingCart.getQuantity();
                            int newSalesCount = outflow ? currentSalesCount + shoppingCart.getQuantity() : currentSalesCount;

                            // Ensure stock does not go negative
                            if (newStock < 0) {
                                throw new FirebaseFirestoreException("Insufficient stock", FirebaseFirestoreException.Code.ABORTED);
                            }
                            // Update the stock and sales count in Firestore
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("stock", newStock);
                            if (outflow) {
                                updates.put("salesCount", newSalesCount);
                            }
                            transaction.update(productRef, updates);

                            return null;

                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Problem while updating", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(context, "Product wasn't found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
