package pl.uncleglass.ecommerce.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.uncleglass.ecommerce.model.persistence.User;
import pl.uncleglass.ecommerce.model.persistence.repositories.CartRepository;
import pl.uncleglass.ecommerce.model.persistence.repositories.UserRepository;
import pl.uncleglass.ecommerce.model.requests.CreateUserRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private UserController userController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

    @BeforeEach
    public void setUp() {
        userController = new UserController(userRepository, cartRepository, passwordEncoder);
    }

    @Test
    public void create_user_happy_path() {
        when(passwordEncoder.encode("password")).thenReturn("thisIsHashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("Test");
        userRequest.setPassword("password");
        userRequest.setConfirmPassword("password");

        ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("Test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void create_user_with_short_password() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("Test");
        userRequest.setPassword("pass");
        userRequest.setConfirmPassword("pass");

        ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void create_user_with_wrong_confirm_password() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("Test");
        userRequest.setPassword("password");
        userRequest.setConfirmPassword("passwords");

        ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void get_user_by_id_happy_path() {
        User user = getUser();
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(user));

        ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User body = response.getBody();
        assertEquals("testUser", body.getUsername());
    }

    @Test
    public void get_user_by_name_happy_path() {
        User user = getUser();
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User body = response.getBody();
        assertEquals("testUser", body.getUsername());
    }

    @Test
    public void get_user_by_name_no_user() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        ResponseEntity<User> response = userController.findByUserName("testUser");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private User getUser() {
        User user = new User();
        user.setUsername("testUser");
        return user;
    }
}