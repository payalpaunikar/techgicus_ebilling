package com.example.techgicus_ebilling.techgicus_ebilling.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

     @Autowired
     private JwtService jwtService;

     @Autowired
     private MyUserDetailsService myUserDetailsService;

     private final static Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String emailId = null;

        if (authHeader !=null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            try {
                emailId = jwtService.extractEmailId(token);

               // logger.info("The extract emailId is : "+emailId);
            }
            catch (Exception exception){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
              //  response.getWriter().write("Invalid token : "+exception.getMessage());
                Map<String,Object> errorResponse = new HashMap<>();
                errorResponse.put("timestamp", LocalDateTime.now().toString());
                errorResponse.put("status",HttpServletResponse.SC_UNAUTHORIZED);
                errorResponse.put("error","Unauthorized");
                errorResponse.put("message", "Invalid token : "+exception.getMessage());

                // Convert map to JSON and write it to response
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getWriter(), errorResponse);
               // logger.error("The extract emailId is not correct : "+exception);
                return;
            }
        }

        if (emailId != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = myUserDetailsService.loadUserByUsername(emailId);

           // logger.info("userDetails username is : "+userDetails.getUsername());
           // logger.info("authority : "+userDetails.getAuthorities());

            // check if token is valid
            if (userDetails.getUsername().equals(emailId)  && !jwtService.isTokenExpired(token)){

             //   logger.info("The token is valid");

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

       // logger.info("the reuest is done ");
        filterChain.doFilter(request,response);
    }
}
