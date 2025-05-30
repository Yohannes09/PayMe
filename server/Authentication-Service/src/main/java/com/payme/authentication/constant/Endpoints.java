package com.payme.authentication.constant;

public final class Endpoints {
    public static final String API_BASE = "/api/v1";

    private Endpoints(){}


    public static final class Auth {
        public static final String BASE = API_BASE + "/auth";

        public static final String LOGIN = "/login";
        public static final String REGISTER = "/register";
        public static final String TOKEN_REFRESH = "/refresh";
        public static final String LOGOUT = "/logout";
    }

    public static final class Users {
        public static final String BASE = API_BASE + "/users";

        public static final String UPDATE_USERNAME = "/username";
        public static final String UPDATE_EMAIL = "/email";
        public static final String UPDATE_PASSWORD = "/password";
    }

}
