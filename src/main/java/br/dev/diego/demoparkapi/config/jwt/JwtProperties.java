package br.dev.diego.demoparkapi.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    @Value("${spring.jwt.prefix}")
    private String prefix;

    @Value("${spring.jwt.expire.days}")
    private long expireDays;

    @Value("${spring.jwt.expire.hours}")
    private long expireHours;

    @Value("${spring.jwt.expire.minutes}")
    private long expireMinutes;

    @Value("${spring.jwt.secret}")
    private String secret;

    public String getPrefix() {
        return prefix;
    }

    public long getExpireDays() {
        return expireDays;
    }

    public long getExpireHours() {
        return expireHours;
    }

    public long getExpireMinutes() {
        return expireMinutes;
    }

    public String getSecret() {
        return secret;
    }
}