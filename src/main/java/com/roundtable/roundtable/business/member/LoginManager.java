package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.business.auth.JwtPayload;
import com.roundtable.roundtable.business.auth.JwtProvider;
import com.roundtable.roundtable.business.auth.Tokens;
import com.roundtable.roundtable.business.token.CreateToken;
import com.roundtable.roundtable.business.token.TokenService;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
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

    private final TokenService tokenService;

    public Tokens login(final LoginMember loginMember) {
        Member member = memberReader.findByEmail(loginMember.email());
        if(member.isCorrectPassword(loginMember.password(), passwordEncoder)) {
            Tokens tokens = jwtProvider.issueToken(toJwtPayload(member));
            tokenService.saveOrUpdateToken(new CreateToken(member.getId(), tokens.getRefreshToken()));
            return tokens;
        }

        throw new MemberUnAuthorizationException(MemberErrorCode.INVALID_LOGIN);
    }

    private JwtPayload toJwtPayload(Member member) {
        if(member.isEnterHouse()) {
            return new JwtPayload(member.getId(), member.getHouse().getId());
        }

        return new JwtPayload(member.getId(), null);
    }
}
