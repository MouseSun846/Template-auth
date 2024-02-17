package org.doc.exception;


import lombok.Getter;
import org.doc.enums.AuthEnum;

@Getter
public class UserAuthorizeException extends RuntimeException{
    private Integer code;

    public UserAuthorizeException(AuthEnum authEnum) {
        super(authEnum.getMessage());
        this.code = authEnum.getCode();
    }
    public UserAuthorizeException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
