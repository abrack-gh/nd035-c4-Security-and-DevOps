package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);
    List<Item> itemList = new ArrayList<>();

    @Before
    public void setUp() {

        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
        Item item = new Item();
        item.setId(0L);
        item.setName("Car");
        item.setDescription("White car");
        item.setPrice(BigDecimal.valueOf(5000));

        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepository.findById(0L)).thenReturn(Optional.of(item));
        when(itemRepository.findByName("car")).thenReturn(Collections.singletonList(item));


    }

    @Test
    public void find_all_items(){

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());


    }

    @Test
    public void find_item_by_name(){

        ResponseEntity<List<Item>> response = itemController.getItemsByName("car");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> item = response.getBody();
        assertNotNull(item);
        assertEquals(1, item.size());
    }

}
