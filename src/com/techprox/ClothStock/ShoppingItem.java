package com.techprox.ClothStock;

import com.techprox.ClothStock.model.ProductItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by stkornsmc on 2/13/14 AD.
 */
public class ShoppingItem {

//    item.put("id", String.valueOf(proid));
//    item.put("name", namePro);
//    item.put("type", String.valueOf(type));
//    item.put("admix", String.valueOf(admix));
//    item.put("price", priceTv.getText().toString());
//    item.put("quantity", String.valueOf(quantity));
//    item.put("img", img);


    public static ArrayList<HashMap<String, String>> drinkOrder = new ArrayList<HashMap<String, String>>();
//    public static HashMap<Integer, Integer> cart = new HashMap<Integer, Integer>();

    public static void addCart(HashMap<String, String> orderItem) {
        drinkOrder.add(orderItem);

    }

    public static ArrayList<HashMap<String, String>> getItemList() {
        return drinkOrder;
    }

    public static int getTotal(){
        int totalPrice = 0;
        for (int i = 0; i < drinkOrder.size(); i++) {
             int unitPrice = Integer.parseInt(drinkOrder.get(i).get("price"));
            int quan = Integer.parseInt(drinkOrder.get(i).get("quantity"));
            totalPrice += unitPrice * quan;
        }

        return totalPrice;
    }

    public static int getTotalUnit(){
        int ttUnit = 0;
        for (int i = 0; i < drinkOrder.size(); i++) {
            int quan = Integer.parseInt(drinkOrder.get(i).get("quantity"));
            ttUnit += quan;
        }
        return ttUnit;
    }

//    public static HashMap<Integer, Integer> getAmount() {
//        return cart;
//    }

    public static void deleteitem(int pos) {
        drinkOrder.remove(pos);
    }
}
