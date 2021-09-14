package com.example.madclassproject;

public class Product {
    private  int id;
    private String title;
    private float price;
    private int qty;

    public Product() {
        id = 0;
        title = "Not Set";
        price = 0.0F;
        qty = 0;
    }

    public Product(int i, String t, float p, int q) {
        id = i;
        title = t;
        price = p;
        qty = q;
    }

    public void setId(int v) { id = v; }
    public void setTitle(String v) { title = v; }
    public void setPrice(float v) { price = v; }
    public void setQty(int v) { qty = v; }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public float getPrice() { return price; }
    public float getQty() { return qty; }

}