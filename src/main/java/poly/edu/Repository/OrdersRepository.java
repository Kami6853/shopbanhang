package poly.edu.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poly.edu.Entity.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
	List<Orders> findByAccounts_Username(String username);
}
