package pl.uncleglass.ecommerce.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.uncleglass.ecommerce.model.persistence.Cart;
import pl.uncleglass.ecommerce.model.persistence.User;


public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
