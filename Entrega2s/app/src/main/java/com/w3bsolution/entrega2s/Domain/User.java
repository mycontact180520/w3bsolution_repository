package com.w3bsolution.entrega2s.Domain;

import android.graphics.Bitmap;

import java.util.List;


/**
 * Created by Ale on 8/6/2019.
 */

public class User {

    private int id;
    private String name, username;
    private String role;
    private String lastname, email, password, telefono, movil, direccion, provincia, municipio;
    private String store_name, store_detail;
    private double store_rating;
    private int card_id;
    private String image_url;

    public User() {
    }

    public User(int id, String name, String username, String role, String lastname, String email, String password, String telefono, String movil, String direccion, String provincia, String municipio, String store_name, String store_detail, double store_rating, int card_id, String image_url) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.role = role;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.movil = movil;
        this.direccion = direccion;
        this.provincia = provincia;
        this.municipio = municipio;
        this.store_name = store_name;
        this.store_detail = store_detail;
        this.store_rating = store_rating;
        this.card_id = card_id;
        this.image_url = image_url;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_detail() {
        return store_detail;
    }

    public void setStore_detail(String store_detail) {
        this.store_detail = store_detail;
    }

    public double getStore_rating() {
        return store_rating;
    }

    public void setStore_rating(double store_rating) {
        this.store_rating = store_rating;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
