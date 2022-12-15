package fan.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义异常类
 *
 * @author Fan
 * @since 2022/11/26 3:35
 */
public class CustomException extends AuthenticationException {

    private static final long serialVersionUID = -1L;

    public CustomException(String msg) {
        super(msg);
    }
}
