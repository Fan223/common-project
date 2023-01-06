package fan.hander;

import cn.hutool.json.JSONUtil;
import fan.consts.AuthConst;

import fan.utils.LogUtil;
import fan.utils.RedisUtil;
import fan.base.Response;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 注销成功处理器
 *
 * @author Fan
 * @since 2022/11/30 11:34
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        LogUtil.info("CustomLogoutSuccessHandler-注销成功处理器");

        // 手动退出
        if (authentication != null) {
            RedisUtil.del(AuthConst.AUTH_KEY + ":" + authentication.getName());
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        // 返回响应信息
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        Response success = Response.success("注销成功", "CustomLogoutSuccessHandler");
        outputStream.write(JSONUtil.toJsonStr(success).getBytes(StandardCharsets.UTF_8));

        // 关闭流
        outputStream.flush();
        outputStream.close();
    }
}
