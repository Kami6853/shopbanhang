package poly.edu.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import poly.edu.Entity.Categories;
import poly.edu.Service.CategoriesService;
import poly.edu.Service.ShoppingCartService;

@ControllerAdvice
public class GlobalController {

	@Autowired
	CategoriesService categoriesService;

	@Autowired
	ShoppingCartService cart;

	@ModelAttribute("menuCategories")
	public List<Categories> menuCategories() {
		return categoriesService.findAll();
	}

	@ModelAttribute("cart")
	public ShoppingCartService cart() {
		return cart;
	}
}
