package fan.hander;

import cn.hutool.json.JSONUtil;
import eu.bitwalker.useragentutils.UserAgent;
import fan.base.Response;
import fan.consts.AuthConst;
import fan.entity.LoginInfoDO;
import fan.service.LoginInfoService;
import fan.service.impl.UserDetailsImpl;
import fan.utils.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录成功处理器
 *
 * @author Fan
 * @since 2022/11/23 14:57
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private LoginInfoService loginInfoService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        LogUtil.info("LoginSuccessHandler-登录成功处理器");

        // 生成 JWT, 并放到响应头里
        String jwt = JwtUtil.generateJwt(authentication.getName());
        response.setHeader(AuthConst.AUTH_KEY, jwt);

        // 返回响应信息
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        Response success = Response.success("登录成功", "successHandler");
        outputStream.write(JSONUtil.toJsonStr(success).getBytes(StandardCharsets.UTF_8));

        // 关闭流
        outputStream.flush();
        outputStream.close();

        // 通过 request 请求获取 IP 地址
        String ipAddress = AuthUtil.getIpAddress(request);
        // 通过 IP 地址获取地理信息
        LoginInfoDO loginInfoDO = AuthUtil.getGeoInformation(ipAddress);

        // 获取操作系统和浏览器信息
        String ua = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);

        loginInfoDO.setOperateSystem(userAgent.getOperatingSystem().getName());
        loginInfoDO.setBrowser(userAgent.getBrowser().getName());

        // 获取真实用户名
        String realUserName = ((UserDetailsImpl) authentication.getPrincipal()).getRealUsername();
        loginInfoDO.setUsername(realUserName);

        // 判断登录用户信息是否存在
        LoginInfoDO loginInfo = loginInfoService.getLoginInfo(realUserName);
        if (null == loginInfo) {
            loginInfoService.addLoginInfo(loginInfoDO);
        } else {
            loginInfoService.updateLoginInfo(loginInfoDO);
        }
    }
}
