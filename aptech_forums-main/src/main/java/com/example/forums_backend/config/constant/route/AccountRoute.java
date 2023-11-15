package com.example.forums_backend.config.constant.route;

public class AccountRoute {
    public static final String PREFIX_ACCOUNT_ROUTE = "/api";
    public static final String DELETE_PATH = "/me/delete-profile";
    public static final String UPDATE_PATH = "/me/update-profile";

    public static final String USER_INFO_BY_USERNAME = "/user/{username}/info";
    public static final String USER_INFO_BY_USERNAME_PATH_ANT_MATCHES = "/user/\\S*/info";
    public static final String USER_INFO_BY_USERNAME_ROUTE = PREFIX_ACCOUNT_ROUTE.concat(USER_INFO_BY_USERNAME_PATH_ANT_MATCHES);
    public static final String UPDATE_ACCOUNT_ROUTE = PREFIX_ACCOUNT_ROUTE.concat(UPDATE_PATH);
    public static final String DELETE_ACCOUNT_ROUTE = PREFIX_ACCOUNT_ROUTE.concat(DELETE_PATH);
}
