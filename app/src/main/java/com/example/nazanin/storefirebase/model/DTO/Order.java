package com.example.nazanin.storefirebase.model.DTO;

public class Order {
    private String id;
    private String shoppingCartId;
    private boolean confirm_status;
    private boolean sent_status;
    private String date;

    public Order(){

    }

    public Order(String id, String shoppingCartId, boolean confirm_status, boolean sent_status, String date) {
        this.id = id;
        this.shoppingCartId = shoppingCartId;
        this.confirm_status = confirm_status;
        this.sent_status = sent_status;
        this.date = date;
    }

    public boolean isSent_status() {
        return sent_status;
    }

    public void setSent_status(boolean sent_status) {
        this.sent_status = sent_status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(String shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public boolean isConfirm_status() {
        return confirm_status;
    }

    public void setConfirm_status(boolean confirm_status) {
        this.confirm_status = confirm_status;
    }
}
