package poly.edu.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieService {
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;

	public Cookie createCookie(String name, String value, int days) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(days * 60 * 60);
		cookie.setPath("/");
		return cookie;
	}

	public Cookie getCookie(String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies) {
				if (cookie.getName().equalsIgnoreCase(name))
					return cookie;
			}
		return null;
	}

	// ✅ THÊM: gửi cookie về client
	public void addCookie(Cookie cookie) {
		response.addCookie(cookie);
	}

	// ✅ THÊM: xoá cookie
	public void removeCookie(String name) {
		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}