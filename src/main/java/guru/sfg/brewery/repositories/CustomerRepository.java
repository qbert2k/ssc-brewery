package guru.sfg.brewery.repositories;

import guru.sfg.brewery.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    List<Customer> findAllByCustomerNameLike(String customerName);

    Optional<Customer> findAllByCustomerName(String customerName);
}
