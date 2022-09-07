package com.example.nazanin.store.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.nazanin.store.R;
import com.example.nazanin.store.model.DTO.Category;
import com.example.nazanin.store.model.DTO.Product;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

public class FileManager {

    public static int images[]={R.drawable.animalsfarm,R.drawable.bigane,R.drawable.bishoori,R.drawable.mannamedove,R.drawable.melateeshgh,R.drawable.clown,R.drawable.mebeforeyou,R.drawable.nineteen,
            R.drawable.caterpilar,R.drawable.doro,R.drawable.elphone,R.drawable.iphone,
            R.drawable.celvin,R.drawable.khosus,R.drawable.lotos,R.drawable.poochini,
            R.drawable.albertini,R.drawable.classic,R.drawable.frd,R.drawable.shoeone,
            R.drawable.filon,R.drawable.lucky,R.drawable.luckyy,R.drawable.metal,R.drawable.nalino,R.drawable.payon,R.drawable.tabe,
    };

    public static String saveImageToStorage(Bitmap bitmap,Context context){
      //  SimpleDateFormat timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss");
        String uuid = UUID.randomUUID().toString();
        String path="";
        File internalStorage = context.getDir("productImages", Context.MODE_PRIVATE);
        File filePath;
        filePath = new File(internalStorage,uuid+ ".jpg");
        path=filePath.toString();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);

            fos.close();
        } catch (Exception ex) {

        }
        return path;

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
                Product product=new Product(line,reader.readLine(),Integer.parseInt(reader.readLine()),Integer.parseInt(reader.readLine()),paths.get(i),Integer.parseInt(reader.readLine()));
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
                Category category=new Category(Integer.parseInt(line),reader.readLine());
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
