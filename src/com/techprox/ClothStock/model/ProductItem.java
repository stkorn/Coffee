package com.techprox.ClothStock.model;

/**
 * Created by stkornsmc on 2/3/14 AD.
 */
public class ProductItem {

    public String imageProduct;
    public String nameProduct;
    public int price;
    public int id;
    public String cata;


    public ProductItem(String img, String name, int price, int id, String cata) {

        this.imageProduct = img;
        this.nameProduct = name;
        this.price = price;
        this.id = id;
        this.cata = cata;

    }
}
