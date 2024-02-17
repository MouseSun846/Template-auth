package org.doc.auth.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.doc.entity.User;
import org.doc.enums.AuthEnum;
import org.doc.exception.UserAuthorizeException;
import org.doc.services.impl.UserServicesImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;

@Component
public class JwtTokenProvider {

  @Value("${JwtConfig.secret}")
  private String secretKey;

  @Resource
  private UserServicesImpl myUserDetails;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(String username, String role) {

    Claims claims = Jwts.claims().setSubject(username);
    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
    claims.put("auth", new ArrayList<SimpleGrantedAuthority>(){{add(simpleGrantedAuthority);}});
    claims.put("time", System.currentTimeMillis());
    return Jwts.builder()
        .setClaims(claims)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public UsernamePasswordAuthenticationToken getAuthentication(String token) {
    UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      User user = myUserDetails.findUserByAccount(getUsername(token));
      if(ObjectUtils.isEmpty(user)) {
        throw new UserAuthorizeException(AuthEnum.USER_NOT_EXISTED);
      }
      if(ObjectUtils.isEmpty(user.getToken()) || !user.getToken().equals(token)) {
        throw new UserAuthorizeException(AuthEnum.EXPIRED_JWT_TOKEN);
      }
      return true;
    } catch (Exception e) {
      throw new UserAuthorizeException(AuthEnum.EXPIRED_JWT_TOKEN);
    }
  }

}
