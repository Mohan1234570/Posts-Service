//package in.krish.jwtUtils;
//
//import in.krish.configuration.CustomUserDetails;
//import in.krish.impl.CustomUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//
//    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
//
//    private static final String[] PUBLIC_ENDPOINTS = {
//            "/api/users/login",
//            "/api/users/register",
//            "/api/users/refresh",
//            "/uploads/**",
//            "/api/health"
//    };
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain chain)
//            throws ServletException, IOException {
//
//        String requestURI = request.getRequestURI();
//        String method = request.getMethod();
//        String header = request.getHeader("Authorization");
//
//        System.out.println("\n===========================");
//        System.out.println("🔹 Incoming request: " + method + " " + requestURI);
//        System.out.println("🔹 Authorization header: " + header);
//        System.out.println("===========================\n");
//
//        // ✅ Skip JWT validation for public endpoints
//        for (String path : PUBLIC_ENDPOINTS) {
//            if (pathMatcher.match(path, requestURI)) {
//                System.out.println("🟢 Public endpoint matched: " + path + " — skipping JWT validation.");
//                chain.doFilter(request, response);
//                return;
//            }
//        }
//
//        String jwt = null;
//        String username = null;
//
//        // ✅ Extract JWT token from header
//        if (header != null && header.startsWith("Bearer ")) {
//            jwt = header.substring(7);
//            try {
//                username = jwtUtil.extractUsername(jwt);
//                System.out.println(" Extracted username from JWT: " + username);
//            } catch (Exception e) {
//                System.out.println("❌ Failed to extract username from JWT: " + e.getMessage());
//            }
//        } else {
//            System.out.println("⚠️ No valid Bearer token found in Authorization header.");
//        }
//
//        // ✅ Authenticate user if token is valid
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            try {
//                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
//
//                // ✅ Use new JwtUtil single-arg validation
//                if (jwtUtil.validateToken(jwt)) {
//                    UsernamePasswordAuthenticationToken authenticationToken =
//                            new UsernamePasswordAuthenticationToken(
//                                    userDetails, null, userDetails.getAuthorities());
//                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                    System.out.println("JWT validated successfully for user: " + username);
//                } else {
//                    System.out.println("JWT validation failed for user: " + username);
//                }
//            } catch (Exception e) {
//                System.out.println(" Error while validating JWT: " + e.getMessage());
//            }
//        } else if (username == null) {
//            System.out.println("⚠ Skipping authentication setup since username is null or already authenticated.");
//        }
//
//        chain.doFilter(request, response);
//    }
//}
