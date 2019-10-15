package com.w3bsolution.entrega2s.Domain;

import java.sql.Date;

public class CreditCard {

    private int card_id;
    private String card_number;
    private double card_credit;
    private String card_ping;
    private Date card_emition_date;
    private Date card_expiration_date;

    public CreditCard(int card_id) {
        this.card_id = card_id;
    }

    public CreditCard(int card_id, String card_number, double card_credit, String card_ping, Date card_emition_date, Date card_expiration_date) {
        this.card_id = card_id;
        this.card_number = card_number;
        this.card_credit = card_credit;
        this.card_ping = card_ping;
        this.card_emition_date = card_emition_date;
        this.card_expiration_date = card_expiration_date;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public double getCard_credit() {
        return card_credit;
    }

    public void setCard_credit(double card_credit) {
        this.card_credit = card_credit;
    }

    public String getCard_ping() {
        return card_ping;
    }

    public void setCard_ping(String card_ping) {
        this.card_ping = card_ping;
    }

    public Date getCard_emition_date() {
        return card_emition_date;
    }

    public void setCard_emition_date(Date card_emition_date) {
        this.card_emition_date = card_emition_date;
    }

    public Date getCard_expiration_date() {
        return card_expiration_date;
    }

    public void setCard_expiration_date(Date card_expiration_date) {
        this.card_expiration_date = card_expiration_date;
    }
}
