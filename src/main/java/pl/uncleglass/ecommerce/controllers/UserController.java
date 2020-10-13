package pl.uncleglass.ecommerce.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.uncleglass.ecommerce.model.persistence.Cart;
import pl.uncleglass.ecommerce.model.persistence.User;
import pl.uncleglass.ecommerce.model.persistence.repositories.CartRepository;
import pl.uncleglass.ecommerce.model.persistence.repositories.UserRepository;
import pl.uncleglass.ecommerce.model.requests.CreateUserRequest;


@RestController
@RequestMapping("/api/user")
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public UserController(
			UserRepository userRepository,
			CartRepository cartRepository,
			BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.passwordEncoder = passwordEncoder;
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
		if (createUserRequest.getPassword().length() < 7 ||
				!createUserRequest.getConfirmPassword().equals(createUserRequest.getPassword())) {
			logger.error("Error with password. Cannot create user {} ", createUserRequest.getConfirmPassword());
			return ResponseEntity.badRequest().build();
		}
		user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		return ResponseEntity.ok(user);
	}
}
