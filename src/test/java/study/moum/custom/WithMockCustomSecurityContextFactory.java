package study.moum.custom;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import study.moum.auth.domain.CustomUserDetails;
import study.moum.auth.domain.entity.MemberEntity;

public class WithMockCustomSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomMember> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomMember annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        MemberEntity member = MemberEntity.builder()
                .id(1)
                .role("ROLE_USER")
                .email("test@user.com")
                .password("1234")
                .username("tester")
                .build();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(member, null, customUserDetails.getAuthorities());
        context.setAuthentication(usernamePasswordAuthenticationToken);

        return context;
    }
}