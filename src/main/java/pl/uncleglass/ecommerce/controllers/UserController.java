package pl.uncleglass.ecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.uncleglass.ecommerce.model.persistence.Cart;
import pl.uncleglass.ecommerce.model.persistence.User;
import pl.uncleglass.ecommerce.model.persistence.repositories.CartRepository;
import pl.uncleglass.ecommerce.model.persistence.repositories.UserRepository;
import pl.uncleglass.ecommerce.model.requests.CreateUserRequest;


@RestController
@RequestMapping("/api/user")
public class UserController {
	private final UserRepository userRepository;
	private final CartRepository cartRepository;

	public UserController(UserRepository userRepository, CartRepository cartRepository) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		userRepository.save(user);
		return ResponseEntity.ok(user);
	}
}
