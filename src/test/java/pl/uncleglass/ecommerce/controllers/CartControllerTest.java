package pl.uncleglass.ecommerce.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import pl.uncleglass.ecommerce.model.persistence.Cart;
import pl.uncleglass.ecommerce.model.persistence.Item;
import pl.uncleglass.ecommerce.model.persistence.User;
import pl.uncleglass.ecommerce.model.persistence.repositories.CartRepository;
import pl.uncleglass.ecommerce.model.persistence.repositories.ItemRepository;
import pl.uncleglass.ecommerce.model.persistence.repositories.UserRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.uncleglass.ecommerce.controllers.TestUtils.*;

class CartControllerTest {
    private CartController cartController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @BeforeEach
    public void setUp() {
        cartController = new CartController(
                userRepository,
                cartRepository,
                itemRepository);
    }

    @Test
    public void add_to_cart_happy_path() {
        User user = getUserWithCart();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        Item item = getItem();
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.of(item));

        ResponseEntity<Cart> response = cartController.addToCart(getModifyCartRequestAdd());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart body = response.getBody();
        assertEquals(5, body.getItems().size());
        assertEquals(new BigDecimal("48"), body.getTotal());
        assertEquals("testUser", body.getUser().getUsername());
    }

    @Test
    public void add_to_cart_user_does_not_exist() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        ResponseEntity<Cart> response = cartController.addToCart(getModifyCartRequestAdd());

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void add_to_cart_item_does_not_exist() {
        User user = getUserWithCart();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        ResponseEntity<Cart> response = cartController.addToCart(getModifyCartRequestAdd());

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void remove_from_cart_happy_path() {
        User user = getUserWithCart();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        Item item = getItem();
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.of(item));

        ResponseEntity<Cart> response = cartController.removeFromCart(getModifyCartRequestRemove());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart body = response.getBody();
        assertEquals(2, body.getItems().size());
        assertEquals(new BigDecimal("12"), body.getTotal());
        assertEquals("testUser", body.getUser().getUsername());
    }

    @Test
    public void remove_from_cart_user_does_not_exist() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        ResponseEntity<Cart> response = cartController.removeFromCart(getModifyCartRequestAdd());

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void remove_from_cart_item_does_not_exist() {
        User user = getUserWithCart();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        ResponseEntity<Cart> response = cartController.removeFromCart(getModifyCartRequestAdd());

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }
}