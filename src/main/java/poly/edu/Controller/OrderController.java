package poly.edu.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import poly.edu.Entity.Accounts;
import poly.edu.Entity.OrderDetails;
import poly.edu.Entity.Orders;
import poly.edu.Repository.AccountsRepository;
import poly.edu.Repository.OrderDetailsRepository;
import poly.edu.Repository.OrdersRepository;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	OrdersRepository ordersRepository;

	@Autowired
	OrderDetailsRepository orderDetailsRepo;

	@Autowired
	AccountsRepository accountsRepo;

	@Autowired
	HttpSession session;

	@GetMapping("/my")
	public String myOrders(Model model) {

		Accounts accSession = (Accounts) session.getAttribute("user");
		if (accSession == null) {
			return "redirect:/auth/login";
		}

		// 🔥 LOAD LẠI ACCOUNT TỪ DB
		Accounts acc = accountsRepo.findById(accSession.getUsername()).orElse(null);

		if (acc == null) {
			return "redirect:/auth/login";
		}

		// ✅ GỌI ĐÚNG METHOD
		var orders = ordersRepository.findByAccounts_Username(acc.getUsername());

		Map<Long, List<OrderDetails>> orderDetailsMap = new HashMap<>();
		for (Orders o : orders) {
			orderDetailsMap.put(o.getId(), orderDetailsRepo.findByOrders_Id(o.getId()));
		}

		model.addAttribute("orders", orders);
		model.addAttribute("orderDetailsMap", orderDetailsMap);

		return "pages/order-list";
	}

}