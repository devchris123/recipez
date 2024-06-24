package com.cwunder.recipe._config;

import java.util.HashSet;

import org.springframework.security.oauth2.jwt.Jwt;

import com.cwunder.recipe.user.CustomUserDetailsService;

/**
 * Utility class to construct a custom JwtAuthenticationToken
 */
public class JWTUtil {
    private CustomUserDetailsService userDetailsService;

    JWTUtil(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public JwtUser createJwtUser(Jwt jwt) {
        var userName = (String) jwt.getSubject();
        var user = (CustomUserDetailsService.RecipeUserDetails) userDetailsService.loadUserByUsername(userName);
        return new JwtUser(jwt, new HashSet<>(), user);
    }
}
