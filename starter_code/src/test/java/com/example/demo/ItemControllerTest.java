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
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);
    List<Item> itemList = new ArrayList<>();

    @Before
    public void setUp() {
        TestUtils.injectObjects(itemController, "itemController", itemController);
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        when(itemRepository.findAll()).thenReturn(itemList);
        when(itemRepository.findById(0L)).thenReturn(Optional.of(itemList.get(0)));
        when(itemRepository.findByName("car")).thenReturn(itemList.subList(0,1));


    }
    private Item addItem(Long Id, String name, String description, BigDecimal price) {
        Item item = new Item();
        item.setId(0L);
        item.setName("car");
        item.setDescription("white car");
        item.setPrice(BigDecimal.valueOf(5000));

        return item;
    }

    @Test
    public void find_all_items(){

        ResponseEntity<List<Item>> response = itemController.getItems();

        List<Item> items = response.getBody();
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void find_item_by_name(){

        ResponseEntity<List<Item>> response = itemController.getItemsByName("car");

        Item item = (Item) response.getBody();
        assertEquals(200, response.getStatusCodeValue());

        assertNotNull(item);
        assertEquals(itemList.get(0).getName(), item.getName());
        assertEquals(itemList.get(0).getDescription(), item.getDescription());
        assertEquals(itemList.get(0).getPrice(), item.getPrice());

    }

}
