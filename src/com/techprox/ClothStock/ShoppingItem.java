package com.techprox.ClothStock;

import com.techprox.ClothStock.model.ProductItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by stkornsmc on 2/13/14 AD.
 */
public class ShoppingItem {

    public static ArrayList<Integer> cartList = new ArrayList<Integer>();
    public static HashMap<Integer, Integer> cart = new HashMap<Integer, Integer>();

    public static void addCart(int proid) {
        if (!cartList.contains(proid)) {
            cartList.add(proid);
            cart.put(proid, 1);

        } else {
            cart.put(proid, cart.get(proid) + 1);
        }
    }

    public static ArrayList<Integer> getItemList() {
        return cartList;
    }

    public static HashMap<Integer, Integer> getAmount() {
        return cart;
    }

    public static void deleteitem(int pos) {
        cartList.remove(pos);
        cart.remove(pos);
    }
}
