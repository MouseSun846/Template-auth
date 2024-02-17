package org.doc.controller;

import lombok.extern.slf4j.Slf4j;
import org.doc.data.vo.Account;
import org.doc.data.vo.User;
import org.doc.services.impl.AuthService;
import org.doc.util.ResultVO;
import org.doc.util.ResultVOUtil;
import org.doc.util.Utils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Resource
    private AuthService authService;

    @PostMapping("/login")
    public ResultVO login(HttpServletRequest request, @Valid @RequestBody User user) {
        String jwtToken = authService.authenticateUser(Utils.getIpAddress(request), user.getAccount(), user.getPassword());
        return ResultVOUtil.success(jwtToken);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResultVO logout(HttpServletRequest request, @Valid @RequestBody Account account) {
        authService.logout(request, account.getAccount());
        return ResultVOUtil.success();
    }
}
