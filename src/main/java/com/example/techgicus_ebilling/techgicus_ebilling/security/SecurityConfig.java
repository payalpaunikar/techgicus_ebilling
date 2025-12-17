package com.example.techgicus_ebilling.techgicus_ebilling.security;


import com.example.techgicus_ebilling.techgicus_ebilling.component.JwtAuthenticationEntryPoint;
import com.example.techgicus_ebilling.techgicus_ebilling.filter.SubscriptionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

      @Autowired
      private JWTAuthenticationFilter jwtAuthenticationFilter;

      @Autowired
      private MyUserDetailsService myUserDetailsService;

      @Autowired
      private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

//      @Autowired
//      private SubscriptionFilter subscriptionFilter;



      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
         return http
                 .cors(Customizer.withDefaults())
                 .csrf(csrf-> csrf.disable())
                 .authorizeHttpRequests(request->request
                         .requestMatchers("/auth/login","/test","/auth/register/admin",
                                 "/get/access-token/by/refresh-token","/forgot-password",
                                 "/v1/api/**",
                                 "/v2/api-docs",
                                 "/v3/api-docs",
                                 "/v3/api-docs/**",
                                 "/swagger-resources",
                                 "/swagger-resources/**",
                                 "/configuration/ui",
                                 "/configuration/security",
                                 "/swagger-ui/**",
                                 "/webjars/**",
                                 "/swagger-ui.html").permitAll()
                         .anyRequest().authenticated())
                 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // .addFilterAfter(subscriptionFilter,JWTAuthenticationFilter.class)
                 .exceptionHandling(ex-> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                 .sessionManagement(session-> session
                         .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .build();

      }

      @Bean
      public PasswordEncoder passwordEncoder(){
          return new BCryptPasswordEncoder();
      }

      @Bean
      public DaoAuthenticationProvider daoAuthenticationProvider(){
          DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
          daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
          daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
          return daoAuthenticationProvider;
      }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3001"));  // Allow specific origins or use "*"
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "x-auth-token", "Accept"));  // Allowed headers
        configuration.setExposedHeaders(Arrays.asList("Authorization", "x-auth-token"));  // Expose headers to the client
        configuration.setAllowCredentials(true);  // Allow credentials (cookies, authorization headers, etc.)
        configuration.setMaxAge(3600L);  // Cache preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Apply CORS configuration to all endpoints
        return source;
    }
}
