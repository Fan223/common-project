package fan.hander;

import cn.hutool.json.JSONUtil;
import fan.utils.LogUtil;
import fan.base.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 认证失败处理器(实际该类异常会先被全局异常处理器拦截)
 *
 * @author Fan
 * @since 2022/11/25 17:17
 */
@Component
public class UnAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        LogUtil.error("UnAuthenticationEntryPoint-认证失败处理器");

        // 401, 未认证
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");

        // 返回响应信息
        ServletOutputStream outputStream = response.getOutputStream();
        Response fail = Response.fail(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage(), "UnAuthenticationEntryPoint");
        outputStream.write(JSONUtil.toJsonStr(fail).getBytes(StandardCharsets.UTF_8));

        // 关闭流
        outputStream.flush();
        outputStream.close();
    }
}
