package poly.edu.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import poly.edu.Entity.Accounts;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, String> {

	Page<Accounts> findByUsernameContainingOrFullnameContaining(String username, String fullname, Pageable pageable);

	@Query("""
			    SELECT a FROM Accounts a
			    WHERE (a.username = :input OR a.email = :input)
			    AND a.password = :password
			""")
	Accounts login(@Param("input") String input, @Param("password") String password);
}
