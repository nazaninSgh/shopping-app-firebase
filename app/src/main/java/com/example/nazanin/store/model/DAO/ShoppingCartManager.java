package com.example.nazanin.store.model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.nazanin.store.model.DTO.ShoppingCart;

import java.util.ArrayList;

public class ShoppingCartManager {
    private Context context;
    private DbHelper dbHelper;
    private ShoppingCart shoppingCart;
    private ArrayList<ShoppingCart> shoppingCarts;
    public SQLiteDatabase db;
    public static final String CREATE_TABLE_SHOPPING_CART="CREATE TABLE SHOPPING_CART (ID INTEGER PRIMARY KEY AUTOINCREMENT,CUSTOMER_ID INTEGER,PRODUCT_ID INTEGER,QUANTITY INTEGER,TOTAL_PRICE INTEGER,CONFIRM_STATUS INTEGER,SENT_STATUS INTEGER)";

    public ShoppingCartManager(Context context){
        this.context=context;
        dbHelper=new DbHelper(context);
    }


    public void addToShoppingCart(ShoppingCart shoppingCart){
        if (!isDuplicate(shoppingCart)) {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("customer_id", shoppingCart.getCustomer_id());
            values.put("product_id", shoppingCart.getProduct_id());
            values.put("quantity", shoppingCart.getQuantity());
            values.put("total_price", shoppingCart.getTotalPrice());
            values.put("confirm_status", shoppingCart.isConfirm_status() ? 1 : 0);
            values.put("sent_status", shoppingCart.isSent_status() ? 1 : 0);
            db.insert("SHOPPING_CART", null, values);
        }
    }

    private boolean isDuplicate(ShoppingCart shoppingCart){
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT ID FROM SHOPPING_CART WHERE CONFIRM_STATUS=0 AND SENT_STATUS=0 AND CUSTOMER_ID="+shoppingCart.getCustomer_id()+" AND PRODUCT_ID="+shoppingCart.getProduct_id(),null);
        if (cursor!=null && cursor.moveToFirst() && cursor.getCount()>0) {
        //    Toast.makeText(context,String.valueOf(cursor.getInt(cursor.getColumnIndex("ID"))),Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public int getShoppingCartCount(int id){
        int count=0;
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM SHOPPING_CART WHERE CUSTOMER_ID="+id+" AND CONFIRM_STATUS=0",null);
        cursor.moveToFirst();
        count=cursor.getCount();
        cursor.close();
        return count;
    }

    public ArrayList<ShoppingCart> getShoppingCartList(int customer_id){
        shoppingCarts=new ArrayList<>();
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM SHOPPING_CART WHERE CUSTOMER_ID="+customer_id+" AND CONFIRM_STATUS=0",null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                shoppingCart = new ShoppingCart();
                shoppingCart.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                shoppingCart.setCustomer_id(cursor.getInt(cursor.getColumnIndex("CUSTOMER_ID")));
                shoppingCart.setProduct_id(cursor.getInt(cursor.getColumnIndex("PRODUCT_ID")));
                shoppingCart.setQuantity(cursor.getInt(cursor.getColumnIndex("QUANTITY")));
                shoppingCart.setTotalPrice(cursor.getInt(cursor.getColumnIndex("TOTAL_PRICE")));
                shoppingCart.setConfirm_status(cursor.getInt(cursor.getColumnIndex("CONFIRM_STATUS")) == 1 ? true : false);
                shoppingCarts.add(shoppingCart);
                cursor.moveToNext();
            }
        }
        return shoppingCarts;
    }

    public void deleteFromShoppingCart(int id){
        db=dbHelper.getWritableDatabase();
        db.delete("SHOPPING_CART","ID="+id,null);
    }

    public void updatePaymentDetails(ShoppingCart shoppingCart){
        db=dbHelper.getWritableDatabase();
     //   Toast.makeText(context,String.valueOf(shoppingCart.getQuantity()+"     "+shoppingCart.getProduct_id()+"     "+shoppingCart.getCustomer_id()),Toast.LENGTH_SHORT).show();
        ContentValues values=new ContentValues();
        values.put("quantity",shoppingCart.getQuantity());
        values.put("total_price",shoppingCart.getTotalPrice());
        db.update("SHOPPING_CART",values,"ID="+shoppingCart.getId(),null);
    }

    public int getTotalPayment(int id){
        db=dbHelper.getReadableDatabase();
        int totalPrice=0;
        Cursor cursor=db.rawQuery("SELECT SUM(TOTAL_PRICE) AS TOTAL FROM SHOPPING_CART WHERE CUSTOMER_ID="+id+" AND CONFIRM_STATUS=0",null);
        if(cursor.moveToFirst()){

            totalPrice=cursor.getInt(cursor.getColumnIndex("TOTAL"));
        //    Toast.makeText(context,String.valueOf(totalPrice),Toast.LENGTH_SHORT).show();
        }
        return totalPrice;
    }

    public ShoppingCart giveShoppingCart(int id){
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM SHOPPING_CART WHERE ID="+id,null);
        if (cursor.moveToFirst()) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            shoppingCart.setCustomer_id(cursor.getInt(cursor.getColumnIndex("CUSTOMER_ID")));
            shoppingCart.setProduct_id(cursor.getInt(cursor.getColumnIndex("PRODUCT_ID")));
            shoppingCart.setQuantity(cursor.getInt(cursor.getColumnIndex("QUANTITY")));
            shoppingCart.setTotalPrice(cursor.getInt(cursor.getColumnIndex("TOTAL_PRICE")));
            shoppingCart.setConfirm_status(cursor.getInt(cursor.getColumnIndex("CONFIRM_STATUS")) == 1 ? true : false);
        }
        return shoppingCart;
    }

    public void updateStatus(ShoppingCart shoppingCart){
        db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("confirm_status",shoppingCart.isConfirm_status() ? 1:0);
        db.update("SHOPPING_CART",values,"ID="+shoppingCart.getId(),null);
    }
}
