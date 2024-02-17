package org.doc.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum RoleEnum implements CodeEnum, GrantedAuthority {

    ROLE_USER(0, "USER"),
    ROLE_ADMIN(1, "ADMIN")
    ;
    private Integer code;
    private String message;

    RoleEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getAuthority() {
        return name();
    }
}
