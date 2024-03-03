package com.roundtable.roundtable.implement.member;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import com.roundtable.roundtable.implement.auth.JwtProvider;
import com.roundtable.roundtable.implement.auth.Token;
import com.roundtable.roundtable.global.exception.MemberException.MemberUnAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginManager {
    private final PasswordEncoder passwordEncoder;
    private final MemberReader memberReader;
    private final JwtProvider jwtProvider;
    public Token login(final LoginMember loginMember) {
        Member member = memberReader.findByEmail(loginMember.email());
        if(member.isCorrectPassword(loginMember.password(), passwordEncoder)) {
            return jwtProvider.issueToken(member.getId());
        }

        throw new MemberUnAuthorizationException(MemberErrorCode.INVALID_LOGIN);
    }
}
