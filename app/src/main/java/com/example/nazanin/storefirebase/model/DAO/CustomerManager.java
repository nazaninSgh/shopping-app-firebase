package com.example.nazanin.storefirebase.model.DAO;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    DbHelper dbHelper;
    SQLiteDatabase db;
    private int id;
    private Customer customer;
    public static final String CREATE_TABLE_CUSTOMERS="CREATE TABLE CUSTOMERS (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,LASTNAME TEXT,EMAIL TEXT,PASSWORD TEXT,CREDIT INTEGER,ISACTIVE INTEGER)";


    public CustomerManager(Context context){
        this.context=context;

        database = FirebaseDatabase.getInstance("https://fir-demo-adc8c-default-rtdb.firebaseio.com/");
    }

    public void insertCustomerData(Customer customer,OnSuccessListener<Void> onSuccessListener){
        customer.setCredit(0);
        customer.setActive(false);
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


    public Customer getThatCustomer(){
        db=dbHelper.getReadableDatabase();
        customer=new Customer();
        Cursor customerCursor=db.rawQuery("SELECT * FROM CUSTOMERS WHERE ID="+id,null);
        if (customerCursor.moveToFirst()) {
           // customer.setId(customerCursor.getInt(customerCursor.getColumnIndex("ID")));
            customer.setName(customerCursor.getString(customerCursor.getColumnIndex("NAME")));
            customer.setLastname(customerCursor.getString(customerCursor.getColumnIndex("LASTNAME")));
            customer.setEmail(customerCursor.getString(customerCursor.getColumnIndex("EMAIL")));
            customer.setCredit(customerCursor.getInt(customerCursor.getColumnIndex("CREDIT")));
            customer.setActive(customerCursor.getInt(customerCursor.getColumnIndex("ISACTIVE")) == 1 ? true : false);

        }
        customerCursor.close();
        return customer;
    }

    public void updateCustomerCredit(Customer customer){
        db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("CREDIT",customer.getCredit());
        db.update("CUSTOMERS",values,"ID="+customer.getId(),null);
    }

    public void updateCustomerActiveState(int id,boolean isActive){
        db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("ISACTIVE",isActive ? 1:0);
        db.update("CUSTOMERS",values,"ID="+id,null);
    }

    public ArrayList<Customer> getAllCustomers(){
        reference = database.getReference("customer");

        ArrayList<Customer> customers=new ArrayList<>();
        db=dbHelper.getReadableDatabase();
        Cursor customerCursor=db.rawQuery("SELECT * FROM CUSTOMERS",null);
        if (customerCursor.moveToFirst()) {
            while (!customerCursor.isAfterLast()) {
                customer=new Customer();
               // customer.setId(customerCursor.getInt(customerCursor.getColumnIndex("ID")));
                customer.setName(customerCursor.getString(customerCursor.getColumnIndex("NAME")));
                customer.setLastname(customerCursor.getString(customerCursor.getColumnIndex("LASTNAME")));
                customer.setEmail(customerCursor.getString(customerCursor.getColumnIndex("EMAIL")));
                customer.setActive(customerCursor.getInt(customerCursor.getColumnIndex("ISACTIVE")) == 1 ? true : false);
                customers.add(customer);
                customerCursor.moveToNext();
            }
        }
        customerCursor.close();
        return customers;
    }

    public Customer searchCustomerById(String id){
        db=dbHelper.getReadableDatabase();
        Cursor customerCursor=db.rawQuery("SELECT * FROM CUSTOMERS WHERE ID="+id,null);
        if (customerCursor.moveToFirst()) {
            customer=new Customer();
           // customer.setId(customerCursor.getInt(customerCursor.getColumnIndex("ID")));
            customer.setName(customerCursor.getString(customerCursor.getColumnIndex("NAME")));
            customer.setLastname(customerCursor.getString(customerCursor.getColumnIndex("LASTNAME")));
            customer.setEmail(customerCursor.getString(customerCursor.getColumnIndex("EMAIL")));
            customer.setCredit(customerCursor.getInt(customerCursor.getColumnIndex("CREDIT")));
            customer.setActive(customerCursor.getInt(customerCursor.getColumnIndex("ISACTIVE"))==1 ? true:false);
        }
        customerCursor.close();
        return customer;
    }
}
