package pl.uncleglass.ecommerce.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.uncleglass.ecommerce.model.persistence.User;
import pl.uncleglass.ecommerce.model.persistence.UserOrder;
import pl.uncleglass.ecommerce.model.persistence.repositories.OrderRepository;
import pl.uncleglass.ecommerce.model.persistence.repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	private final Logger logger = LoggerFactory.getLogger(OrderController.class);
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;

	public OrderController(UserRepository userRepository, OrderRepository orderRepository) {
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
	}

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.error("User {} does not exist", username);
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		logger.info("Order nr {} placed correctly", order.getId());
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
