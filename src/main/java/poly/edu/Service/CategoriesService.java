package poly.edu.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import poly.edu.Entity.Categories;

public interface CategoriesService {
	List<Categories> findAll();

	Categories findById(String id);

	Categories create(Categories entity);

	void update(Categories entity);

	void deleteById(String id);

	Boolean existsById(String id);

//	List<Categories> search(String keyword);

	Page<Categories> findAll(Pageable pageable);

	Page<Categories> search(String keyword, Pageable pageable);
}
