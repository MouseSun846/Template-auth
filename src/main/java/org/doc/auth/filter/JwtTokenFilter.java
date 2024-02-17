package org.doc.auth.filter;

import cn.hutool.json.JSONUtil;

import org.doc.exception.UserAuthorizeException;
import org.doc.util.ResultVO;
import org.doc.util.ResultVOUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
 */
public class JwtTokenFilter extends OncePerRequestFilter {

  private JwtTokenProvider jwtTokenProvider;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    String token = jwtTokenProvider.resolveToken(httpServletRequest);
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        UsernamePasswordAuthenticationToken auth = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (UserAuthorizeException ex) {
      // this is very important, since it guarantees the user is not authenticated at all
      SecurityContextHolder.clearContext();
      httpServletResponse.setStatus(HttpServletResponse.SC_OK);
      ResultVO resultVO = ResultVOUtil.error(ex.getCode(), ex.getMessage());
      httpServletResponse.setContentType("text/plain;charset=UTF-8");
      httpServletResponse.getWriter().write(JSONUtil.toJsonStr(resultVO));
      return;
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

}
