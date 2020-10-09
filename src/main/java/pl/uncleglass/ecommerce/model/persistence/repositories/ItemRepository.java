package pl.uncleglass.ecommerce.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.uncleglass.ecommerce.model.persistence.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
	public List<Item> findByName(String name);

}
