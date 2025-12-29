//package in.krish.jwtUtils;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Component
//public class JwtUtil {
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private long jwtExpirationInMs;
//
//    @Value("${jwt.refreshExpiration}")
//    private long refreshExpirationInMs;
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//    }
//
//    // ============================================================
//    // TOKEN GENERATION — store EMAIL in subject
//    // ============================================================
//
//    public String generateToken(String email) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, email, jwtExpirationInMs);
//    }
//
//    public String generateRefreshToken(String email) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, email, refreshExpirationInMs);
//    }
//
//    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject) // EMAIL stored here
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
//                .compact();
//    }
//
//    // ============================================================
//    // TOKEN VALIDATION
//    // ============================================================
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            System.err.println("Invalid JWT: " + e.getMessage());
//            return false;
//        }
//    }
//
//    // ============================================================
//    // CLAIM EXTRACTION
//    // ============================================================
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject); // EMAIL returned
//    }
//
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    // ============================================================
//    // READ TOKEN FROM REQUEST
//    // ============================================================
//
//    public String extractToken(javax.servlet.http.HttpServletRequest request) {
//        String header = request.getHeader("Authorization");
//        if (header != null && header.startsWith("Bearer ")) {
//            return header.substring(7);
//        }
//        return null;
//    }
//}
