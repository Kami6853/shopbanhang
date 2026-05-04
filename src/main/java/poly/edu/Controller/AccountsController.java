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
import poly.edu.Entity.Accounts;
import poly.edu.Service.AccountsService;
import poly.edu.Service.UploadService;

@Controller
public class AccountsController {
	@Autowired
	AccountsService accountsService;

	@Autowired
	UploadService uploadService;

	@RequestMapping("/admin/accounts")
	public String index(Model model, @RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page) {

		int size = 8; // số dòng mỗi trang
		Pageable pageable = PageRequest.of(page, size);

		Page<Accounts> pageResult;

		if (keyword != null && !keyword.trim().isEmpty()) {
			pageResult = accountsService.search(keyword, pageable);
		} else {
			pageResult = accountsService.findAll(pageable);
		}

		model.addAttribute("item", new Accounts());
		model.addAttribute("items", pageResult.getContent());
		model.addAttribute("page", pageResult);
		model.addAttribute("keyword", keyword);

		return "admin/accounts";
	}

	@PostMapping("/admin/accounts/create")
	public String create(@Valid @ModelAttribute("item") Accounts item, BindingResult result,
			@RequestParam("imageFile") MultipartFile imageFile, Model model) {

		Pageable pageable = PageRequest.of(0, 8);
		Page<Accounts> pageResult = accountsService.findAll(pageable);

		// ⭐ nếu validate lỗi
		if (result.hasErrors()) {
			model.addAttribute("items", pageResult.getContent());
			model.addAttribute("page", pageResult); // BẮT BUỘC (tránh lỗi page.number)
			model.addAttribute("keyword", null);
			return "admin/accounts";
		}

		// upload ảnh nếu có
		if (!imageFile.isEmpty()) {
			File file = uploadService.save(imageFile, "/image");
			item.setPhoto(file.getName());
		}

		// kiểm tra trùng username
		if (accountsService.existsById(item.getUsername())) {
			result.rejectValue("username", "error.username", "Username đã tồn tại");
			model.addAttribute("items", pageResult.getContent());
			model.addAttribute("page", pageResult);
			model.addAttribute("keyword", null);
			return "admin/accounts";
		}

		accountsService.create(item);
		return "redirect:/admin/accounts";
	}

	@PostMapping("/admin/accounts/update")
	public String update(@Valid @ModelAttribute("item") Accounts item, BindingResult result,
			@RequestParam("imageFile") MultipartFile imageFile,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			RedirectAttributes redirectAttributes) {

		Pageable pageable = PageRequest.of(page, 8);
		Page<Accounts> pageResult;

		if (keyword != null && !keyword.trim().isEmpty()) {
			pageResult = accountsService.search(keyword, pageable);
		} else {
			pageResult = accountsService.findAll(pageable);
		}

		// ⭐ nếu validate lỗi
		if (result.hasErrors()) {
			model.addAttribute("items", pageResult.getContent());
			model.addAttribute("page", pageResult);
			model.addAttribute("keyword", keyword);
			return "admin/accounts";
		}

		// nếu có chọn ảnh mới → up ảnh mới
		if (!imageFile.isEmpty()) {
			File file = uploadService.save(imageFile, "/image");
			item.setPhoto(file.getName());
		}
		// nếu không → giữ ảnh cũ
		else {
			Accounts old = accountsService.findById(item.getUsername());
			item.setPhoto(old.getPhoto());
		}

		accountsService.update(item);
		redirectAttributes.addFlashAttribute("success", "Cập nhật tài khoản thành công!");

		if (keyword != null && !keyword.trim().isEmpty()) {
			return "redirect:/admin/accounts?keyword=" + keyword + "&page=" + page;
		}

		return "redirect:/admin/accounts?page=" + page;
	}

	@RequestMapping("/admin/accounts/delete/{username}")
	public String delete(@PathVariable("username") String username) {
		accountsService.deleteById(username);
		return "redirect:/admin/accounts";
	}

	@RequestMapping("/admin/accounts/edit/{username}")
	public String edit(Model model, @PathVariable("username") String username,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page) {

		Accounts item = accountsService.findById(username);
		model.addAttribute("item", item);

		Pageable pageable = PageRequest.of(page, 8);
		Page<Accounts> pageResult;

		if (keyword != null && !keyword.trim().isEmpty()) {
			pageResult = accountsService.search(keyword, pageable);
		} else {
			pageResult = accountsService.findAll(pageable);
		}

		model.addAttribute("items", pageResult.getContent());
		model.addAttribute("page", pageResult);
		model.addAttribute("keyword", keyword);

		return "admin/accounts";
	}

	@PostMapping("/account/edit-profile")
	public String updateProfile(@ModelAttribute("item") Accounts acc,
			@RequestParam(required = false) String newPassword, @RequestParam(required = false) MultipartFile photoFile,
			Model model) {

		Accounts old = accountsService.findById(acc.getUsername());

		old.setFullname(acc.getFullname());
		old.setEmail(acc.getEmail());

		if (newPassword != null && !newPassword.isBlank()) {
			old.setPassword(newPassword);
		}

		if (photoFile != null && !photoFile.isEmpty()) {
			File file = uploadService.save(photoFile, "/image");
			old.setPhoto(file.getName());
		}

		accountsService.update(old);

		model.addAttribute("message", "Cập nhật thành công!");
		model.addAttribute("item", old);

		return "pages/edit-profile";
	}

}
