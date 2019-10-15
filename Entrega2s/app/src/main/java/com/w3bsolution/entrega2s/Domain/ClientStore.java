package com.w3bsolution.entrega2s.Domain;

/**
 * Created by Ale on 9/asset3/2019.
 */

public class ClientStore {

    private int id;
    private String name;
    private String description;
    private double rating;

    public ClientStore() {
    }

    public ClientStore(int id, String name, String description, double rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
