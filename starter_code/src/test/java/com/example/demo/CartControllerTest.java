package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "cartController", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);

        User user = new User(); //creating new user for purpose of testing.
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("test1234");

        Cart cart = new Cart(); //creating a cart and assigning to above created user.
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        // when(cartRepository.findByUser(user)).thenReturn(cart);

        Item item = new Item(); //create new item for user.
        item.setId(1L);
        item.setName("Car");
        item.setDescription("White Car");
        item.setPrice(BigDecimal.valueOf(5000));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    }

    @Test
    public void add_item_to_cart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertEquals("test", cart.getUser().getUsername());
        assertEquals(BigDecimal.valueOf(5000), cart.getTotal());
        assertEquals(1, cart.getItems().size());
    }

    @Test
    public void remove_item_from_cart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> responseAdded = cartController.addTocart(modifyCartRequest);

        assertEquals(200, responseAdded.getStatusCodeValue());

        ModifyCartRequest removeItems = new ModifyCartRequest();

        removeItems.setUsername("test");
        removeItems.setItemId(1L);
        removeItems.setQuantity(1);

        ResponseEntity<Cart> responseRemoved = cartController.removeFromcart(removeItems);


        assertEquals(200, responseRemoved.getStatusCodeValue());

        Cart cart = responseRemoved.getBody();

        assertEquals("test", cart.getUser().getUsername());
        assertEquals(BigDecimal.valueOf(0L), cart.getTotal());
        assertEquals(0, cart.getItems().size());
    }

}

