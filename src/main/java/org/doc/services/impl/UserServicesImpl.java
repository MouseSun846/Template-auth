package org.doc.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.doc.entity.User;
import org.doc.enums.AuthEnum;
import org.doc.exception.UserAuthorizeException;
import org.doc.mapper.UserMapper;
import org.doc.services.UserServices;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class UserServicesImpl implements UserServices, UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User findUserByAccount(String account) {
        if (ObjectUtils.isEmpty(account)) {
            throw new UserAuthorizeException(AuthEnum.ACCOUNT_PARAM_ERROR);
        }
        return userMapper.findUserByAccount(account);
    }

    @Override
    public User saveUser(User user) {
        return userMapper.updateById(user) > 0 ? user : null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean cleanUpToken() {
        return userMapper.cleanUpToken() > 0;
    }

    @Override
    public boolean modifyUser(User user) {
        return userMapper.modifyUser(user) > 0;
    }


    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = userMapper.findUserByAccount(account);
        if (ObjectUtils.isEmpty(account)) {
            throw new UserAuthorizeException(AuthEnum.ACCOUNT_PARAM_ERROR);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getAccount())
                .password(user.getPassword())
                .authorities(user.getRole())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
