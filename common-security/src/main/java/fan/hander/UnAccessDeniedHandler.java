package fan.hander;

import cn.hutool.json.JSONUtil;
import fan.utils.LogUtil;
import fan.base.Response;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 鉴权失败处理器
 *
 * @author Fan
 * @since 2022/11/25 17:25
 */
@Component
public class UnAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        LogUtil.error("UnAccessDeniedHandler-鉴权失败处理器");

        // 403, 未授权, 禁止访问
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");

        // 返回响应信息
        ServletOutputStream outputStream = response.getOutputStream();
        Response fail = Response.fail(HttpServletResponse.SC_FORBIDDEN, "未授权, 不允许访问", "UnAccessDeniedHandler");
        outputStream.write(JSONUtil.toJsonStr(fail).getBytes(StandardCharsets.UTF_8));

        // 关闭流
        outputStream.flush();
        outputStream.close();
    }
}
