package pl.edu.pg.gateway.user.dto;

public record PostTokenRefreshResponse(int status, String access, String refresh) { }
