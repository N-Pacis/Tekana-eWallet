package rw.pacis.tekanaewallet.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import rw.pacis.tekanaewallet.config.SecurityConfig;
import rw.pacis.tekanaewallet.repository.IUserRepository;
import rw.pacis.tekanaewallet.security.service.IJwtService;
import rw.pacis.tekanaewallet.services.impl.UserDetailServiceImpl;
import rw.pacis.tekanaewallet.utils.ApiResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final IJwtService jwtService;

	private final UserDetailServiceImpl userService;

	private final IUserRepository userRepository;

	private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
									@NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		final String requestUri = request.getRequestURI();
		final String authHeader = request.getHeader("Authorization");

		final String jwt;

		if (Arrays.stream(SecurityConfig.AUTH_WHITELIST).anyMatch(pattern -> PATH_MATCHER.match(pattern, requestUri))) {
			filterChain.doFilter(request, response);
			return;
		}

		if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);
		String userEmail = jwtService.extractUserName(jwt);

		if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userService.loadUserByUsername(userEmail);
			if (jwtService.isTokenValid(jwt, userDetails)) {
				UUID sessionId = jwtService.extractSessionId(jwt);
				UUID userId = UUID.fromString(jwtService.extractId(jwt));
				if (sessionId == null || userRepository.findBySessionIdAndId(sessionId, userId).isEmpty()) {
					unauthorizedResponse(response);
					return;
				}

				SecurityContext context = SecurityContextHolder.createEmptyContext();
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				context.setAuthentication(authToken);
				SecurityContextHolder.setContext(context);
			}
		}

		filterChain.doFilter(request, response);
	}

	private void unauthorizedResponse(HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ServletOutputStream out = response.getOutputStream();
		new ObjectMapper().writeValue(out, new ApiResponse<>("Invalid or missing auth token." +
				"",  (Object) "", HttpStatus.UNAUTHORIZED));
		out.flush();
	}
}