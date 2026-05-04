package poly.edu.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "Accounts")
public class Accounts {
	@Id
	@NotBlank(message = "Username không được để trống")
	String username;

	@NotBlank(message = "Mật khẩu không được để trống")
	@Column(nullable = false, length = 50)
	String password;

	@NotBlank(message = "Họ tên không được để trống")
	@Column(columnDefinition = "nvarchar(50)")
	String fullname;

	@NotBlank(message = "Email không được để trống")
	@Email(message = "Email không đúng định dạng")
	String email;

	String photo;

	Boolean activated;

	Boolean admin;
}
