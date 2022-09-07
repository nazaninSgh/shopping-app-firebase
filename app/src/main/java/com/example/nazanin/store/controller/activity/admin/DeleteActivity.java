package com.example.nazanin.store.controller.activity.admin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nazanin.store.R;
import com.example.nazanin.store.model.DAO.ProductManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DeleteActivity extends AppCompatActivity {

    private EditText idEdittext;
    private ProductManager productManager;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        productManager=new ProductManager(this);
        idEdittext=findViewById(R.id.customerCodeEdittext);

    }

    public void delete(View view) {
        if (!TextUtils.isEmpty(idEdittext.getText())) {
            int id = Integer.parseInt(idEdittext.getText().toString());
            productManager.delete(id);
            Toast.makeText(this,"کالای مورد نظر حذف شد",Toast.LENGTH_SHORT).show();
            reload();
        }
        else {
            idEdittext.setError("لطفا کد کالا را وارد کنید");
        }
    }
    private void reload(){
        idEdittext.setText("");
    }
}
