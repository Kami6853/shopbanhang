package poly.edu.Controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import poly.edu.Entity.Categories;
import poly.edu.Service.CategoriesService;

@Controller
public class CategoriesController {
	@Autowired
	CategoriesService categoriesService;

	@RequestMapping("/admin/categories")
	public String index(Model model, @RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page) {

		Pageable pageable = PageRequest.of(page, 5);

		Page<Categories> pageResult;

		if (keyword != null && !keyword.trim().isEmpty()) {
			pageResult = categoriesService.search(keyword, pageable);
		} else {
			pageResult = categoriesService.findAll(pageable);
		}

		model.addAttribute("item", new Categories());
		model.addAttribute("items", pageResult.getContent());
		model.addAttribute("page", pageResult);
		model.addAttribute("keyword", keyword);

		return "admin/categories";
	}

	@PostMapping("/admin/categories/create")
	public String create(@Valid @ModelAttribute("item") Categories item, BindingResult result, Model model,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page) {
		if (result.hasErrors()) {

			Pageable pageable = PageRequest.of(page, 5);
			Page<Categories> pageResult;

			if (keyword != null && !keyword.trim().isEmpty()) {
				pageResult = categoriesService.search(keyword, pageable);
			} else {
				pageResult = categoriesService.findAll(pageable);
			}

			model.addAttribute("item", item); // 🔥 BẮT BUỘC
			model.addAttribute("items", pageResult.getContent());
			model.addAttribute("page", pageResult);
			model.addAttribute("keyword", keyword);

			return "admin/categories";
		}

		if (categoriesService.existsById(item.getId())) {
			return "redirect:/admin/categories";
		}

		categoriesService.create(item);
		return "redirect:/admin/categories";
	}

	// 👉 UPDATE
	@PostMapping("/admin/categories/update")
	public String update(@Valid @ModelAttribute("item") Categories item, BindingResult result, Model model,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {

			Pageable pageable = PageRequest.of(page, 5);
			Page<Categories> pageResult;

			if (keyword != null && !keyword.trim().isEmpty()) {
				pageResult = categoriesService.search(keyword, pageable);
			} else {
				pageResult = categoriesService.findAll(pageable);
			}

			model.addAttribute("item", item);
			model.addAttribute("items", pageResult.getContent());
			model.addAttribute("page", pageResult); // 🔥 DÒNG QUYẾT ĐỊNH
			model.addAttribute("keyword", keyword);

			return "admin/categories";
		}

		categoriesService.update(item);
		redirectAttributes.addFlashAttribute("success", "Cập nhật loại sản phẩm thành công!");
		return "redirect:/admin/categories";
	}

	@RequestMapping("/admin/categories/delete/{id}")
	public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {

		categoriesService.deleteById(id);

		redirectAttributes.addFlashAttribute("success", "Xóa loại sản phẩm thành công!");

		return "redirect:/admin/categories";
	}

	// 👉 LOAD dữ liệu lên form để edit
	@RequestMapping("/admin/categories/edit/{id}")
	public String edit(Model model, @PathVariable("id") String id,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page) {

		model.addAttribute("item", categoriesService.findById(id));

		Pageable pageable = PageRequest.of(page, 5);
		Page<Categories> pageResult;

		if (keyword != null && !keyword.trim().isEmpty()) {
			pageResult = categoriesService.search(keyword, pageable);
		} else {
			pageResult = categoriesService.findAll(pageable);
		}

		model.addAttribute("items", pageResult.getContent());
		model.addAttribute("page", pageResult);
		model.addAttribute("keyword", keyword);

		return "admin/categories";
	}

}
