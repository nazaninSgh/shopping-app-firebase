package com.example.nazanin.storefirebase.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.controller.FileManager;
import com.example.nazanin.storefirebase.model.DAO.ProductManager;
import com.example.nazanin.storefirebase.model.DAO.ShoppingCartManager;
import com.example.nazanin.storefirebase.model.DTO.Customer;
import com.example.nazanin.storefirebase.model.DTO.Product;
import com.example.nazanin.storefirebase.model.DTO.ShoppingCart;

import java.util.ArrayList;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder>{

    private ArrayList<ShoppingCart> shoppingCarts;
    private Product product;
    private ProductManager manager;
    private Integer[] stockArray;
    private int position,currentPosition;
    private String customer_id;
    private Context context;
    private IShoppingListener shoppingListener;
    private Customer customer;

    public ShoppingCartAdapter(ArrayList<ShoppingCart> shoppingCarts,Context context){
        this.shoppingCarts=shoppingCarts;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppingcart_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        this.position=position;
        manager=new ProductManager(context);
        //product=manager.searchProductById(shoppingCarts.get(position).getProduct_id());
        holder.imageView.setImageBitmap(FileManager.getBitmapImage(product.getImage()));
        holder.nameTextview.setText(product.getProductName());
        holder.descriptionTextview.setText(product.getDescription());
        holder.priceTextview.setText(String.valueOf(product.getPrice()));
        holder.toPayTextview.setText(String.valueOf(shoppingCarts.get(position).getTotalPrice()));
        int stock=product.getStock();
        if (stock>0) {
            stockArray = new Integer[stock];
            for (int i = 0; i < stock; i++) {
                stockArray[i] = i + 1;
            }
            ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, stockArray);
            holder.quantitySpinner.setAdapter(spinnerAdapter);
            if (stock!=0 && stock<shoppingCarts.get(position).getQuantity()){
                holder.quantitySpinner.setSelection(stock-1);
            }
            else {
                holder.quantitySpinner.setSelection(shoppingCarts.get(position).getQuantity() - 1);
            }
        }
        else if (stock==0){
            shoppingListener.updateShoppingDetails(position);
        }
    }

    @Override
    public int getItemCount() {
        return shoppingCarts.size();
    }

    public void setOnItemClickListener(IShoppingListener shoppingListener){
        this.shoppingListener=shoppingListener;
    }

    public interface IShoppingListener {
        void updateShoppingDetails(int position);
        void updateTotalPaymentDetails();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener, View.OnClickListener {
        public TextView toPayTextview,nameTextview,descriptionTextview,priceTextview,finished;
        public ImageView imageView;
        private Button delete;
        public Spinner quantitySpinner;
        public int quantity=1,toPay;


        public MyViewHolder(View itemView) {
            super(itemView);
            delete=itemView.findViewById(R.id.deleteButton);
            quantitySpinner=itemView.findViewById(R.id.quantity);
            nameTextview=itemView.findViewById(R.id.name);
            descriptionTextview=itemView.findViewById(R.id.description);
            priceTextview=itemView.findViewById(R.id.price);
            imageView=itemView.findViewById(R.id.productImage);
            toPayTextview=itemView.findViewById(R.id.toPay);
            quantitySpinner.setOnItemSelectedListener(this);
            delete.setOnClickListener(this);

        }

        private int calcTotal(){
            //product=manager.searchProductById(shoppingCarts.get(currentPosition).getProduct_id());
            toPay=quantity*product.getPrice();
            return toPay;
        }


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ShoppingCartManager manager;
            manager=new ShoppingCartManager(context);
            currentPosition=getAdapterPosition();
            quantity=Integer.parseInt(parent.getItemAtPosition(position).toString());
            toPayTextview.setText(String.valueOf(calcTotal()));
            shoppingCarts.get(currentPosition).setQuantity(quantity);
            shoppingCarts.get(currentPosition).setTotalPrice(toPay);
            manager.updatePaymentDetails(shoppingCarts.get(currentPosition));
            customer_id=shoppingCarts.get(currentPosition).getCustomer_id();
            shoppingListener.updateTotalPaymentDetails();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,shoppingCarts.size());
            notifyDataSetChanged();
            shoppingListener.updateShoppingDetails(position);
        }
    }
}
