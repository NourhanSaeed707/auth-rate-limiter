package com.example.authentication.Service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private static final String secretKey = "ced88ad9c80d3b432fb7872d2b6e1791dda1614cc60093aacae098369aede4bf5a0c05cd08e4d8df64f05b9353dbaa37708497dc1ceb4e48b0f4d7b0b09f4ac98abb36ebfe41d4baf4dd2b3108eacbe346089ce7dfa9a554c25c86cd2ed95e0871e130ac7c7d9c8c961e0e84af626c2052e6f89e8a67cc1f6c2fa8c40856463bf5b2fbd0a156bffc20685f9c16ff839ae9f362601f3e5d050a07a0c7058b5410465763c88cf44ff4657fb655652aa040f8e52849c82b787d050f83a0ca6088049a8afd9dea7d4612bfc33407b65027eb0f25d0d7b0378df7edc61db209ba31ad54492dd05b7970bbdf67dc3234403605bacbdd3c0fa6b3fc7534e814e9a023bf";
    private final long jwtExpiration = 86400000; // 1 day.

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "JWT token has expired");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("JWT token is unsupported");
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("JWT token is malformed");
        } catch (SignatureException e) {
            throw new SignatureException("JWT signature does not match");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT token is illegal");
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
