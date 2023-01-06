package fan.controller;

import fan.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import fan.base.Response;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限Controller
 *
 * @author Fan
 * @since 2022/11/23 16:32
 */
@RestController
public class AuthController {

    @Resource
    private AuthService authService;

    /**
     * 获取验证码
     *
     * @param request 请求
     * @return {@link Response}
     * @author Fan
     * @since 2022/11/27 6:25
     */
    @GetMapping("/api/getCaptcha")
    public Response getCaptcha(HttpServletRequest request) {

        return authService.getCaptcha(request);
    }
}
