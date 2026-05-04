package poly.edu.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import poly.edu.Entity.Accounts;
import poly.edu.Repository.AccountsRepository;
import poly.edu.Service.AccountsService;
import poly.edu.Service.CookieService;
import poly.edu.Service.UploadService;

@Controller
public class AuthController {
	@Autowired
	UploadService uploadService;
	@Autowired
	CookieService cookieService;
	@Autowired
	AccountsRepository accountsRepo;
	@Autowired
	AccountsService accountsService;

	@PostMapping("/auth/login")
	public String login(@RequestParam("email") String input, @RequestParam("password") String password,
			@RequestParam(value = "remember", required = false) String remember, HttpSession session) {

		// 🔹 login bằng email HOẶC username
		Accounts acc = accountsRepo.login(input, password);

		if (acc == null || !acc.getActivated()) {
			return "redirect:/auth/login?error";
		}

		// ==========================
		// ✅ SESSION CHUẨN
		// ==========================
		session.setAttribute("user", acc); // object Accounts
		session.setAttribute("username", acc.getUsername()); // username gốc
		session.setAttribute("admin", acc.getAdmin());

		// ==========================
		// ✅ REMEMBER ME
		// ==========================
		if (remember != null) {
			cookieService.addCookie(cookieService.createCookie("username", acc.getUsername(), 7));
		} else {
			cookieService.removeCookie("username");
		}

		return "redirect:/home";
	}

	@PostMapping("/account/sign-up")
	public String signUp(@Valid @ModelAttribute("item") Accounts acc, BindingResult result,
			@RequestParam("imageFile") MultipartFile file, Model model) {
		if (result.hasErrors()) {
			return "pages/sign-up";
		}

		// FIX QUAN TRỌNG 👇
		acc.setActivated(true); // tài khoản hoạt động
		acc.setAdmin(false); // user thường

		accountsService.create(acc);
		return "redirect:/auth/login";
	}

	@GetMapping("/auth/logout")
	public String logout(HttpSession session) {
		// Xóa toàn bộ session
		session.invalidate();

		// Xóa cookie remember me (nếu có)
		cookieService.removeCookie("username");

		// Quay về trang đăng nhập
		return "redirect:/auth/login";
	}

}
