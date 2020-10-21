package pl.uncleglass.ecommerce.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import pl.uncleglass.ecommerce.model.persistence.Item;
import pl.uncleglass.ecommerce.model.persistence.repositories.ItemRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @BeforeEach
    public void setUp() {
        itemController = new ItemController(itemRepository);
    }

    @Test
    public void get_item_happy_path() {
        Item item = TestUtils.getItem();
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item body = response.getBody();
        assertEquals("test Item", body.getName());
        assertEquals("test description", body.getDescription());
        assertEquals(new BigDecimal("12"), body.getPrice());

    }

    @Test
    public void get_item_no_exists() {
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_items_happy_path() {
        List<Item> items = TestUtils.getItems();
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> body = response.getBody();
        assertEquals(3, body.size());
    }

    @Test
    public void get_items_by_name_happy_path() {
        List<Item> items = TestUtils.getItems();
        when(itemRepository.findByName(anyString())).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("test Item");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> body = response.getBody();
        assertEquals(3, body.size());
    }

    @Test
    public void get_items_by_name_empty_list() {
        when(itemRepository.findByName(anyString())).thenReturn(new ArrayList<>());

        ResponseEntity<List<Item>> response = itemController.getItemsByName("test Item");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}