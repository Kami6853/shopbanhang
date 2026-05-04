package poly.edu.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import poly.edu.Entity.Item;
import poly.edu.Entity.Products;
import poly.edu.Repository.ProductsRepository;

@SessionScope
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	Map<Integer, Item> items = new HashMap<>();

	@Autowired
	ProductsRepository productsRepository; // lấy từ SQL

	@Override
	public Collection<Item> getItems() {
		return items.values();
	}

	// Thêm 1 sản phẩm
	@Override
	public Item add(Integer id) {
		add(id, 1);
		return items.get(id);
	}

	// Thêm với số lượng
	@Override
	public void add(Integer id, Integer qty) {

	    Products product = productsRepository.findById(id).orElse(null);

	    if (product == null)
	        return;

	    Item cartItem = items.get(id);

	    if (cartItem == null) {

	        cartItem = new Item(
	                product.getId(),
	                product.getName(),
	                product.getPrice(),
	                qty,
	                product.getImage()   // ✅ FIX
	        );

	        items.put(id, cartItem);

	    } else {
	        cartItem.setQty(cartItem.getQty() + qty);
	    }
	}

	@Override
	public Item update(Integer id, Integer qty) {
		Item item = items.get(id);
		if (item != null) {
			item.setQty(qty);
		}
		return item;
	}

	@Override
	public void remove(Integer id) {
		items.remove(id);
	}

	@Override
	public void clear() {
		items.clear();
	}

	@Override
	public Integer getCount() {
		int count = 0;
		for (Item item : items.values()) {
			count += item.getQty();
		}
		return count;
	}

	@Override
	public Double getAmount() {
		double amount = 0;
		for (Item item : items.values()) {
			amount += item.getQty() * item.getPrice();
		}
		return amount;
	}
}
