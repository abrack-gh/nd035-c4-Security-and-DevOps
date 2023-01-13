package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import javax.persistence.criteria.Order;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        Item item = new Item(); //create new item for user.
        item.setId(1L);
        item.setName("Car");
        item.setDescription("White Car");
        item.setPrice(BigDecimal.valueOf(5000));
        List<Item> items = new ArrayList<>();
        items.add(item);


        User y = new User(); //creating new user for purpose of testing.
        Cart cart = new Cart();

        y.setId(0);
        y.setUsername("test");
        y.setPassword("test1234");
        cart.setId(0L);
        cart.setUser(y);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(5000));
        y.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(y);

    }

    @Test
    public void submit_order(){

        ResponseEntity<UserOrder> response = orderController.submit("test");

        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();

        Cart cart = new Cart();

        assertNotNull(userOrder);
        assertEquals(1, userOrder.getItems().size());

    }

    @Test
    public void get_user_order(){
        orderController.submit("test");

        ResponseEntity<List<UserOrder>> responseGet = orderController.getOrdersForUser("test");

        assertEquals(200, responseGet.getStatusCodeValue());

        List<UserOrder> userOrders = responseGet.getBody();

        assertNotNull(userOrders);



    }


}
