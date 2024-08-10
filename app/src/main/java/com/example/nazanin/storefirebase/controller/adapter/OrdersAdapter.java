package com.example.nazanin.storefirebase.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nazanin.storefirebase.R;
import com.example.nazanin.storefirebase.model.DAO.ShoppingCartManager;
import com.example.nazanin.storefirebase.model.DTO.Order;
import com.example.nazanin.storefirebase.model.DTO.ShoppingCart;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder>{

    private ArrayList<Order> orders;
    private Context context;
    private int position;
    private IOnclickListener listener;

    public OrdersAdapter(ArrayList<Order> orders,Context context){
        this.orders=orders;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        this.position=position;
        ShoppingCartManager manager=new ShoppingCartManager(context);
        int shoppingCartId=orders.get(position).getShoppingCartId();
        ShoppingCart shoppingCart=manager.giveShoppingCart(shoppingCartId);
        holder.productId.setText(String.valueOf(shoppingCart.getProduct_id()));
        holder.customerId.setText(String.valueOf(shoppingCart.getCustomer_id()));
        holder.quantity.setText(String.valueOf(shoppingCart.getQuantity()));
        holder.date.setText(orders.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOnItemClickListener(IOnclickListener listener){
        this.listener=listener;
    }

    public interface IOnclickListener{
        void onSendClick(int position);
        void onCancelClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView productId,customerId,quantity,date;
        public Button cancel,send;

        public MyViewHolder(View itemView) {
            super(itemView);
            productId=itemView.findViewById(R.id.productId);
            customerId=itemView.findViewById(R.id.idCustomer);
            quantity=itemView.findViewById(R.id.quantity);
            date=itemView.findViewById(R.id.date);
            cancel=itemView.findViewById(R.id.cancel);
            send=itemView.findViewById(R.id.send);
            cancel.setOnClickListener(this);
            send.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position=0;
            switch (v.getId()){
                case R.id.send:
                    if (listener!=null){
                        position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION) {
                            listener.onSendClick(position);
                        }
                    }
                    break;
                case R.id.cancel:
                    if (listener!=null){
                        position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION) {
                            listener.onCancelClick(position);
                        }
                    }
                    break;
            }
            orders.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,orders.size());
            notifyDataSetChanged();
        }
    }
}
