package poly.edu.Controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import poly.edu.Entity.Products;
import poly.edu.Service.CategoriesService;
import poly.edu.Service.ProductsService;
import poly.edu.Service.UploadService;

@Controller
public class ProductsController {
	@Autowired
	ProductsService productsService;

	@Autowired
	UploadService uploadService;

	@Autowired
	CategoriesService categoriesService;

	@RequestMapping("/admin/products")
	public String index(Model model, @RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "minPrice", required = false) Double minPrice,
			@RequestParam(name = "maxPrice", required = false) Double maxPrice,
			@RequestParam(name = "page", defaultValue = "0") int page) {

		int size = 7;
		Pageable pageable = PageRequest.of(page, size);

		Page<Products> pageResult = productsService.searchByNameAndPrice(keyword, minPrice, maxPrice, pageable);

		model.addAttribute("item", new Products());
		model.addAttribute("items", pageResult.getContent());
		model.addAttribute("page", pageResult);

		model.addAttribute("keyword", keyword);
		model.addAttribute("minPrice", minPrice);
		model.addAttribute("maxPrice", maxPrice);
		model.addAttribute("categories", categoriesService.findAll());

		return "admin/products";
	}

	@PostMapping("/admin/products/create")
	public String create(@Valid @ModelAttribute("item") Products item, BindingResult result,
			@RequestParam("imageFile") MultipartFile imageFile, Model model,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page) {
		if (item.getCategories() == null || item.getCategories().getId() == null
				|| item.getCategories().getId().isBlank()) {

			result.rejectValue("categories", "NotNull", "Vui lòng chọn loại sản phẩm");
		}
		if (result.hasErrors()) {
			Pageable pageable = PageRequest.of(page, 7);
			Page<Products> pageResult = productsService.findAll(pageable);

			model.addAttribute("items", pageResult.getContent());
			model.addAttribute("page", pageResult);
			model.addAttribute("keyword", keyword);
			model.addAttribute("categories", categoriesService.findAll());

			return "admin/products";
		}

		if (!imageFile.isEmpty()) {
			File file = uploadService.save(imageFile, "/image");
			item.setImage(file.getName());
		}

		productsService.create(item);
		return "redirect:/admin/products";
	}

	@PostMapping("/admin/products/update")
	public String update(@Valid @ModelAttribute("item") Products item, BindingResult result,
			@RequestParam("imageFile") MultipartFile imageFile, Model model, RedirectAttributes redirectAttributes) {

		if (item.getId() == null) {
			result.reject("id", "Vui lòng chọn sản phẩm cần cập nhật");
		}

		if (item.getCategories() == null || item.getCategories().getId() == null
				|| item.getCategories().getId().isBlank()) {
			result.rejectValue("categories", "NotNull", "Vui lòng chọn loại sản phẩm");
		}

		if (result.hasErrors()) {

			Pageable pageable = PageRequest.of(0, 7);
			Page<Products> pageResult = productsService.findAll(pageable);

			model.addAttribute("items", pageResult.getContent());
			model.addAttribute("page", pageResult);
			model.addAttribute("categories", categoriesService.findAll());

			return "admin/products";
		}

		if (!imageFile.isEmpty()) {
			File file = uploadService.save(imageFile, "/image");
			item.setImage(file.getName());
		} else {
			item.setImage(productsService.findById(item.getId()).getImage());
		}

		productsService.update(item);

		// 🔥 THÊM DÒNG NÀY
		redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");

		return "redirect:/admin/products";
	}

	@RequestMapping("/admin/products/edit/{id}")
	public String edit(Model model, @PathVariable("id") Integer id,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "minPrice", required = false) Double minPrice,
			@RequestParam(name = "maxPrice", required = false) Double maxPrice) {
		Products item = productsService.findById(id);
		model.addAttribute("item", item);

		Pageable pageable = PageRequest.of(page, 7);
		Page<Products> pageResult;

		if (keyword != null && !keyword.trim().isEmpty()) {
			pageResult = productsService.searchByName(keyword, pageable);
		} else {
			pageResult = productsService.findAll(pageable);
		}

		model.addAttribute("items", pageResult.getContent());
		model.addAttribute("page", pageResult);
		model.addAttribute("keyword", keyword);
		model.addAttribute("minPrice", minPrice);
		model.addAttribute("maxPrice", maxPrice);
		model.addAttribute("categories", categoriesService.findAll());

		return "admin/products";
	}

	@RequestMapping("/admin/products/delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		productsService.deleteById(id);

		redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");

		return "redirect:/admin/products";
	}

}
