package com.example.nazanin.storefirebase.controller.activity.customer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.model.DAO.CustomerManager;
import com.example.nazanin.storefirebase.model.DTO.Customer;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class IncreaseCreditActivity extends AppCompatActivity {

    private TextView fullnameTextview;
    private EditText creditEdittext;
    private Customer customer;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_increase_credit);
        //find
        fullnameTextview=findViewById(R.id.fullname);
        creditEdittext=findViewById(R.id.credit);
        //
        customer=getIntent().getExtras().getParcelable("customer");
        fullnameTextview.setText(customer.getName()+" "+customer.getLastname());

    }


    public void confirm(View view) {
        if (!TextUtils.isEmpty(creditEdittext.getText())){
            int credit=Integer.parseInt(creditEdittext.getText().toString());
            customer.setCredit(credit);
            CustomerManager customerManager=new CustomerManager(this);
            customerManager.updateCustomerCredit(customer);
            Intent intent=new Intent();
            intent.putExtra("credit",credit);
            setResult(RESULT_OK,intent);
            Toast.makeText(this,"افزایش اعتبار با موفقیت انجام شد",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            creditEdittext.setError("ابتدا مبلغ مورد نظر را وارد کنید");
        }
    }
}
