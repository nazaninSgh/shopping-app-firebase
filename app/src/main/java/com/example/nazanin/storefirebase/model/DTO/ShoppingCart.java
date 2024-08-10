package com.example.nazanin.storefirebase.model.DTO;

public class ShoppingCart {
    private String id;
    private String customer_id;
    private String product_id;
    private int quantity;
    private int totalPrice;
    private boolean sent_status;
    private boolean confirm_status;

    public ShoppingCart(){

    }

    public ShoppingCart(String id, String customer_id, String product_id, int quantity, int totalPrice, boolean sent_status, boolean confirm_status) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
