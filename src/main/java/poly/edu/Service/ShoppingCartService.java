package poly.edu.Service;

import java.util.Collection;

import poly.edu.Entity.Item;


public interface ShoppingCartService {

	/**
	 * Thêm 1 mặt hàng vào giỏ (mặc định số lượng = 1)
	 * 
	 * @param id mã mặt hàng
	 * @return mặt hàng đã được thêm
	 */
	Item add(Integer id);

	/**
	 * Thêm mặt hàng vào giỏ với số lượng xác định
	 * 
	 * @param id  mã mặt hàng
	 * @param qty số lượng thêm
	 */
	void add(Integer id, Integer qty);

	/**
	 * Xóa mặt hàng khỏi giỏ
	 * 
	 * @param id mã mặt hàng cần xóa
	 */
	void remove(Integer id);

	/**
	 * Cập nhật số lượng mặt hàng trong giỏ
	 * 
	 * @param id  mã mặt hàng
	 * @param qty số lượng mới
	 * @return mặt hàng sau khi cập nhật
	 */
	Item update(Integer id, Integer qty);

	/**
	 * Xóa toàn bộ mặt hàng trong giỏ
	 */
	void clear();

	/**
	 * Lấy danh sách mặt hàng trong giỏ
	 */
	 Collection<Item> getItems();

	/**
	 * Tổng số lượng sản phẩm trong giỏ
	 */
	Integer getCount();

	/**
	 * Tổng tiền giỏ hàng
	 */
	Double getAmount();
}
