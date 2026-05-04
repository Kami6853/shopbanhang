package poly.edu.Controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import poly.edu.Entity.Accounts;
import poly.edu.Entity.OrderDetails;
import poly.edu.Entity.Orders;
import poly.edu.Entity.Products;
import poly.edu.Repository.OrderDetailsRepository;
import poly.edu.Repository.OrdersRepository;
import poly.edu.Repository.ProductsRepository;
import poly.edu.Service.ShoppingCartService;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

	@Autowired
	ShoppingCartService cart;

	@Autowired
	OrdersRepository ordersRepository;

	@Autowired
	OrderDetailsRepository orderDetailsRepository;

	@Autowired
	ProductsRepository productsRepository;

	@Autowired
	HttpSession session;

	// ================= VIEW CART =================
	@GetMapping("")
	public String index(Model model) {
		model.addAttribute("cart", cart);
		return "pages/cart";
	}

	// ================= ADD =================
	@GetMapping("/add/{id}")
	public String add(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

		Accounts acc = (Accounts) session.getAttribute("user");

		if (acc == null) {
			session.setAttribute("redirectAfterLogin", "/cart/add/" + id);

			redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để mua sản phẩm!");

			return "redirect:/auth/login";
		}

		cart.add(id);

		redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng!");

		return "redirect:/cart";
	}

	// ================= UPDATE QTY =================
	@PostMapping("/update/{id}")
	public String update(@PathVariable Integer id, @RequestParam("qty") Integer qty) {
		cart.update(id, qty);
		return "redirect:/cart";
	}

	// ================= REMOVE =================
	@GetMapping("/remove/{id}")
	public String remove(@PathVariable Integer id) {
		cart.remove(id);
		return "redirect:/cart";
	}

	// ================= CLEAR =================
	@GetMapping("/clear")
	public String clear() {
		cart.clear();
		return "redirect:/cart";
	}

	// ================= PAY =================
	@GetMapping("/pay")
	public String pay(Model model) {

		Accounts acc = (Accounts) session.getAttribute("user");

		// ❌ chưa đăng nhập
		if (acc == null) {
			return "redirect:/auth/login";
		}

		// ❌ giỏ trống
		if (cart.getItems().isEmpty()) {
			model.addAttribute("message", "Giỏ hàng đang trống!");
			model.addAttribute("cart", cart);
			return "pages/cart";
		}

		Orders order = new Orders();
		order.setAccounts(acc);
		order.setCreateDate(LocalDate.now());
		order.setAddress("TP.HCM");

		ordersRepository.save(order);

		for (var item : cart.getItems()) {

			Products product = productsRepository.findById(item.getId()).orElse(null);

			if (product == null)
				continue;

			// 🔥 KIỂM TRA TỒN KHO
			if (product.getQuantity() < item.getQty()) {
				model.addAttribute("message", "Sản phẩm " + product.getName() + " không đủ số lượng trong kho!");
				model.addAttribute("cart", cart);
				return "pages/cart";
			}

			// 🔥 TRỪ KHO
			product.setQuantity(product.getQuantity() - item.getQty());

			// 🔥 NẾU HẾT HÀNG -> ẨN
			if (product.getQuantity() == 0) {
				product.setAvailable(false);
			}

			productsRepository.save(product);

			// 🔥 LƯU ORDER DETAIL
			OrderDetails d = new OrderDetails();
			d.setOrders(order);
			d.setProducts(product);
			d.setPrice(product.getPrice());
			d.setQuantity(item.getQty());

			orderDetailsRepository.save(d);
		}

		cart.clear();

		return "redirect:/order/my";
	}

}
