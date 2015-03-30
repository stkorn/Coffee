package com.techprox.ClothStock.model;

/**
 * Created by stkornsmc on 2/13/14 AD.
 */
public class CartItem {

    public String imageProduct;
    public String nameProduct;
    public int price;
    public int id;
    public String cata;
    public int amount;


    public CartItem(String img, String name, int price, int amount) {

        this.imageProduct = img;
        this.nameProduct = name;
        this.price = price;
        this.amount = amount;

    }

}
