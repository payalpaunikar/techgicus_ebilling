package com.example.techgicus_ebilling.techgicus_ebilling.filter;


import com.example.techgicus_ebilling.techgicus_ebilling.security.MyUserDetails;
import com.example.techgicus_ebilling.techgicus_ebilling.service.UserSubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SubscriptionFilter extends OncePerRequestFilter {

     private final UserSubscriptionService userSubscriptionService;

     private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionFilter.class);

     @Autowired
    public SubscriptionFilter(UserSubscriptionService userSubscriptionService) {
        this.userSubscriptionService = userSubscriptionService;
    }


    // Endpoints that should skip subscription check
    private static final List<String> SKIP_SUBSCRIPTION_CHECK = List.of(
            "/auth/**",
            "/swagger/**",
            "/v3/api-docs/**",
            "/get/subscription-plan",
            "/buy/subscription-plan",
            "/user/{userId}/subscription/isActive",
            "/user/{userId}/current-plan",
            "/user/{userId}/current-plan/available-plan",
            "/user/{userId}/update-plan",
            "/get/subscription-plan/{subscriptionPlanId}"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


         logger.info("inside the subscription filter");

         String path = request.getRequestURI();

        // ✅ Skip subscription check for auth, docs, and subscription purchase endpoints
//        if (path.startsWith("/auth") ||
//                path.startsWith("/swagger") ||
//                path.startsWith("/v3/api-docs") ||
//               // path.startsWith("/subscription/purchase") ||
//              //  path.startsWith("/subscription/renew"))
//                path.startsWith("/get/subscription-plan") ||
//                path.startsWith("/buy/subscription-plan") ||
//                path.startsWith("/user/{userId}/subscription/isActive") ||
//                path.startsWith("/user/{userId}/current-plan") ||
//                path.startsWith("/user/{userId}/current-plan/available-plan") ||
//                path.startsWith("/user/{userId}/update-plan")) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        for (String pattern :SKIP_SUBSCRIPTION_CHECK){
            if (pathMatcher.match(pattern,path)){
                filterChain.doFilter(request,response);
                return;
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication !=null && authentication.isAuthenticated()){
            Long userId = ((MyUserDetails)authentication.getPrincipal()).getUser().getUserId();

            // Validate subscription
            if (!userSubscriptionService.isUserSubscriptionActive(userId)){
                response.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
                response.setContentType("application/json");

                Map<String,Object> newResponse = new HashMap<>();
                newResponse.put("timestamp", LocalDateTime.now().toString());
                newResponse.put("status",HttpServletResponse.SC_PAYMENT_REQUIRED);
                newResponse.put("message", "Active subsscription required");

//                response.getWriter().write("Active subscription required.");

                // Convert map to JSON and write it to response
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getWriter(), newResponse);
                return;
            }

            // ✅ Validate limits (example: company creation limit)
            if (path.startsWith("/create/company/by/owner/")) {
                logger.info("the path is match");
                if (!userSubscriptionService.canUserCreateCompany(userId)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");

                    Map<String,Object> newResponse = new HashMap<>();
                    newResponse.put("timestamp", LocalDateTime.now().toString());
                    newResponse.put("status",HttpServletResponse.SC_FORBIDDEN);
                    newResponse.put("message", "Company creation limit reached for your plan.");

                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(response.getWriter(),newResponse);
                    return;
                }
            }
//
//            // ✅ Validate other feature access (backup, billing, etc.)
//            if (path.startsWith("/api/backup")) {
//                if (!subscriptionService.canUseBackup(userId)) {
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                    response.getWriter().write("Backup not available in your plan.");
//                    return;
//                }
//            }
        }

        filterChain.doFilter(request,response);
    }
}
