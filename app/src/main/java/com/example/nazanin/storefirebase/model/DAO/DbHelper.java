package com.example.nazanin.storefirebase.model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nazanin.storefirebase.controller.FileManager;
import com.example.nazanin.storefirebase.model.DTO.Category;
import com.example.nazanin.storefirebase.model.DTO.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="orderingsys.db";
    public static final int DATABASE_VERSION=3;
    private Context context;
    private ArrayList<Product> books;
    private SimpleDateFormat dateFormat;
    private SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
        dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        db.execSQL(CustomerManager.CREATE_TABLE_CUSTOMERS);
        db.execSQL(ShoppingCartManager.CREATE_TABLE_SHOPPING_CART);
        db.execSQL(OrderManager.CREATE_TABLE_ORDERS);
        db.execSQL(ProductManager.CREATE_TABLE_PRODUCTS);
        fillCategoriesTable(FileManager.getCategoryInfo(context));
        fillProductTable(FileManager.getImageStoragePath(context));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PRODUCTS");
        db.execSQL("DROP TABLE IF EXISTS CUSTOMERS");
        onCreate(db);
    }

    private void fillProductTable(ArrayList<String> paths){

        books= FileManager.readProductInfo(paths,context);
        addProductInfo(books);
    }

    private void fillCategoriesTable(ArrayList<Category> categories){

        for (int i = 0; i <categories.size() ; i++) {
            ContentValues values=new ContentValues();
            values.put("category_id",categories.get(i).getId());
            values.put("name",categories.get(i).getName());
            db.insert("CATEGORIES",null,values);
        }
    }

    private void addProductInfo(ArrayList<Product> books){
        for (int i = 0; i <books.size() ; i++) {
            ContentValues values=new ContentValues();
            values.put("image",books.get(i).getImage());
            values.put("name",books.get(i).getProductName());
            values.put("description",books.get(i).getDescription());
            values.put("stock",books.get(i).getStock());
            values.put("price",books.get(i).getPrice());
            values.put("category_id",books.get(i).getCategory_id());
            values.put("date_inserted",dateFormat.format(new Date()));
            db.insert("PRODUCTS",null,values);
        }
    }


}
