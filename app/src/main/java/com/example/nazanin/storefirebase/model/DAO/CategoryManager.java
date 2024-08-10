package com.example.nazanin.storefirebase.model.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CategoryManager {

    private Context context;
    private FirebaseFirestore firestore;

    public CategoryManager(Context context){
        this.context=context;

    }
//    public String getCategoryIdByName(String categoryName){
//
//        final String category_id;
//        firestore.collection("categories").whereEqualTo("name",categoryName).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for (QueryDocumentSnapshot snapshot:task.getResult()){
//                            category_id = snapshot.getString("category_id");
//                        }
//                    }
//                });
//        return category_id;
//    }
}
