package com.example.nazanin.store.controller.fragment.viewPagerFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nazanin.store.R;
import com.example.nazanin.store.controller.FileManager;
import com.example.nazanin.store.controller.activity.customer.ShoppingActivity;
import com.example.nazanin.store.model.DAO.ProductManager;
import com.example.nazanin.store.model.DTO.Customer;
import com.example.nazanin.store.model.DTO.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductViewpagerFragment extends Fragment {
    private ListView productListview;
    private ArrayList<Product> products=new ArrayList<>();
    private ProductManager productManager;
    private Customer customer;

    public ProductViewpagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_product_list, container, false);
        customer=getArguments().getParcelable("customer");
        productListview=view.findViewById(R.id.productListview);
        int position=getArguments().getInt("position");
        productManager=new ProductManager(getContext());
        products=productManager.getNewestProducts(position);
        CustomListview customListview=new CustomListview();
        productListview.setAdapter(customListview);
        productListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), ShoppingActivity.class);
                intent.putExtra("selectedProduct",products.get(position));
                intent.putExtra("customer",customer);
                startActivity(intent);
            }
        });
        return view;
    }

    public class CustomListview extends BaseAdapter {

        ImageView imageView;
        TextView name,price,description;

        @Override
        public int getCount() {
            return products.size();
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
            View view=null;
            if (convertView==null) {
                view = getLayoutInflater().inflate(R.layout.product_list_row, null);
            }
            else {
                view=convertView;
            }
            imageView=view.findViewById(R.id.productImage);
            name=view.findViewById(R.id.name);
            description=view.findViewById(R.id.description);
            price=view.findViewById(R.id.price);
            imageView.setImageBitmap(FileManager.getBitmapImage(products.get(position).getImage()));
            name.setText(products.get(position).getProductName());
            description.setText("توضیحات: "+products.get(position).getDescription());
            price.setText("قیمت: "+products.get(position).getPrice()+" تومان ");
            return view;
        }
    }
}
