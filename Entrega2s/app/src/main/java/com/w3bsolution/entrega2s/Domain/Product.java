package com.w3bsolution.entrega2s.Domain;

import java.sql.Date;

/**
 * Created by Ale on 8/16/2019.
 */

public class Product {

    private int id;
    private String product_token;
    private String title;
    private String description;
    private int user_id;
    private double seller_price;
    private double price_to_show;
    private String product_state;
    private String warraty;
    private String product_condition;
    private int category_id;
    private String product_image_one;
    private String product_image_two;
    private Date created_date;

    public Product() {
    }

    public Product(int id, String product_token, String title, String description, int user_id, double seller_price, double price_to_show, String product_state, String warraty, String product_condition, int category_id, String product_image_one, String product_image_two) {
        this.id = id;
        this.product_token = product_token;
        this.title = title;
        this.description = description;
        this.user_id = user_id;
        this.seller_price = seller_price;
        this.price_to_show = price_to_show;
        this.product_state = product_state;
        this.warraty = warraty;
        this.product_condition = product_condition;
        this.category_id = category_id;
        this.product_image_one = product_image_one;
        this.product_image_two = product_image_two;
    }

    public Product(int id, String product_token, String title, String description, int user_id, double seller_price, double price_to_show, String product_state, String warraty, String product_condition, int category_id, String product_image_one, String product_image_two, Date created_date) {
        this.id = id;
        this.product_token = product_token;
        this.title = title;
        this.description = description;
        this.user_id = user_id;
        this.seller_price = seller_price;
        this.price_to_show = price_to_show;
        this.product_state = product_state;
        this.warraty = warraty;
        this.product_condition = product_condition;
        this.category_id = category_id;
        this.product_image_one = product_image_one;
        this.product_image_two = product_image_two;
        this.created_date = created_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getSeller_price() {
        return seller_price;
    }

    public void setSeller_price(double seller_price) {
        this.seller_price = seller_price;
    }

    public double getPrice_to_show() {
        return price_to_show;
    }

    public void setPrice_to_show(double price_to_show) {
        this.price_to_show = price_to_show;
    }

    public String getProduct_state() {
        return product_state;
    }

    public void setProduct_state(String product_state) {
        this.product_state = product_state;
    }

    public String getWarraty() {
        return warraty;
    }

    public void setWarraty(String warraty) {
        this.warraty = warraty;
    }

    public String getProduct_condition() {
        return product_condition;
    }

    public void setProduct_condition(String product_condition) {
        this.product_condition = product_condition;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getProduct_image_one() {
        return product_image_one;
    }

    public void setProduct_image_one(String product_image_one) {
        this.product_image_one = product_image_one;
    }

    public String getProduct_image_two() {
        return product_image_two;
    }

    public void setProduct_image_two(String product_image_two) {
        this.product_image_two = product_image_two;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getProduct_token() {
        return product_token;
    }

    public void setProduct_token(String product_token) {
        this.product_token = product_token;
    }
}
