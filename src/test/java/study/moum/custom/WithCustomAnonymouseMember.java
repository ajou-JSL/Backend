package study.moum.custom;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomAnonymouseSecurityContextFactory.class)
public @interface WithCustomAnonymouseMember {
    String membername = "anonymouse";
    String password = "1234";
}