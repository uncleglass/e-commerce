package pl.uncleglass.ecommerce.controllers;

import pl.uncleglass.ecommerce.model.persistence.Cart;
import pl.uncleglass.ecommerce.model.persistence.Item;
import pl.uncleglass.ecommerce.model.persistence.User;
import pl.uncleglass.ecommerce.model.persistence.UserOrder;
import pl.uncleglass.ecommerce.model.requests.ModifyCartRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    static Item getItem() {
        return getItem(1L);
    }

    static Item getItem(long id) {
        Item item = new Item();
        item.setId(id);
        item.setName("test Item");
        item.setDescription("test description");
        item.setPrice(new BigDecimal("12"));
        return item;
    }

    static List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        items.add(getItem(1L));
        items.add(getItem(2L));
        items.add(getItem(3L));
        return items;
    }

     static User getUser() {
        User user = new User();
        user.setUsername("testUser");
        return user;
    }

     static Cart getCart() {
         Item item1 = getItem(2L);
        item1.setPrice(new BigDecimal("7"));
        Item item2 = getItem(3L);
        item2.setPrice(new BigDecimal("5"));
        Cart cart = new Cart();
        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(getItem());
        return cart;
    }

     static User getUserWithCart() {
        User user = getUser();
        Cart cart = getCart();
        cart.setUser(user);
        user.setCart(cart);
        return user;
    }

     static List<UserOrder> getOrders() {
        List<UserOrder> orders =  new ArrayList<>();
        orders.add(new UserOrder());
        orders.add(new UserOrder());
        return orders;
    }

    static ModifyCartRequest getModifyCartRequestAdd() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("testUser");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        return modifyCartRequest;
    }

    static ModifyCartRequest getModifyCartRequestRemove() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("testUser");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        return modifyCartRequest;
    }
}
