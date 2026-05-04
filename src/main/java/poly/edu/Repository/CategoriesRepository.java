package poly.edu.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poly.edu.Entity.Categories;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, String> {
//	List<Categories> findByIdContainingOrNameContaining(String id, String name);

	Page<Categories> findByIdContainingOrNameContaining(String id, String name, Pageable pageable);
}
