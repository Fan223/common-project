package fan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import fan.utils.LogUtil;
import fan.base.Response;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理类
 *
 * @author Fan
 * @since 2022/11/25 13:59
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Response handler(RuntimeException e) {
        LogUtil.error("运行时异常管理: " + e);
        return Response.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    public Response handler(AccessDeniedException e) {
        LogUtil.error("鉴权失败异常处理: " + e);
        return Response.fail(HttpServletResponse.SC_FORBIDDEN, "未授权, 不允许访问", "UnAccessDeniedHandler");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = CustomException.class)
    public Response handler(CustomException e) {
        LogUtil.error("自定义异常处理: " + e);
        return Response.fail(HttpServletResponse.SC_UNAUTHORIZED, "认证失败, 请重新登录", "CustomException-UNAUTHORIZED");
    }
}
