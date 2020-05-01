package com.snrt.security.jwt;

public class JWTSecurityParams {
    public static final String JWT_HEADER_NAME="Authorization";
    public static final String SECRET="snrt_music";
    public static final long EXPIRATION=86400; // convert 10d to sec 10*24*3600
    public static final String HEADER_PREFIX="Bearer ";
}
