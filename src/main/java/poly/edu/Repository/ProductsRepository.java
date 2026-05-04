package poly.edu.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import poly.edu.Entity.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {
	Page<Products> findByCategories_Id(String categoryId, Pageable pageable);

//	List<Products> findByNameContaining(String name);

	Page<Products> findByNameContaining(String name, Pageable pageable);

	Page<Products> findByPriceBetween(Double min, Double max, Pageable pageable);

	Page<Products> findByNameContainingAndPriceBetween(String name, Double min, Double max, Pageable pageable);

	@Query("""
			    SELECT
			        c.name,
			        AVG(p.price),
			        SUM(p.price)
			    FROM Products p
			    JOIN p.categories c
			    GROUP BY c.name
			""")
	List<Object[]> inventoryByCategory();

}
