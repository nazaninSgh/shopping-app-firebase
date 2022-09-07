package com.example.nazanin.store.controller.activity.admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nazanin.store.R;
import com.example.nazanin.store.controller.activity.BestSellerActivity;
import com.example.nazanin.store.controller.activity.LoginActivity;
import com.example.nazanin.store.controller.activity.customer.ProductListActivity;
import com.example.nazanin.store.controller.activity.customer.ViewCategoriesActivity;
import com.example.nazanin.store.model.DAO.ProductManager;
import com.example.nazanin.store.model.DTO.Customer;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AllCategoriesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    private TextView fullnameTextView,emailTextview;
    private Customer customer;
    private ListView listViewCategory;
    private ActionBarDrawerToggle toggle;
    private android.support.v7.widget.Toolbar toolbar;
    private static String[] categories={"کتاب","کالای دیجیتال","آرایشی,بهداشتی و سلامت","ورزشی","خانه و آشپزخانه"};
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_admin);
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();
        NavigationView navigationView=findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View header=navigationView.getHeaderView(0);
        fullnameTextView=header.findViewById(R.id.fullname);
        fullnameTextView.setText("admin123");
        emailTextview=header.findViewById(R.id.email);
        emailTextview.setText("admin123@gmail.com");
        listViewCategory=findViewById(R.id.categoryListview);
        CustomListview customListview=new CustomListview();
        listViewCategory.setAdapter(customListview);
        listViewCategory.setOnItemClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.insert:
                Intent insertIntent=new Intent(this,InsertActivity.class);
                startActivity(insertIntent);
                break;
            case R.id.delete:
                Intent deleteIntent=new Intent(this,DeleteActivity.class);
                startActivity(deleteIntent);
                break;
            case R.id.update:
                Intent updateIntent=new Intent(this,UpdateActivity.class);
                startActivity(updateIntent);
                break;
            case R.id.bestSeller:
                Intent bestSellerIntent=new Intent(this,BestSellerActivity.class);
                startActivity(bestSellerIntent);
                break;
            case R.id.viewAllCustomers:
                Intent allCustomersIntent=new Intent(this,AllCustomersActivity.class);
                startActivity(allCustomersIntent);
                break;
            case R.id.viewOrderList:
                Intent ordersIntent=new Intent(this,ViewOrdersActivity.class);
                startActivity(ordersIntent);
                break;
            case R.id.searchCustomerById:
                Intent customerSearchIntent=new Intent(this,SearchCustomerActivity.class);
                startActivity(customerSearchIntent);
                break;
            case R.id.searchProductById:
                Intent productSearchIntent=new Intent(this,ProductSearchActivity.class);
                startActivity(productSearchIntent);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent=new Intent(this,ProductListActivity.class);
        intent.putExtra("category_id",position+1);
        startActivity(intent);
    }

    class CustomListview extends BaseAdapter{
        private TextView categoryNameTextview;

        @Override
        public int getCount() {
            return categories.length;
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
            convertView=getLayoutInflater().inflate(R.layout.category_row,null);
            categoryNameTextview=convertView.findViewById(R.id.categoryName);
            categoryNameTextview.setText(AllCategoriesActivity.categories[position]);
            return convertView;
        }
    }

}
