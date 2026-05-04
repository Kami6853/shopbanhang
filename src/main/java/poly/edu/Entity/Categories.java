package poly.edu.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "Categories")
public class Categories {
	@Id
	@NotBlank(message = "Mã loại không được để trống")
	@Size(max = 4, message = "Mã loại tối đa 4 ký tự")
	String id;

	@Column(columnDefinition = "nvarchar(50)")
	@NotBlank(message = "Tên loại không được để trống")
	@Size(max = 50, message = "Tên loại tối đa 50 ký tự")
	String name;
}
