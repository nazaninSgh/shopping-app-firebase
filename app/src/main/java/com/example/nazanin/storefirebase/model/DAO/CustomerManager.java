package com.example.nazanin.storefirebase.model.DAO;


import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.nazanin.storefirebase.model.DTO.Customer;
import com.example.nazanin.storefirebase.model.DTO.Product;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CustomerManager {

    private Context context;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public CustomerManager(Context context) {
        this.context = context;
    }

    public void insertCustomerData(Customer customer,OnSuccessListener<Void> onSuccessListener){
        customer.setCredit(0);
        customer.setActive(true);
        DocumentReference reference = FirebaseFirestore.getInstance().collection("customers").document();
        String id = reference.getId();
        customer.setId(id);
        reference.set(customer)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context,"signed up successfully",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context,"problem while registering",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void getCustomerByEmail(String email, final OnSuccessListener<Customer> listener){
        //final TaskCompletionSource<Customer> taskCompletionSource = new TaskCompletionSource<>();
        FirebaseFirestore.getInstance().collection("customers").whereEqualTo("email",email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot snapshot = task.getResult();
                    Customer customer = snapshot.getDocuments().get(0).toObject(Customer.class);
                    listener.onSuccess(customer);
                    //taskCompletionSource.setResult(customer);
                } else {
                    // taskCompletionSource.setException(task.getException() != null ?
                    //     task.getException() : new Exception("Product not found"));

                }
            }
        });
    }

    public void updateCustomerCredit(Customer customer,OnSuccessListener listener){
        FirebaseFirestore.getInstance().collection("customers").document(customer.getId())
                .update("credit",customer.getCredit()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"credit successfully updated",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,"problem while updating your credit",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void updateCustomerActiveState(String id,boolean isActive){
        FirebaseFirestore.getInstance().collection("customers").document(id)
                .update("isactive",isActive).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"active status successfully updated",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,"problem while updating your active status",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void getAllCustomers(final OnSuccessListener<ArrayList<Customer>> listener){
        final ArrayList<Customer> customers=new ArrayList<>();
        FirebaseFirestore.getInstance().collection("customers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                        customers.add(snapshot.toObject(Customer.class));
                    }
                    listener.onSuccess(customers);
                }
                else {
                    Toast.makeText(context,"problem while retrieving customers",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void searchCustomerById(String id,final OnSuccessListener<Customer> listener) {
        FirebaseFirestore.getInstance().collection("customers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    listener.onSuccess(snapshot.toObject(Customer.class));
                } else {
                    Toast.makeText(context, "problem while retrieving customer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
