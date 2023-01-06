package fan.controller;

import fan.query.LoginInfoQuery;
import fan.service.LoginInfoService;
import fan.base.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登录信息Controller
 *
 * @author Fan
 * since 2022/12/15 20:23
 */
@RestController
@RequestMapping("/loginInfo")
public class LoginInfoController {

    @Resource
    private LoginInfoService loginInfoService;

    @GetMapping("/pageLoginInfos")
    public Response pageLoginInfos(LoginInfoQuery loginInfoQuery) {
        return loginInfoService.pageLoginInfos(loginInfoQuery);
    }
}
