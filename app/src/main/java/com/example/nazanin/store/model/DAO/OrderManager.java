package com.example.nazanin.store.model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nazanin.store.model.DTO.Order;
import com.example.nazanin.store.model.DTO.Product;
import com.example.nazanin.store.model.DTO.ShoppingCart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderManager {
    private Context context;
    private DbHelper dbHelper;
    public SQLiteDatabase db;
    private ArrayList<Order> orders=new ArrayList<>();
    private SimpleDateFormat dateFormat;
    public static final String CREATE_TABLE_ORDERS="CREATE TABLE ORDERS (ID INTEGER PRIMARY KEY AUTOINCREMENT,SHOPPINGCART_ID INTEGER,CONFIRM_STATUS INTEGER,SENT_STATUS INTEGER,DATE_INSERTED TEXT)";

    public OrderManager(Context context){
        this.context=context;
        dbHelper=new DbHelper(context);
        dateFormat=new SimpleDateFormat("yyyy-MM-dd");
    }

    public void addToOrders(ShoppingCart shoppingCart){
        db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("shoppingcart_id",shoppingCart.getId());
        values.put("confirm_status",shoppingCart.isConfirm_status());
        values.put("sent_status",shoppingCart.isSent_status());
        values.put("date_inserted",dateFormat.format(new Date()));
        db.insert("ORDERS",null,values);
    }

    public ArrayList<Order> getAllOrders(){
        Order order;
        db=dbHelper.getReadableDatabase();
        Cursor orderCursor=db.rawQuery("SELECT * FROM ORDERS WHERE CONFIRM_STATUS=0",null);
        if (orderCursor.moveToFirst()) {
            while (!orderCursor.isAfterLast()) {
                order=new Order();
                order.setId(orderCursor.getInt(orderCursor.getColumnIndex("ID")));
                order.setShoppingCartId(orderCursor.getInt(orderCursor.getColumnIndex("SHOPPINGCART_ID")));
                order.setDate(orderCursor.getString(orderCursor.getColumnIndex("DATE_INSERTED")));
                orders.add(order);
                orderCursor.moveToNext();
            }
        }
        orderCursor.close();
        return orders;
    }

    public void updateStatus(Order order){
        db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("CONFIRM_STATUS",order.isConfirm_status() ? 1:0);
        values.put("SENT_STATUS",order.isSent_status() ? 1:0);
        db.update("ORDERS",values,"ID="+order.getId(),null);
    }
}
