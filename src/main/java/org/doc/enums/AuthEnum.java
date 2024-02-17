package org.doc.enums;

import lombok.Getter;

@Getter
public enum AuthEnum implements CodeEnum {
    ACCOUNT_PARAM_ERROR(100, "密码输入错误，请重新输入！"),
    USER_NOT_EXISTED(101, "用户不存在!"),
    EXPIRED_JWT_TOKEN(102, "登录已过期，当前用户被下线！"),
    USER_IS_LOGIN(103, "用户已登录，请勿重复登录！"),
    USER_IS_LOGIN_OUT(104, "用户已退出，请重新登录！");

    private Integer code;
    private String message;

    AuthEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
