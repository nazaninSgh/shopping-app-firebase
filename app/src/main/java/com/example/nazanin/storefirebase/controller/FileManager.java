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


public class FileManager {
    private static String imageUrl;

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

    public static Bitmap getBitmapImage(String path){
        if (path == null || path.length() == 0)
            return (null);

        Bitmap image = BitmapFactory.decodeFile(path);

        return image;
    }

}
