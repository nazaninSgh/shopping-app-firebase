package com.example.nazanin.storefirebase.controller.activity.customer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.controller.activity.BestSellerActivity;
import com.example.nazanin.storefirebase.controller.adapter.PagerAdapter;
import com.example.nazanin.storefirebase.model.DAO.CustomerManager;
import com.example.nazanin.storefirebase.model.DAO.ProductManager;
import com.example.nazanin.storefirebase.model.DAO.ShoppingCartManager;
import com.example.nazanin.storefirebase.model.DTO.Customer;
import com.example.nazanin.storefirebase.model.DTO.ShoppingCart;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private TextView fullnameTextView,emailTextview,shoppingCartQuantity;
    private TabLayout categorytablayout;
    private TabItem digitaltanitem,booktabitem,kitchenTabitem,sportTabitem,cosmeticTabitem;
    private ViewPager productListViewpager;
    private PagerAdapter pagerAdapter;
    private Customer customer;
    private ProductManager productManager;
    private CustomerManager customerManager;
    private Toolbar toolbar;
    private Context context;
    private ShoppingCartManager manager;
    private ArrayList<ShoppingCart> shoppingCartList;
    private ActionBarDrawerToggle toggle;
    public static boolean orderSaved;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_customer);
        context=this;
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();
        NavigationView navigationView=findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        //get full name
        customerManager=new CustomerManager(this);
        customer=getIntent().getExtras().getParcelable("customer");
        productManager=new ProductManager(this);
        //find
        shoppingCartQuantity=findViewById(R.id.shoppingCartnum);
        categorytablayout=findViewById(R.id.categoryTablayout);
        digitaltanitem=findViewById(R.id.digital);
        kitchenTabitem=findViewById(R.id.kitchen);
        sportTabitem=findViewById(R.id.sport);
        cosmeticTabitem=findViewById(R.id.cosmetics);
        booktabitem=findViewById(R.id.book);
        productListViewpager=findViewById(R.id.productlistViewpager);
        toolbar=findViewById(R.id.home_toolbar);
        //set shopping cart quantity in toolbar
        manager=new ShoppingCartManager(this);
        int count=manager.getShoppingCartCount(customer.getId());

        if (count>0){
            shoppingCartQuantity.setText(String.valueOf(count));
        }
        //navigation menu header
        View header=navigationView.getHeaderView(0);
        fullnameTextView=header.findViewById(R.id.fullname);
        fullnameTextView.setText(customer.getName()+" "+customer.getLastname());
        emailTextview=header.findViewById(R.id.email);
        emailTextview.setText(customer.getEmail());
        //
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setCustomFont();
        //setup viewpager
        pagerAdapter=new PagerAdapter(getSupportFragmentManager(),categorytablayout.getTabCount(),customer);
        productListViewpager.setAdapter(pagerAdapter);
        categorytablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productListViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        productListViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(categorytablayout));

    }

    public void setCustomFont() {

        ViewGroup vg = (ViewGroup) categorytablayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "font/byekan.ttf"));
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.viewMyInfo:
                Intent viewMyInfoIntent=new Intent(this,ViewCustomerInfoActivity.class);
                viewMyInfoIntent.putExtra("customer",customer);
                startActivity(viewMyInfoIntent);
                break;
            case R.id.viewBestSeller:
                Intent bestSellerIntent=new Intent(this,BestSellerActivity.class);
                startActivity(bestSellerIntent);
                break;
            case R.id.viewCategoryList:
                Intent categoryIntent=new Intent(this, ViewCategoriesActivity.class);
                categoryIntent.putExtra("customer",customer);
                startActivity(categoryIntent);
                break;
            case R.id.viewShoppingCart:
                Intent shoppingCartIntent=new Intent(this,FinalPaymentActivity.class);
                shoppingCartIntent.putExtra("customer",customer);
                startActivity(shoppingCartIntent);
                break;
            case R.id.increaseCredit:
                Intent creditIntent=new Intent(this,IncreaseCreditActivity.class);
                creditIntent.putExtra("customer",customer);
                startActivityForResult(creditIntent,1);
                break;
        }
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==1){
            customer.setCredit(data.getIntExtra("credit",0));
        }
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int count=manager.getShoppingCartCount(customer.getId());
        if (count>0){
            shoppingCartQuantity.setText(String.valueOf(count));
        }
        else {
            shoppingCartQuantity.setText("");
        }
        if (orderSaved){
            customer=customerManager.searchCustomerById(customer.getId());
        }
    }

    public void viewShoppingCart(View view) {
        Intent shoppingCartIntent=new Intent(this,FinalPaymentActivity.class);
        shoppingCartIntent.putExtra("customer",customer);
        startActivity(shoppingCartIntent);
    }
}
