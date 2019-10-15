package com.w3bsolution.entrega2s.Domain;

import java.sql.Date;

/**
 * Created by Ale on 9/16/2019.
 */

public class Cart {

    private int id;
    private int product_id;
    private int user_id;
    Date added_to_cart_date;

    public Cart(int id, int product_id, int user_id, Date added_to_cart_date) {
        this.id = id;
        this.product_id = product_id;
        this.user_id = user_id;
        this.added_to_cart_date = added_to_cart_date;
    }

    public Cart(int product_id, int user_id, Date added_to_cart_date) {
        this.product_id = product_id;
        this.user_id = user_id;
        this.added_to_cart_date = added_to_cart_date;
    }

    public Cart(int product_id, int user_id) {
        this.product_id = product_id;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getAdded_to_cart_date() {
        return added_to_cart_date;
    }

    public void setAdded_to_cart_date(Date added_to_cart_date) {
        this.added_to_cart_date = added_to_cart_date;
    }
}
