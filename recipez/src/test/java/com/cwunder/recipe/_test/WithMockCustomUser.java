package com.cwunder.recipe._test;

import java.lang.annotation.*;

import org.springframework.security.test.context.support.*;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "testuser";

    String password() default "testpw";

}