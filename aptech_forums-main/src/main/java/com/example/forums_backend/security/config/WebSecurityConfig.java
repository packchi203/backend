package com.example.forums_backend.security.config;

import com.example.forums_backend.config.ApiAuthenticationFilter;
import com.example.forums_backend.config.ApiAuthorizationFilter;
import com.example.forums_backend.config.oauth2.CustomOAuth2UserService;
import com.example.forums_backend.config.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.forums_backend.config.oauth2.OAuth2AuthenticationFailureHandler;
import com.example.forums_backend.config.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.forums_backend.config.constant.route.AccountRoute.PREFIX_ACCOUNT_ROUTE;
import static com.example.forums_backend.config.constant.route.AdminRoute.*;
import static com.example.forums_backend.config.constant.route.AccountRoute.USER_INFO_BY_USERNAME_ROUTE;
import static com.example.forums_backend.config.constant.route.AuthRoute.LOGIN_ROUTE;
import static com.example.forums_backend.config.constant.route.AuthRoute.PREFIX_AUTH_ROUTE;
import static com.example.forums_backend.config.constant.route.ClientRoute.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ApiAuthenticationFilter apiAuthenticationFilter = new ApiAuthenticationFilter(authenticationManagerBean());
        apiAuthenticationFilter.setFilterProcessesUrl(LOGIN_ROUTE);
        http.cors().and().csrf().disable();
        //route dành cho user không cần đang nhập
        http
                .authorizeRequests()
                .antMatchers(
                        "/",
                        PREFIX_AUTH_ROUTE.concat("/**"),
                        PREFIX_ACCOUNT_ROUTE.concat("/**"),
                        POSTS_CLIENT_ROUTE.concat("/**"),
                        TAG_CLIENT_ROUTE.concat("/**"),
                        POST_COMMENTS_ROUTE_ANT_MATCHES.concat("/**"),
                        POST_DETAILS_ROUTE_ANT_MATCHES.concat("/**")
                )
                .permitAll();
        //route quyền truy cập danh cho user đã đang nhập
        http
                .authorizeRequests()
                .antMatchers(
                        COMMENT_POST_ROUTE_ANT_MATCHES.concat("/**"),
                        POST_VOTE_ROUTE_ANT_MATCHES.concat("/**"),
                        COMMENT_VOTE_ROUTE_ANT_MATCHES.concat("/**"),
                        POST_CLIENT_CREATE_POST_ROUTE.concat("/**"),
                        TAG_FOLLOW_CLIENT_ROUTE.concat("/**")
                )
                .hasAnyAuthority("USER", "ADMIN");
        //route quyền truy cập dành cho admin
        http
                .authorizeRequests()
                .antMatchers("/api/admin/**")
                .hasAnyAuthority("ADMIN");
        http
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService).and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);
        http
                .addFilter(apiAuthenticationFilter);
        http
                .addFilterBefore(new ApiAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
