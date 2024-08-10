package com.example.nazanin.storefirebase.controller;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.model.DTO.Category;
import com.example.nazanin.storefirebase.model.DTO.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;


public class FileManager {
    private static String imageUrl;

    public static int images[]={R.drawable.animalsfarm,R.drawable.bigane,R.drawable.bishoori,R.drawable.mannamedove,R.drawable.melateeshgh,R.drawable.clown,R.drawable.mebeforeyou,R.drawable.nineteen,
            R.drawable.caterpilar,R.drawable.doro,R.drawable.elphone,R.drawable.iphone,
            R.drawable.celvin,R.drawable.khosus,R.drawable.lotos,R.drawable.poochini,
            R.drawable.albertini,R.drawable.classic,R.drawable.frd,R.drawable.shoeone,
            R.drawable.filon,R.drawable.lucky,R.drawable.luckyy,R.drawable.metal,R.drawable.nalino,R.drawable.payon,R.drawable.tabe,
    };

    private static String getExtension(Uri imageUri,Context context){
        ContentResolver resolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(imageUri));
    }
    public static String saveImageToStorage(final Uri imageUri, final Context context, final OnSuccessListener<String> onSuccessListener){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("uploading");
        dialog.show();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("productImages").child(System.currentTimeMillis()+"."+getExtension(imageUri,context));
        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl = uri.toString();
                        onSuccessListener.onSuccess(imageUrl);
                        dialog.dismiss();
                        Toast.makeText(context,"uploaded successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        String localizedMessage = task.getException().getLocalizedMessage();
                        Toast.makeText(context, localizedMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return imageUrl;

    }

    public static ArrayList<String> getImageStoragePath(Context context){
        File internalStorage = context.getDir("productImages", Context.MODE_PRIVATE);
        File filePath;
        ArrayList<String> paths=new ArrayList<>();
        for (int i = 0; i < images.length ; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),images[i]);

            filePath = new File(internalStorage, images[i] + ".jpg");
            paths.add(filePath.toString());

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);

                fos.close();
            } catch (Exception ex) {

            }
        }
        return paths;
    }

    public static Bitmap getBitmapImage(String path){
        if (path == null || path.length() == 0)
            return (null);

        Bitmap image = BitmapFactory.decodeFile(path);

        return image;
    }

    public static ArrayList<Product> readProductInfo(ArrayList<String> paths,Context context){

        ArrayList<Product> products=new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("tables_info/product_info.txt")));
            int i=0;
            String line = "";
            while ((line = reader.readLine()) != null) {
                Product product=new Product(line,reader.readLine(),Integer.parseInt(reader.readLine()),Integer.parseInt(reader.readLine()),paths.get(i),reader.readLine());
                products.add(product);
                reader.readLine();
                i++;

            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return products;
    }

    public static ArrayList<Category> getCategoryInfo(Context context){
        ArrayList<Category> categories=new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("tables_info/category_info.txt")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                Category category=new Category(line,reader.readLine());
                categories.add(category);
                reader.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return categories;
    }
}
