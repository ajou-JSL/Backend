package study.moum.custom;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockCustomAnonymouseSecurityContextFactory implements WithSecurityContextFactory<WithCustomAnonymouseMember> {
    @Override
    public SecurityContext createSecurityContext(WithCustomAnonymouseMember annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");
        AnonymousAuthenticationToken anonymouseToken = new AnonymousAuthenticationToken("key","anonymouse", authorities);
        context.setAuthentication(anonymouseToken);

        return context;
    }
}