package com.example.nazanin.storefirebase.controller.activity.admin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.controller.fragment.dialogFragments.ActiveChangeDialogFragment;
import com.example.nazanin.storefirebase.model.DAO.CustomerManager;
import com.example.nazanin.storefirebase.model.DTO.Customer;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchCustomerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView idTextview,nameTextview,emailTextview,creditTextview;
    public CheckBox isActiveCheckbox;
    private EditText idAdmin;
    private int id;
    private Customer customer;
    private CustomerManager customerManager;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);
        idAdmin=findViewById(R.id.idAdmin);
        idTextview=findViewById(R.id.idCustomer);
        nameTextview=findViewById(R.id.name);
        emailTextview=findViewById(R.id.email);
        creditTextview=findViewById(R.id.credit);
        isActiveCheckbox=findViewById(R.id.isAciveCheckbox);
        isActiveCheckbox.setOnClickListener(this);
    }

    public void search(View view){
        String id = idAdmin.getText().toString();
        CustomerManager customerManager = new CustomerManager(this);
        customer=customerManager.searchCustomerById(id);
        if (customer!=null) {
            showCustomerInfo();
        }
        else {
            Toast.makeText(this,"کاربر مورد نظر یافت نشد",Toast.LENGTH_SHORT).show();
        }
    }

    private void showCustomerInfo(){
        idTextview.setText(String.valueOf(customer.getId()));
        nameTextview.setText(customer.getName()+" "+customer.getLastname());
        emailTextview.setText(customer.getEmail());
        creditTextview.setText(String.valueOf(customer.getCredit()));
        isActiveCheckbox.setChecked(customer.isActive());
    }

    //checkbox click
    @Override
    public void onClick(View v) {
        if (customer!=null) {
            openDialog();
        }

    }
    private void openDialog(){
        Bundle bundle=new Bundle();
        bundle.putBoolean("ifchecked",isActiveCheckbox.isChecked());
        bundle.putInt("id",id);
        ActiveChangeDialogFragment fragment=new ActiveChangeDialogFragment();
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),"changeActiveState");
    }

}
