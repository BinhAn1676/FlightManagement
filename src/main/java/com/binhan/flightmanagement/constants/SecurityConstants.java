package com.binhan.flightmanagement.constants;

public class SecurityConstants {
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 3600000;//1h
    //public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 30000;
    public static final String JWT_SECRET = "nguyenbinhan123456789";
    public static final long JWT_EXPIRATION = 3600000;
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SIGNING_KEY = "binhan";
    public static final String AUTHORITIES_KEY = "scopes";
}
