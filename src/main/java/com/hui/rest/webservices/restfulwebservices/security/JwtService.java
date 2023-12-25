package com.hui.rest.webservices.restfulwebservices.security;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY = "cap9oWOVv6er2gx2GtbuTV7Q58nh6Y8HOeL5amzsextjZyaGIhAFmUGPcpxOyDwalAA59D2dPcc2Fnc7MK0HHG//CBOXiNzcUB+e5QZtFtCEUWU2uQApsC9mVL7QXoLREYIuAXmGWvr48ZLSTWNB/DdaQi9hcTO5c2M0QRjjzilScDu/rlR41eAUl533VSbtDTcfAafaqtiiwKyHdRWmYNNyyVEt081OB5MJcKt7+11fJgHP1BMzvefqW6RRc/nXuys3FkSFwzGNGiYLGtFmAUWDD5gbbPk3PA5j7MBuQSNOF904XkK2JxauF1Gpv39l2acwd5R7b4nRKL5+tjnJjqeJDawuBB5uW2PCHdlR/uY=";
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keBytes) ;
    }

}
