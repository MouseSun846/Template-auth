package org.doc.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.doc.auth.filter.JwtTokenProvider;
import org.doc.entity.User;
import org.doc.enums.AuthEnum;
import org.doc.exception.UserAuthorizeException;
import org.doc.services.UserServices;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@Service
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @Resource
    private UserServices userServices;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public String authenticateUser(String ip, String account, String password) {
        User user = userServices.findUserByAccount(account);
        if (ObjectUtils.isEmpty(user)) {
            throw new UserAuthorizeException(AuthEnum.USER_NOT_EXISTED);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(account, Base64.getEncoder().encodeToString(password.getBytes())));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new UserAuthorizeException(AuthEnum.ACCOUNT_PARAM_ERROR);
        }
        String token = jwtTokenProvider.createToken(account, user.getRole().name());
        user.setToken(token);
        long time = System.currentTimeMillis();
        user.setUpdated_time(time);
        user.setIp(ip);
        userServices.modifyUser(user);

        return token;
    }

    public void logout(HttpServletRequest request, String account) {
        // 经过鉴权后token肯定不为空
        String token = jwtTokenProvider.resolveToken(request);
        User user = getUser(account, token);
        user.setToken("");
        userServices.saveUser(user);

    }

    public User getUser(String account, String token) {
        User user = userServices.findUserByAccount(account);
        if (ObjectUtils.isEmpty(user)) {
            throw new UserAuthorizeException(AuthEnum.USER_NOT_EXISTED);
        }
        // 判断账户是否登录
        if (ObjectUtils.isEmpty(user.getToken())) {
            throw new UserAuthorizeException(AuthEnum.USER_IS_LOGIN_OUT);
        }
        if (!user.getToken().equals(token)) {
            throw new UserAuthorizeException(AuthEnum.ACCOUNT_PARAM_ERROR);
        }
        return user;
    }
}
