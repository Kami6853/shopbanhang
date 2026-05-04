package poly.edu.Entity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "Products")
public class Products {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@NotBlank(message = "Tên sản phẩm không được để trống")
	@Column(columnDefinition = "nvarchar(50)")
	String name;

	String image;

	@NotNull(message = "Giá không được để trống")
	@Positive(message = "Giá phải lớn hơn 0")
	Double price;

	@NotNull(message = "Số lượng không được để trống")
	@Min(value = 0, message = "Số lượng không được âm")
	Integer quantity;

	@NotNull(message = "Ngày tạo không được để trống")
	@Column(name = "CreateDate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate createDate = LocalDate.now();

	@NotNull(message = "Vui lòng chọn trạng thái")
	Boolean available;

	@NotNull(message = "Vui lòng chọn loại sản phẩm")
	@ManyToOne
	@JoinColumn(name = "CategoryId")
	Categories categories;

	@OneToMany(mappedBy = "products")
	List<OrderDetails> orderDetails;
}
