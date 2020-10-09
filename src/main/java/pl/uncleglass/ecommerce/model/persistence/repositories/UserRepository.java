package pl.uncleglass.ecommerce.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.uncleglass.ecommerce.model.persistence.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
