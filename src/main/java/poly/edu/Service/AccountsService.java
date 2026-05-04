package poly.edu.Service;

import java.util.List;

import poly.edu.Entity.Accounts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountsService {
//	List<Accounts> findAll();

	Accounts findById(String username);

	Accounts create(Accounts entity);

	void update(Accounts entity);

	void deleteById(String username);

	Boolean existsById(String username);

//	List<Accounts> search(String keyword);

	Page<Accounts> findAll(Pageable pageable);

	Page<Accounts> search(String keyword, Pageable pageable);
}
