package com.example.nazanin.store.model.DTO;

public class ShoppingCart {
    private int id;
    private int customer_id;
    private int product_id;
    private int quantity;
    private int totalPrice;
    private boolean sent_status;
    private boolean confirm_status;

    public ShoppingCart(){

    }

    public ShoppingCart(int id, int customer_id, int product_id, int quantity, int totalPrice, boolean sent_status, boolean confirm_status) {
        this.id = id;
        this.customer_id = customer_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.sent_status = sent_status;
        this.confirm_status = confirm_status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public boolean isSent_status() {
        return sent_status;
    }

    public void setSent_status(boolean sent_status) {
        this.sent_status = sent_status;
    }

    public boolean isConfirm_status() {
        return confirm_status;
    }

    public void setConfirm_status(boolean confirm_status) {
        this.confirm_status = confirm_status;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}
