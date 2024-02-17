package org.doc.exception.Handle;

import lombok.extern.slf4j.Slf4j;
import org.doc.exception.UserAuthorizeException;
import org.doc.util.ResultVO;
import org.doc.util.ResultVOUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 */
@ControllerAdvice
@Slf4j
public class HandlerException {

    @ExceptionHandler(value = UserAuthorizeException.class)
    @ResponseBody
    public ResultVO handleUserAuthorizeException(UserAuthorizeException userAuthorizeException) {
        return ResultVOUtil.error(userAuthorizeException.getCode(), userAuthorizeException.getMessage());
    }
}
