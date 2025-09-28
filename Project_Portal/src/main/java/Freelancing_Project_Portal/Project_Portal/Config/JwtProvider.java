package Freelancing_Project_Portal.Project_Portal.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtProvider {
    private SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    public String generateToken(Authentication auth){
        String authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        return Jwts.builder().setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .claim("email",auth.getName())
                .claim("authorities",authorities)
                .signWith(key)
                .compact();
    }

    public String getEmailfromToken(String token){
        token = token.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return String.valueOf(claims.get("email"));

    }

    public boolean validateToken(String token){
        try{
            token = token.substring(7);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            return true;
        }catch (Exception e){
            return false;
        }
    }
}
