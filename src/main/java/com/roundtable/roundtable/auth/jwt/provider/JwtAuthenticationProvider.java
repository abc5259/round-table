package com.roundtable.roundtable.auth.jwt.provider;

import com.roundtable.roundtable.auth.exception.AuthenticationException;
import com.roundtable.roundtable.auth.jwt.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {
    private final JwtProvider jwtProvider;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null || !supports(authentication.getClass())) {
            return null;
        }

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

        Token token = jwtAuthenticationToken.getToken();
        /**
         * 사용자가 요청으로 보낸 인증 헤더가 빈 값이면 token은 null일 수 있다.
         */
<<<<<<< HEAD
        if(token == null || !jwtProvider.isValidToken(token.getAccessToken())) {
            return null;
=======
        if(token == null) {
            throw new JwtAuthenticationException("유효하지 않은 인증입니다.");
        }

        if(!jwtProvider.isValidToken(token.getAccessToken())) {
            throw new JwtAuthenticationException("유효하지 않은 인증입니다.");
>>>>>>> origin/main
        }

        final Long userId = jwtProvider.getSubject(token.getAccessToken());

        return new JwtAuthenticationToken(token, userId, null, null);
    }
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }

}
