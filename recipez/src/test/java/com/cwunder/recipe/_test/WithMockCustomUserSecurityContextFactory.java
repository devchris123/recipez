package com.cwunder.recipe._test;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.cwunder.recipe._shared.IdGenerator;
import com.cwunder.recipe.user.CustomUserDetailsService;
import com.cwunder.recipe.user.User;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        var user = new User(IdGenerator.genId(), customUser.username(), customUser.password(), true);
        var principal = new CustomUserDetailsService.RecipeUserDetails(user);
        var auth = new UsernamePasswordAuthenticationToken(principal, customUser.password(),
                principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}