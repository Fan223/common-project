package fan.service;

import fan.base.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限接口
 *
 * @author Fan
 * @since 2022/11/25 13:54
 */
public interface AuthService {

    /**
     * 获取验证码
     *
     * @param request 请求
     * @return {@link Response}
     * @author Fan
     * @since 2022/11/27 6:25
     */
    Response getCaptcha(HttpServletRequest request);
}
