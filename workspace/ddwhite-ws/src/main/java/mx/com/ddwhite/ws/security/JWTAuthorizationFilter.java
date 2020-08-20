package mx.com.ddwhite.ws.security;

//import java.io.IOException;
import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;

//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;

//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.UnsupportedJwtException;

public class JWTAuthorizationFilter /*extends OncePerRequestFilter*/ {
	
//	private final String HEADER = "Authorization";
//	private static String PREFIX = "Bearer ";
//	private static final String AUTHORITIES = "authorities";
	private static final String SECRET_KEY = "ddWhiteKey";
	private static final String ID = "ddwhiteJWT";
	
	public static String getJWTToken(String username) {
		try {
//			List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
			String token = Jwts
					.builder()
					.setId(ID)
					.setSubject(username)
//					.claim(AUTHORITIES, grantedAuthorities.stream()
//							.map(GrantedAuthority::getAuthority)
//							.collect(Collectors.toList()))
					.setIssuedAt(new Date(System.currentTimeMillis() + 600000))
					.signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes()).compact();
			return token;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		try {
//			if(jwtTokenExists(request, response)) {
//				Claims claims = validateToken(request);
//				if(claims.get(AUTHORITIES) != null) {
//					setUpSpringAuthentication(claims);
//				} else {
//					SecurityContextHolder.clearContext();
//				}
//			} else {
//				SecurityContextHolder.clearContext();
//			}
//			filterChain.doFilter(request, response);
//		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
//			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
//			return;
//		}
//	}
	
//	private Claims validateToken(HttpServletRequest request) {
//		String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
//		return Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(jwtToken).getBody();
//	}
//	
//	private void setUpSpringAuthentication(Claims claims) {
//		@SuppressWarnings("unchecked")
//		List<String> authorities = (List<String>) claims.get(AUTHORITIES);
//		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
//				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//		SecurityContextHolder.getContext().setAuthentication(auth);
//	}
//	
//	private boolean jwtTokenExists(HttpServletRequest request, HttpServletResponse response) {
//		String authenticatorHeader = request.getHeader(HEADER);
//		if(authenticatorHeader == null || !authenticatorHeader.startsWith(PREFIX))
//			return false;
//		return true;
//	}
}
