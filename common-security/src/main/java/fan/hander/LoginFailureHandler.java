package fan.hander;

import cn.hutool.json.JSONUtil;
import fan.utils.LogUtil;
import fan.base.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录失败处理器
 *
 * @author Fan
 * @since 2022/11/23 14:58
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        LogUtil.error("LoginFailureHandler-登录失败处理器: " + exception.getMessage());

        // 返回响应信息
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        Response fail = Response.fail(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage(), "failureHandler");
        outputStream.write(JSONUtil.toJsonStr(fail).getBytes(StandardCharsets.UTF_8));

        // 关闭流
        outputStream.flush();
        outputStream.close();
    }
}
