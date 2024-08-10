package com.example.nazanin.storefirebase.controller.activity.customer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.model.DAO.CustomerManager;
import com.example.nazanin.storefirebase.model.DTO.Customer;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewCustomerInfoActivity extends AppCompatActivity {

    private TextView nameTextview,emailTextview,idTextview,creditTextview;
    private CheckBox isActiveCheckbox;
    private int customer_id;
    private Customer customer;
    private CustomerManager manager;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer_info);
        manager=new CustomerManager(this);
        customer=getIntent().getExtras().getParcelable("customer");
        nameTextview=findViewById(R.id.name);
        emailTextview=findViewById(R.id.email);
        idTextview=findViewById(R.id.id);
        creditTextview=findViewById(R.id.credit);
        isActiveCheckbox=findViewById(R.id.isAciveCheckbox);
        showCustomerInfo();
    }

    private void showCustomerInfo(){
        idTextview.setText(String.valueOf(customer.getId()));
        nameTextview.setText(customer.getName()+" "+customer.getLastname());
        emailTextview.setText(customer.getEmail());
        creditTextview.setText(String.valueOf(customer.getCredit()));
        isActiveCheckbox.setChecked(customer.isActive());
    }
}
