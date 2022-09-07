package com.example.nazanin.store.model.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class CategoryManager {

    private Context context;
    private DbHelper dbHelper;
    public SQLiteDatabase db;
    public static final String CREATE_TABLE_CATEGORIES="CREATE TABLE CATEGORIES (CATEGORY_ID INTEGER,NAME TEXT)";

    public CategoryManager(Context context){
        this.context=context;
        dbHelper=new DbHelper(context);
    }
    public int getCategoryIdByName(String categoryName){
        db=dbHelper.getReadableDatabase();
        int category_id=0;
        Cursor categoryCursor=db.rawQuery("SELECT CATEGORY_ID FROM CATEGORIES WHERE NAME='"+categoryName+"'",null);

        categoryCursor.moveToFirst();
        category_id = categoryCursor.getInt(categoryCursor.getColumnIndex("CATEGORY_ID"));

        return category_id;
    }
}
