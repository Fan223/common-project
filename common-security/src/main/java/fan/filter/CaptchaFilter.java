package fan.filter;

import fan.consts.AuthConst;
import fan.exception.CustomException;
import fan.hander.LoginFailureHandler;
import fan.utils.AuthUtil;

import fan.utils.LogUtil;
import fan.utils.RedisUtil;
import fan.utils.collection.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码过滤器
 *
 * @author Fan
 * @since 2022/11/24 17:25
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        LogUtil.info("CaptchaFilter-验证码过滤器");

        if (request.getRequestURI().equals("/resNav/login") && request.getMethod().equals("POST")) {
            try {
                // 校验验证码
                validate(request);
                filterChain.doFilter(request, response);
            } catch (CustomException e) {
                // 交给登录失败处理器处理
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 校验验证码
     *
     * @param request 请求
     * @author Fan
     * @since 2022/11/27 6:27
     */
    private void validate(HttpServletRequest request) {
        String loginToken = request.getParameter("loginToken");
        String captcha = request.getParameter("captcha");

        if (StringUtil.isBlank(captcha) || StringUtil.isBlank(loginToken)) {
            throw new CustomException("验证码不能为空");
        }

        String ipAddress = AuthUtil.getIpAddress(request);

        // 获取 Redis 中存储的验证码, 取完后就删除
        Object verifyCaptcha = RedisUtil.hashGet(AuthConst.CAPTCHA_KEY + ":" + ipAddress, loginToken);
        RedisUtil.del(AuthConst.CAPTCHA_KEY + ":" + ipAddress);

        if (!captcha.equals(verifyCaptcha)) {
            throw new CustomException("验证码错误");
        }
    }
}
