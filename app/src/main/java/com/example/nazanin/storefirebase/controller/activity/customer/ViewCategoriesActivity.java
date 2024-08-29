package com.example.nazanin.storefirebase.controller.activity.customer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.model.DTO.Customer;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewCategoriesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView categoriesListview;
    private static String[] categories={"کتاب","کالای دیجیتال","آرایشی,بهداشتی و سلامت","ورزشی","خانه و آشپزخانه"};
    private Customer customer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_categories);
        customer=getIntent().getExtras().getParcelable("customer");
        categoriesListview=findViewById(R.id.categoryListview);
        CustomListview customListview=new CustomListview();
        categoriesListview.setAdapter(customListview);
        categoriesListview.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(ViewCategoriesActivity.this,ProductListActivity.class);
        intent.putExtra("category_id",String.valueOf(position));
        intent.putExtra("customer",customer);
        startActivity(intent);
    }

    class CustomListview extends BaseAdapter {
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
            categoryNameTextview.setText(ViewCategoriesActivity.categories[position]);
            return convertView;
        }
    }
}
