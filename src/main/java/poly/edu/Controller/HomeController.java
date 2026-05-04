package poly.edu.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import poly.edu.Entity.Accounts;
import poly.edu.Entity.Products;
import poly.edu.Service.AccountsService;
import poly.edu.Service.CookieService;
import poly.edu.Service.ProductsService;

@Controller
public class HomeController {
	@Autowired
	AccountsService accountsService;
	@Autowired
	ProductsService productsService;
	@Autowired
	CookieService cookieService;

	// ================= TRANG CHỦ =================
	@GetMapping({ "/home" })
	public String home() {
		return "pages/index";
	}

	// ================= SẢN PHẨM =================
//	@GetMapping("/products/list")
//	public String productsList(Model model) {
//		model.addAttribute("items", productsService.findAll());
//		return "pages/products-list";
//	}
	@GetMapping("/products/list")
	public String productsList(Model model, @RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "minPrice", required = false) Double minPrice,
			@RequestParam(name = "maxPrice", required = false) Double maxPrice,
			@RequestParam(name = "p", defaultValue = "0") int p) {

		Pageable pageable = PageRequest.of(p, 16);

		Page<Products> page = productsService.searchByNameAndPrice(keyword, minPrice, maxPrice, pageable);

		model.addAttribute("page", page);
		model.addAttribute("items", page.getContent());

		// giữ lại giá trị trên form
		model.addAttribute("keyword", keyword);
		model.addAttribute("minPrice", minPrice);
		model.addAttribute("maxPrice", maxPrice);

		return "pages/products-list";
	}

	@GetMapping("/products/category/{id}")
	public String productsByCategory(@PathVariable("id") String categoryId,
			@RequestParam(name = "p", defaultValue = "0") int p, Model model) {

		Pageable pageable = PageRequest.of(p, 8);

		Page<Products> page = productsService.findByCategory(categoryId, pageable);

		model.addAttribute("items", page.getContent());
		model.addAttribute("page", page);
		model.addAttribute("categoryId", categoryId);

		return "pages/products-list";
	}

	// ================= THỐNG KÊ =================
	@GetMapping("/admin/report")
	public String inventoryByCategory(Model model) {

		model.addAttribute("items", productsService.inventoryByCategory());

		return "admin/report";
	}
//	@GetMapping("/product/list-by-category/{id}")
//	public String productByCategory(@PathVariable("id") Integer id) {
//		return "pages/product-list";
//	}
//
//	@GetMapping("/product/detail/{id}")
//	public String productDetail(@PathVariable("id") Integer id) {
//		return "pages/product-detail";
//	}

//	@GetMapping("/cart/add/{id}")
//	public String cartAdd(@PathVariable("id") Integer id) {
//		return "pages/cart";
//	}
//
//	@GetMapping("/cart/remove/{id}")
//	public String cartRemove(@PathVariable("id") Integer id) {
//		return "pages/cart";
//	}

	// ================= ĐƠN HÀNG =================
	@GetMapping("/order/checkout")
	public String checkout() {
		return "pages/check-out";
	}

//	@GetMapping("/order/list")
//	public String orderList() {
//		return "pages/order-list";
//	}

	@GetMapping("/order/list")
	public String orderList() {
		return "forward:/order/my";
	}

	@GetMapping("/order/detail/{id}")
	public String orderDetail(@PathVariable("id") Integer id) {
		return "pages/order-detail";
	}

	// ================= TÀI KHOẢN =================
	@GetMapping("/auth/login")
	public String loginForm(Model model) {
		Cookie cookie = cookieService.getCookie("username");
		if (cookie != null) {
			model.addAttribute("email", cookie.getValue());
		}
		return "pages/login";
	}

	@GetMapping("/account/sign-up")
	public String signUp(Model model) {
		model.addAttribute("item", new Accounts());
		return "pages/sign-up";
	}

	@GetMapping("/account/edit-profile")
	public String editProfile(Model model, HttpSession session) {

		Accounts acc = (Accounts) session.getAttribute("user");

		if (acc == null) {
			return "redirect:/auth/login";
		}

		model.addAttribute("item", acc);
		return "pages/edit-profile";
	}

}
