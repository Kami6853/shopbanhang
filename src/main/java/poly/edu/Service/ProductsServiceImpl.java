package poly.edu.Service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import poly.edu.Repository.ProductsRepository;

@Service
public class ProductsServiceImpl implements ProductsService {
	@Autowired
	ProductsRepository productsRepo;

//	@Override
//	public List<Products> findAll() {
//		// TODO Auto-generated method stub
//		return productsRepo.findAll();
//	}

	@Override
	public Products findById(Integer id) {
		Optional<Products> pro = productsRepo.findById(id);
		if (pro.isPresent())
			return pro.get();
		return null;
	}

	@Override
	public Products create(Products entity) {
		return productsRepo.save(entity);
	}

	@Override
	public void update(Products entity) {
		productsRepo.save(entity);

	}

	@Override
	public void deleteById(Integer id) {
		productsRepo.deleteById(id);

	}

	@Override
	public Boolean existsById(Integer id) {
		return productsRepo.existsById(id);
	}

	@Override
	public Page<Products> findAll(Pageable pageable) {
		return productsRepo.findAll(pageable);
	}

	@Override
	public Page<Products> findByCategory(String categoryId, Pageable pageable) {
		return productsRepo.findByCategories_Id(categoryId, pageable);
	}

//	@Override
//	public List<Products> searchByName(String keyword) {
//		return productsRepo.findByNameContaining(keyword);
//	}

	@Override
	public Page<Products> searchByName(String keyword, Pageable pageable) {
		return productsRepo.findByNameContaining(keyword, pageable);
	}

	@Override
	public Page<Products> searchByNameAndPrice(String keyword, Double minPrice, Double maxPrice, Pageable pageable) {

		boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
		boolean hasMin = minPrice != null;
		boolean hasMax = maxPrice != null;

		// 👉 Tên + khoảng giá
		if (hasKeyword && hasMin && hasMax) {
			return productsRepo.findByNameContainingAndPriceBetween(keyword, minPrice, maxPrice, pageable);
		}

		// 👉 Chỉ khoảng giá
		if (hasMin && hasMax) {
			return productsRepo.findByPriceBetween(minPrice, maxPrice, pageable);
		}

		// 👉 Chỉ tên
		if (hasKeyword) {
			return productsRepo.findByNameContaining(keyword, pageable);
		}

		// 👉 Không nhập gì
		return productsRepo.findAll(pageable);
	}

	@Override
	public List<Object[]> inventoryByCategory() {
		return productsRepo.inventoryByCategory();
	}

}
