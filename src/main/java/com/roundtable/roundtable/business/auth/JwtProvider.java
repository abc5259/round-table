package com.roundtable.roundtable.business.auth;

import com.roundtable.roundtable.global.exception.AuthenticationException;
import com.roundtable.roundtable.global.exception.errorcode.AuthErrorCode;
import com.roundtable.roundtable.global.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private static final String USER_ID = "userId";
    private static final String HOUSE_ID = "houseId";

    public Token issueToken(JwtPayload jwtPayload) {
        return Token.of(
                generateToken(jwtPayload.userId(), jwtPayload.houseId(),true),
                generateToken(jwtPayload.userId(), jwtPayload.houseId(),false)
        );
    }

    private String generateToken(Long userId, Long houseId, boolean isAccessToken) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + (isAccessToken ? jwtProperties.getAccessTokenExpireTime() : jwtProperties.getRefreshTokenExpireTime()));
        return Jwts.builder()
                .claim(USER_ID, userId)
                .claim(HOUSE_ID, houseId)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(jwtProperties.getSecretKey(), SIG.HS256)
                .compact();
    }

    public JwtPayload extractPayload(String token) {
        Claims claims = extractClaims(token);
        Long userId = claims.get(USER_ID, Long.class);
        Long houseId = claims.get(HOUSE_ID, Long.class);
        return new JwtPayload(userId, houseId);
    }

    private Claims extractClaims(String token) {
        try {
            Claims claims = getJwtParser().parseSignedClaims(token).getPayload();
            return claims;
        }catch (JwtException e) {
            throw new AuthenticationException(AuthErrorCode.JWT_EXTRACT_ERROR, e);
        }
    }

    private JwtParser getJwtParser() {
        return Jwts.parser().verifyWith(jwtProperties.getSecretKey()).build();
    }
}
