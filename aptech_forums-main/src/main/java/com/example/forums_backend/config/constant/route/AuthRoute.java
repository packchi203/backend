package com.example.forums_backend.config.constant.route;

public class AuthRoute {
    public static final String PREFIX_AUTH_ROUTE = "/api";

    public static final String LOGIN_PATH = "/login";
    public static final String LOGIN_WITH_EMAIL_PATH = "/login/email-login";
    public static final String GET_OTP_PATH = "/login/get-otp";
    public static final String REGISTER_PATH = "/register";
    public static final String USER_INFO_PATH = "/me/info";
    public static final String MY_PROFILE = "/my/profile";

    public static final String LOGIN_ROUTE = PREFIX_AUTH_ROUTE.concat(LOGIN_PATH);
    public static final String LOGIN_WITH_EMAIL_ROUTE = PREFIX_AUTH_ROUTE.concat(LOGIN_WITH_EMAIL_PATH);
    public static final String GET_OTP_ROUTE = PREFIX_AUTH_ROUTE.concat(GET_OTP_PATH);
    public static final String REGISTER_ROUTE = PREFIX_AUTH_ROUTE.concat(REGISTER_PATH);
    public static final String USER_INFO_ROUTE = PREFIX_AUTH_ROUTE.concat(USER_INFO_PATH);
}
