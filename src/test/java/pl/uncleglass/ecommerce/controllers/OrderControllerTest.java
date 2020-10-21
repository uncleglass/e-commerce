package pl.uncleglass.ecommerce.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import pl.uncleglass.ecommerce.model.persistence.User;
import pl.uncleglass.ecommerce.model.persistence.UserOrder;
import pl.uncleglass.ecommerce.model.persistence.repositories.OrderRepository;
import pl.uncleglass.ecommerce.model.persistence.repositories.UserRepository;

import java.math.BigDecimal;
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
        User user = TestUtils.getUserWithCart();
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();

        assertEquals(3, userOrder.getItems().size());
        assertEquals(new BigDecimal("24"), userOrder.getTotal());
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
        User user = TestUtils.getUser();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        List<UserOrder> orders = TestUtils.getOrders();
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
}