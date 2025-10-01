package com.example.techgicus_ebilling.techgicus_ebilling.security;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Role;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration-milliseconds}")
    private int jwtExpirationMs;


    // generated access token (1 hr)
    public String generateAccessToken(User user){


        //Jwt claims only accept primitive type (Like String,integer,Long, etc) not except the
        // and list or set of primitive data type but user role is custom (user defined ) data type.
        // so we convert it Set of String which is primitive data type
        Set<String> roleNames = user.getRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());


        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role",roleNames)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+jwtExpirationMs))
                .signWith(getKey(),SignatureAlgorithm.HS256)
                .compact();
                
    }


    // generate refresh token (7 days)
    public String generateRefreshToken(User user){
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("type","refresh")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60*24*7))
                .signWith(getKey(),SignatureAlgorithm.HS256)
                .compact();

    }


    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    public Claims extractAllClaims(String token){
       return Jwts.parser()
               .verifyWith(getKey())
               .build()
               .parseSignedClaims(token)
               .getPayload();
    }

    public String extractEmailId(String token){
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token){
        return extractAllClaims(token).get("role",String.class);
    }

    public boolean isRefreshToken(String token){
        return "refresh".equals(extractAllClaims(token).get("type",String.class));
    }

    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }



}
