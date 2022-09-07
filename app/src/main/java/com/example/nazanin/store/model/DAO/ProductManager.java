package com.example.nazanin.store.model.DAO;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import com.example.nazanin.store.model.DTO.Product;
import com.example.nazanin.store.model.DTO.ShoppingCart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProductManager {
    private Context context;
    private DbHelper dbHelper;
    private ArrayList<Product> products;
    public SQLiteDatabase db;
    private SimpleDateFormat dateFormat;
    public static final String CREATE_TABLE_PRODUCTS="CREATE TABLE PRODUCTS (ID INTEGER PRIMARY KEY AUTOINCREMENT,IMAGE TEXT,NAME TEXT,STOCK INTEGER,PRICE INTEGER,DESCRIPTION TEXT,CATEGORY_ID INTEGER,DATE_INSERTED TEXT)";

    public ProductManager(Context context){
        this.context=context;
        dbHelper=new DbHelper(context);
        products=new ArrayList<>();
        dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    }

    public ArrayList<Product> getNewestProducts(int category_id){
        Product product;
        db=dbHelper.getReadableDatabase();
        Cursor productCursor=db.rawQuery("SELECT * FROM PRODUCTS WHERE CATEGORY_ID="+category_id+" AND STOCK>0 ORDER BY datetime(DATE_INSERTED) DESC LIMIT 4",null);
        if (productCursor.moveToFirst()) {
            while (!productCursor.isAfterLast()) {
                product=new Product();
                product.setId(productCursor.getInt(productCursor.getColumnIndexOrThrow("ID")));
                product.setImage(productCursor.getString(productCursor.getColumnIndex("IMAGE")));
                product.setProductName(productCursor.getString(productCursor.getColumnIndex("NAME")));
                product.setDescription(productCursor.getString(productCursor.getColumnIndex("DESCRIPTION")));
                product.setStock(productCursor.getInt(productCursor.getColumnIndex("STOCK")));
                product.setPrice(productCursor.getInt(productCursor.getColumnIndex("PRICE")));
                product.setCategory_id(productCursor.getInt(productCursor.getColumnIndex("CATEGORY_ID")));
                products.add(product);
                productCursor.moveToNext();
            }
        }
        productCursor.close();
        return products;
    }

    public ArrayList<Product> getProductsInfo(int category_id){
        Product product;
        db=dbHelper.getReadableDatabase();
        Cursor productCursor=db.rawQuery("SELECT * FROM PRODUCTS WHERE CATEGORY_ID="+category_id,null);
        if (productCursor.moveToFirst()) {
            while (!productCursor.isAfterLast()) {
                product=new Product();
                product.setId(productCursor.getInt(productCursor.getColumnIndexOrThrow("ID")));
                product.setImage(productCursor.getString(productCursor.getColumnIndex("IMAGE")));
                product.setProductName(productCursor.getString(productCursor.getColumnIndex("NAME")));
                product.setDescription(productCursor.getString(productCursor.getColumnIndex("DESCRIPTION")));
                product.setStock(productCursor.getInt(productCursor.getColumnIndex("STOCK")));
                product.setPrice(productCursor.getInt(productCursor.getColumnIndex("PRICE")));
                product.setCategory_id(productCursor.getInt(productCursor.getColumnIndex("CATEGORY_ID")));
                products.add(product);
                productCursor.moveToNext();
            }
        }
        productCursor.close();
        return products;
    }

    public Product searchProductById(int id){
        db=dbHelper.getReadableDatabase();
        Product product=null;
        Cursor productCursor=db.rawQuery("SELECT * FROM PRODUCTS WHERE ID="+id,null);
        if (productCursor.moveToFirst()){
            product=new Product();
            product.setImage(productCursor.getString(productCursor.getColumnIndex("IMAGE")));
            product.setProductName(productCursor.getString(productCursor.getColumnIndex("NAME")));
            product.setDescription(productCursor.getString(productCursor.getColumnIndex("DESCRIPTION")));
            product.setStock(productCursor.getInt(productCursor.getColumnIndex("STOCK")));
            product.setPrice(productCursor.getInt(productCursor.getColumnIndex("PRICE")));
            product.setCategory_id(productCursor.getInt(productCursor.getColumnIndex("CATEGORY_ID")));
        }
        return product;
    }

    public void insert(Product product){
        db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("image",product.getImage());
        values.put("name",product.getProductName());
        values.put("description",product.getDescription());
        values.put("stock",product.getStock());
        values.put("price",product.getPrice());
        values.put("category_id",product.getCategory_id());
        values.put("date_inserted",dateFormat.format(new Date()));
        db.insert("PRODUCTS",null,values);
    }

    public void update(Product product){
        db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("image",product.getImage());
        values.put("name",product.getProductName());
        values.put("description",product.getDescription());
        values.put("stock",product.getStock());
        values.put("price",product.getPrice());
        values.put("category_id",product.getCategory_id());
        db.update("PRODUCTS",values,"ID="+product.getId(),null);
    }

    public void delete(int id){
        db=dbHelper.getWritableDatabase();
        db.delete("PRODUCTS","ID="+id,null);
    }

    public ArrayList<Product> sortDesc(int category_id){
        Product product;
        db=dbHelper.getReadableDatabase();
        Cursor productCursor=db.rawQuery("SELECT * FROM PRODUCTS WHERE CATEGORY_ID="+category_id+" ORDER BY PRICE DESC",null);
        if (productCursor.moveToFirst()) {
            while (!productCursor.isAfterLast()) {
                product=new Product();
                product.setId(productCursor.getInt(productCursor.getColumnIndexOrThrow("ID")));
                product.setImage(productCursor.getString(productCursor.getColumnIndex("IMAGE")));
                product.setProductName(productCursor.getString(productCursor.getColumnIndex("NAME")));
                product.setDescription(productCursor.getString(productCursor.getColumnIndex("DESCRIPTION")));
                product.setStock(productCursor.getInt(productCursor.getColumnIndex("STOCK")));
                product.setPrice(productCursor.getInt(productCursor.getColumnIndex("PRICE")));
                product.setCategory_id(productCursor.getInt(productCursor.getColumnIndex("CATEGORY_ID")));
                products.add(product);
                productCursor.moveToNext();
            }
        }
        productCursor.close();
        return products;
    }

    public ArrayList<Product> sortAsc(int category_id){
        Product product;
        db=dbHelper.getReadableDatabase();
        Cursor productCursor=db.rawQuery("SELECT * FROM PRODUCTS WHERE CATEGORY_ID="+category_id+" ORDER BY PRICE ASC",null);
        if (productCursor.moveToFirst()) {
            while (!productCursor.isAfterLast()) {
                product=new Product();
                product.setId(productCursor.getInt(productCursor.getColumnIndexOrThrow("ID")));
                product.setImage(productCursor.getString(productCursor.getColumnIndex("IMAGE")));
                product.setProductName(productCursor.getString(productCursor.getColumnIndex("NAME")));
                product.setDescription(productCursor.getString(productCursor.getColumnIndex("DESCRIPTION")));
                product.setStock(productCursor.getInt(productCursor.getColumnIndex("STOCK")));
                product.setPrice(productCursor.getInt(productCursor.getColumnIndex("PRICE")));
                product.setCategory_id(productCursor.getInt(productCursor.getColumnIndex("CATEGORY_ID")));
                products.add(product);
                productCursor.moveToNext();
            }
        }
        productCursor.close();
        return products;
    }

    public ArrayList<Integer> getBestSeller(){
        ArrayList<Integer> product_ids=new ArrayList<>();
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT PRODUCT_ID FROM SHOPPING_CART INNER JOIN ORDERS ON SHOPPING_CART.ID=ORDERS.SHOPPINGCART_ID  WHERE ORDERS.SENT_STATUS=1 GROUP BY PRODUCT_ID ORDER BY SUM(QUANTITY) DESC LIMIT 5",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int product_id = cursor.getInt(cursor.getColumnIndex("PRODUCT_ID"));
            product_ids.add(product_id);
            cursor.moveToNext();
        }
        return product_ids;
    }

    public void getBackStock(ShoppingCart shoppingCart){
        db=dbHelper.getWritableDatabase();
        ProductManager productManager=new ProductManager(context);
        Product product=productManager.searchProductById(shoppingCart.getProduct_id());
        ContentValues values=new ContentValues();
        values.put("stock",product.getStock()+shoppingCart.getQuantity());
        db.update("PRODUCTS",values,"ID="+shoppingCart.getProduct_id(),null);
    }

    public void updateStock(ShoppingCart shoppingCart){
        db=dbHelper.getWritableDatabase();
        ProductManager productManager=new ProductManager(context);
        Product product=productManager.searchProductById(shoppingCart.getProduct_id());
        ContentValues values=new ContentValues();
        values.put("stock",product.getStock()-shoppingCart.getQuantity());
        db.update("PRODUCTS",values,"ID="+shoppingCart.getProduct_id(),null);
    }

}
