package com.example.forums_backend.config.constant.route;

public class ClientRoute {
    public static final String PREFIX_CLIENT_ROUTE = "/api";

    //posts router
    //PATH MAPPING CONTROLLER
    public static final String POSTS_CLIENT_PATH             = "/posts";
    public static final String POSTS_NOT_SORT_CLIENT_PATH    = "/posts/list";
    public static final String MY_POSTS_CLIENT_PATH          = "/my/posts";
    public static final String USER_POSTS_CLIENT_PATH        = "/user/{username}/posts";
    public static final String POST_CLIENT_CREATE_POST_PATH  = "/post/new";
    public static final String POST_CLIENT_DETAILS_POST_PATH = "/post/{slug}/details";

    public static final String POST_CLIENT_BOOKMARK_PATH = "/post/{id}/bookmark";
    public static final String POST_CLIENT_DELETE_PATH = "/post/{id}/delete";
    public static final String POST_CLIENT_UPDATE_PATH = "/post/{id}/update";

    public static final String GET_ALL_COMMENT_PATH       = "/comments";
    public static final String MY_COMMENTS_PATH           = "/my/comments";
    public static final String USER_COMMENTS_PATH         = "/user/{username}/comments";
    public static final String FIND_COMMENTS_BY_POST_PATH = "/post/{post_id}/comments";
    public static final String COMMENT_POST_PATH          = "/post/{id}/comment";
    public static final String POST_UP_VOTE_CLIENT_PATH   = "/post/{id}/vote";
    public static final String COMMENT_VOTE_PATH          = "/comment/{id}/vote";
    public static final String DETAILS_COMMENT_PATH = "/comment/{id}";
    public static final String DELETE_MY_COMMENT = "/comment/{id}/delete";
    public static final String UPDATE_MY_COMMENT = "/comment/{id}/update";

    //ANT MATCHES PATH
    public static final String POST_COMMENTS_PATH_ANT_MATCHES = "/post/\\S*/comments";
    public static final String POST_DETAILS_PATH_ANT_MATCHES  = "/post/\\S*/details";
    public static final String COMMENT_POST_PATH_ANT_MATCHES  = "/post/\\S*/comment";
    public static final String POST_VOTE_PATH_ANT_MATCHES     = "/post/\\S*/vote";
    public static final String COMMENT_VOTE_PATH_ANT_MATCHES  = "/comment/\\S*/vote";
    public static final String DETAILS_COMMENT_ANT_MATCHES = "/comment/\\S*/details";
    public static final String USER_COMMENTS_PATH_ANT_MATCHES = "/user/\\S*/comments";
    public static final String USER_POSTS_PATH_ANT_MATCHES = "/user/\\S*/posts";

    //ROUTER ANT MATCHES
    public static final String USER_COMMENTS_ROUTE_ANT_MATCHES = PREFIX_CLIENT_ROUTE.concat(USER_COMMENTS_PATH_ANT_MATCHES);
    public static final String USER_POSTS_ROUTE_ANT_MATCHES    = PREFIX_CLIENT_ROUTE.concat(USER_POSTS_PATH_ANT_MATCHES);
    public static final String POST_COMMENTS_ROUTE_ANT_MATCHES = PREFIX_CLIENT_ROUTE.concat(POST_COMMENTS_PATH_ANT_MATCHES);
    public static final String POST_DETAILS_ROUTE_ANT_MATCHES  = PREFIX_CLIENT_ROUTE.concat(POST_DETAILS_PATH_ANT_MATCHES);
    public static final String COMMENT_POST_ROUTE_ANT_MATCHES  = PREFIX_CLIENT_ROUTE.concat(COMMENT_POST_PATH_ANT_MATCHES);
    public static final String POST_VOTE_ROUTE_ANT_MATCHES     = PREFIX_CLIENT_ROUTE.concat(POST_VOTE_PATH_ANT_MATCHES);
    public static final String COMMENT_VOTE_ROUTE_ANT_MATCHES  = PREFIX_CLIENT_ROUTE.concat(COMMENT_VOTE_PATH_ANT_MATCHES);
    public static final String POSTS_CLIENT_ROUTE              = PREFIX_CLIENT_ROUTE.concat(POSTS_CLIENT_PATH);
    public static final String POSTS_NOT_SORT_CLIENT_ROUTE     = PREFIX_CLIENT_ROUTE.concat(POSTS_NOT_SORT_CLIENT_PATH);
    public static final String POST_CLIENT_CREATE_POST_ROUTE   = PREFIX_CLIENT_ROUTE.concat(POST_CLIENT_CREATE_POST_PATH);
    public static final String DETAILS_COMMENT_ROUTE = PREFIX_CLIENT_ROUTE.concat(DETAILS_COMMENT_ANT_MATCHES);
    //tags router
    public static final String TAG_CLIENT_PATH  = "/tags";
    public static final String TAG_FOLLOW_CLIENT_PATH  = "/tag/follow";
    public static final String MY_TAG_FOLLOWING = "/my/tags-following";

    public static final String POSTS_BY_TAG_ANT_MATCHES = "/filter/\\S*/posts-by-tag";
    public static final String POSTS_BY_TAG_ROUTE = PREFIX_CLIENT_ROUTE.concat(POSTS_BY_TAG_ANT_MATCHES);
    public static final String TAG_CLIENT_ROUTE = PREFIX_CLIENT_ROUTE.concat(TAG_CLIENT_PATH);
    public static final String TAG_FOLLOW_CLIENT_ROUTE = PREFIX_CLIENT_ROUTE.concat(TAG_FOLLOW_CLIENT_PATH);

    //Contact Route
    public static final String MY_CONTACT_PATH = "/my/contacts";
    public static final String USER_CONTACT_PATH = "/user/{username}/contacts";
    public static final String USER_CONTACT_PATH_ANT_MATCHES = "/user/\\S*/contacts";
    public static final String USER_CONTACT_POST_ROUTE   = PREFIX_CLIENT_ROUTE.concat(USER_CONTACT_PATH_ANT_MATCHES);

    //Badge
    public static final String MY_BADGE_PATH = "/my/badges";
    public static final String USER_BADGE_PATH = "/user/{username}/badges";
    public static final String USER_BADGE_PATH_ANT_MATCHES = "/user/badges/\\S*/badges";
    public static final String USER_BADGE_ROUTE   = PREFIX_CLIENT_ROUTE.concat(USER_BADGE_PATH_ANT_MATCHES);

}
