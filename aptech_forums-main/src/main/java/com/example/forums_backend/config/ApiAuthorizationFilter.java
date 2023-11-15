package com.example.forums_backend.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.forums_backend.utils.JwtUtil;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.example.forums_backend.config.constant.route.AccountRoute.*;
import static com.example.forums_backend.config.constant.route.AuthRoute.*;
import static com.example.forums_backend.config.constant.route.ClientRoute.*;

public class ApiAuthorizationFilter extends OncePerRequestFilter {
    private static final String[] IGNORE_PATHS = {
            "/",
            LOGIN_ROUTE,
            REGISTER_ROUTE,
            GET_OTP_ROUTE,
            LOGIN_WITH_EMAIL_ROUTE,
            USER_INFO_BY_USERNAME_ROUTE,
            USER_BADGE_ROUTE,
            USER_COMMENTS_ROUTE_ANT_MATCHES,
            USER_POSTS_ROUTE_ANT_MATCHES,
            DETAILS_COMMENT_ROUTE,
            USER_CONTACT_POST_ROUTE,
            POSTS_CLIENT_ROUTE,
            TAG_CLIENT_ROUTE,
            POST_COMMENTS_ROUTE_ANT_MATCHES,
            POST_DETAILS_ROUTE_ANT_MATCHES,
            POSTS_NOT_SORT_CLIENT_ROUTE,
            POSTS_BY_TAG_ROUTE,
            "/api/tags/popular",
            "/api/posts/popular",
            "/api/users/famous",
            "/api/tag/\\S*/details",
            "/post/\\S*/static/details",
            "/api/search"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getServletPath();
            for(String element : Arrays.asList(IGNORE_PATHS)) {
                if (requestPath.matches(element)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        if (Arrays.asList(IGNORE_PATHS).contains(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Authorization header is required");
            return;
        }
        try {
            String token = authorizationHeader.replace("Bearer", "").trim();
            DecodedJWT decodedJWT = JwtUtil.getDecodedJwt(token);
            String username = decodedJWT.getSubject();
            String role = decodedJWT.getClaim(JwtUtil.ROLE_CLAIM_KEY).asString();
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Map<String, String> errors = new HashMap<>();
            errors.put("error", ex.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().println(new Gson().toJson(errors));
        }
    }

}
