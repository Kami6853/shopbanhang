package poly.edu.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import poly.edu.Entity.Products;

public interface ProductsService {
//	List<Products> findAll();

	Products findById(Integer id);

	Products create(Products entity);

	void update(Products entity);

	void deleteById(Integer id);

	Boolean existsById(Integer id);

	Page<Products> findAll(Pageable pageable);

	Page<Products> findByCategory(String categoryId, Pageable pageable);

//	List<Products> searchByName(String keyword);

	Page<Products> searchByName(String keyword, Pageable pageable);

	Page<Products> searchByNameAndPrice(String keyword, Double minPrice, Double maxPrice, Pageable pageable);

	List<Object[]> inventoryByCategory();
}
