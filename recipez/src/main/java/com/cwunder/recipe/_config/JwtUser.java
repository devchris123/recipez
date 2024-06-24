package com.cwunder.recipe._config;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.cwunder.recipe.user.CustomUserDetailsService;

/**
 * Custom JwtAuthenticationToken.
 * This class overrides getPrincipal() in order to return RecipeUserDetails
 * as expected by e.g. Spring Data to adjust query results depending on the
 * configured RecipeUserDetails.
 */
public class JwtUser extends JwtAuthenticationToken {
    private CustomUserDetailsService.RecipeUserDetails userDetails;

    public JwtUser(Jwt jwt, Collection<? extends GrantedAuthority> authorities,
            CustomUserDetailsService.RecipeUserDetails userDetails) {
        super(jwt, authorities);
        this.userDetails = userDetails;
    }

    @Override
    public CustomUserDetailsService.RecipeUserDetails getPrincipal() {
        return userDetails;
    }
}