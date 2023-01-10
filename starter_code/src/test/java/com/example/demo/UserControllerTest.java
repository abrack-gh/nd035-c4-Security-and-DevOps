package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception{
        when(encoder.encode("test1234")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("test1234");
        r.setConfirmPassword("test1234");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }

    @Test
    public void create_user_pw_too_short() throws Exception{
        when(encoder.encode("test")).thenReturn("thisIsHashed");
        CreateUserRequest f = new CreateUserRequest();
        f.setUsername("test");
        f.setPassword("test"); // below minimum password req so should fail.
        f.setConfirmPassword("test"); // below minimum password req so should fail.

        ResponseEntity<User> response = userController.createUser(f); //400

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue()); //error 400 due to below minimum pw req.
    }

    @Test
    public void create_user_pw_NOT_match() throws Exception{
        when(encoder.encode("test1234")).thenReturn("thisIsHashed");
        CreateUserRequest a = new CreateUserRequest();
        a.setUsername("test");
        a.setPassword("test1234");
        a.setConfirmPassword("test12345"); // confirmPassword != password..

        ResponseEntity<User> response = userController.createUser(a);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue()); //error 400 bad request due to no matching password confirmation.
    }

    @Test
    public void testFindById() {
        User user = new User();

        long id = 1L;
        user.setId(id);
        user.setUsername("user");
        user.setPassword("test1234");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue()); // should find user.

        User y = response.getBody();

        assertNotNull(y);
        assertEquals(id, y.getId());
        assertEquals("user", y.getUsername());
        assertEquals("test1234", y.getPassword());
    }


    @Test
    public void testFindByUserName() {
        User user = new User();

        long id = 1L;
        user.setId(id);
        user.setUsername("user");
        user.setPassword("test1234");

        when(userRepository.findByUsername("user")).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("user");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue()); // should find username.

        User y = response.getBody();

        assertNotNull(y);
        assertEquals(id, y.getId());
        assertEquals("user", y.getUsername());
        assertEquals("test1234", y.getPassword());

    }

}
