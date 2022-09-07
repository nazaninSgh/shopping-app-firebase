package com.example.nazanin.store.model.DAO;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.nazanin.store.model.DTO.Customer;
import com.example.nazanin.store.model.DTO.ShoppingCart;

import java.util.ArrayList;


public class CustomerManager {

    private Context context;
    private DbHelper dbHelper;
    private int id;
    private Customer customer;
    public SQLiteDatabase db;
    public static final String CREATE_TABLE_CUSTOMERS="CREATE TABLE CUSTOMERS (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,LASTNAME TEXT,EMAIL TEXT,PASSWORD TEXT,CREDIT INTEGER,ISACTIVE INTEGER)";

    public CustomerManager(Context context){
        this.context=context;
        dbHelper=new DbHelper(context);
    }

    public boolean insertCustomerData(Customer customer){
        if (!isDuplicate(customer)) {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("NAME", customer.getName());
            values.put("LASTNAME", customer.getLastname());
            values.put("EMAIL", customer.getEmail());
            values.put("PASSWORD", customer.getPassword());
            values.put("CREDIT", 0);
            values.put("ISACTIVE", customer.isActive() ? 1 : 0);
            db.insert("CUSTOMERS", null, values);
            return true;
        }
        return false;
    }

    private boolean isDuplicate(Customer customer){
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT ID FROM CUSTOMERS WHERE EMAIL='"+customer.getEmail()+"'",null);
        if (cursor!=null && cursor.moveToFirst() && cursor.getCount()>0) {
            return true;
        }
        return false;
    }

    public boolean customerExist(String email,String password){
        db=dbHelper.getReadableDatabase();
        Cursor customerCursor=db.rawQuery("SELECT ID FROM CUSTOMERS WHERE EMAIL='"+email+"' AND "+"PASSWORD='"+password+"'",null);
        int row=customerCursor.getCount();
        if (row>0 && customerCursor.moveToFirst()) {
            id = customerCursor.getInt(customerCursor.getColumnIndex("ID"));
            return true;
        }
        return false;
    }

    public int getCustomerId(Customer customer){
        db=dbHelper.getReadableDatabase();
        Cursor customerCursor=db.rawQuery("SELECT ID FROM CUSTOMERS WHERE EMAIL='"+customer.getEmail()+"' AND "+"PASSWORD='"+customer.getPassword()+"'",null);
        customerCursor.moveToFirst();
        customer.setId(customerCursor.getInt(customerCursor.getColumnIndex("ID")));
        return customer.getId();
    }


    public Customer getThatCustomer(){
        db=dbHelper.getReadableDatabase();
        customer=new Customer();
        Cursor customerCursor=db.rawQuery("SELECT * FROM CUSTOMERS WHERE ID="+id,null);
        if (customerCursor.moveToFirst()) {
            customer.setId(customerCursor.getInt(customerCursor.getColumnIndex("ID")));
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
        ArrayList<Customer> customers=new ArrayList<>();
        db=dbHelper.getReadableDatabase();
        Cursor customerCursor=db.rawQuery("SELECT * FROM CUSTOMERS",null);
        if (customerCursor.moveToFirst()) {
            while (!customerCursor.isAfterLast()) {
                customer=new Customer();
                customer.setId(customerCursor.getInt(customerCursor.getColumnIndex("ID")));
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

    public Customer searchCustomerById(int id){
        db=dbHelper.getReadableDatabase();
        Cursor customerCursor=db.rawQuery("SELECT * FROM CUSTOMERS WHERE ID="+id,null);
        if (customerCursor.moveToFirst()) {
            customer=new Customer();
            customer.setId(customerCursor.getInt(customerCursor.getColumnIndex("ID")));
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
