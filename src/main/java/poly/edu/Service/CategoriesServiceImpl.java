package poly.edu.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Entity.Categories;
import poly.edu.Repository.CategoriesRepository;

@Service
public class CategoriesServiceImpl implements CategoriesService {
	@Autowired
	CategoriesRepository catRepo;

	@Override
	public List<Categories> findAll() {
		return catRepo.findAll();
	}

	@Override
	public Categories findById(String id) {
		Optional<Categories> cat = catRepo.findById(id);
		if (cat.isPresent())
			return cat.get();
		return null;
	}

	@Override
	public Categories create(Categories entity) {
		return catRepo.save(entity);
	}

	@Override
	public void update(Categories entity) {
		catRepo.save(entity);

	}

	@Override
	public void deleteById(String id) {
		catRepo.deleteById(id);

	}

	@Override
	public Boolean existsById(String id) {

		return catRepo.existsById(id);
	}

//	@Override
//	public List<Categories> search(String keyword) {
//		return catRepo.findByIdContainingOrNameContaining(keyword, keyword);
//	}

	@Override
	public Page<Categories> findAll(Pageable pageable) {
		return catRepo.findAll(pageable);
	}

	@Override
	public Page<Categories> search(String keyword, Pageable pageable) {
		return catRepo.findByIdContainingOrNameContaining(keyword, keyword, pageable);
	}

}
