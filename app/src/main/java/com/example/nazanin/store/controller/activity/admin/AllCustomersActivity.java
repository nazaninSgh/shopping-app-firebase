package com.example.nazanin.store.controller.activity.admin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nazanin.store.R;
import com.example.nazanin.store.model.DAO.CustomerManager;
import com.example.nazanin.store.model.DTO.Customer;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AllCustomersActivity extends AppCompatActivity {
    private ListView customersList;
    private ArrayList<Customer> customers;
    private CustomerManager customerManager;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_customers);
        customersList=findViewById(R.id.customersListview);
        customerManager=new CustomerManager(this);
        customers=customerManager.getAllCustomers();
        CustomListview customListview=new CustomListview();
        customersList.setAdapter(customListview);
    }

    class CustomListview extends BaseAdapter {
        private TextView nameTextview,emailTextview,idTextview;
        private CheckBox isActiveCheckbox;

        @Override
        public int getCount() {
            return customers.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=getLayoutInflater().inflate(R.layout.customer_info_list_row,null);
            nameTextview=convertView.findViewById(R.id.name);
            emailTextview=convertView.findViewById(R.id.email);
            idTextview=convertView.findViewById(R.id.id);
            isActiveCheckbox=convertView.findViewById(R.id.isAciveCheckbox);
            idTextview.setText(String.valueOf(customers.get(position).getId()));
            nameTextview.setText(customers.get(position).getName()+" "+customers.get(position).getLastname());
            emailTextview.setText(customers.get(position).getEmail());
            isActiveCheckbox.setChecked(customers.get(position).isActive());
            return convertView;
        }
    }

}
