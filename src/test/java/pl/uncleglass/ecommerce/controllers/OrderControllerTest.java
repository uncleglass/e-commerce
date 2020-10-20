package pl.uncleglass.ecommerce.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import pl.uncleglass.ecommerce.model.persistence.Cart;
import pl.uncleglass.ecommerce.model.persistence.Item;
import pl.uncleglass.ecommerce.model.persistence.User;
import pl.uncleglass.ecommerce.model.persistence.UserOrder;
import pl.uncleglass.ecommerce.model.persistence.repositories.OrderRepository;
import pl.uncleglass.ecommerce.model.persistence.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderControllerTest {
    private OrderController orderController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @BeforeEach
    public void setUp() {
        orderController = new OrderController(userRepository, orderRepository);
    }

    @Test
    public void create_order_happy_path() {
        User user = getUserWithCart();
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();

        assertEquals(2, userOrder.getItems().size());
        assertEquals(new BigDecimal("12"), userOrder.getTotal());
        assertEquals("testUser", userOrder.getUser().getUsername());
    }


    @Test
    public void create_order_user_does_not_exist() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("noUser");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_orders_user_happy_path() {
        User user = getUser();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        List<UserOrder> orders = getOrders();
        when(orderRepository.findByUser(any(User.class))).thenReturn(orders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("noUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> body = response.getBody();
        assertEquals(2, body.size());
    }

    @Test
    public void get_orders_user_does_not_exist() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("noUser");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private User getUser() {
        User user = new User();
        user.setUsername("testUser");
        return user;
    }

    private Cart getCart() {
        Item item1 = new Item();
        item1.setPrice(new BigDecimal("7"));
        Item item2 = new Item();
        item2.setPrice(new BigDecimal("5"));
        Cart cart = new Cart();
        cart.addItem(item1);
        cart.addItem(item2);
        return cart;
    }

    private User getUserWithCart() {
        User user = getUser();
        Cart cart = getCart();
        cart.setUser(user);
        user.setCart(cart);
        return user;
    }

    private List<UserOrder> getOrders() {
        List<UserOrder> orders =  new ArrayList<>();
        orders.add(new UserOrder());
        orders.add(new UserOrder());
        return orders;
    }
}