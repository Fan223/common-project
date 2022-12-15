package fan.controller;

import fan.query.LoginGeoQuery;
import fan.service.LoginGeoService;
import fan.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登录地理信息Controller
 *
 * @author Fan
 * since 2022/12/15 20:23
 */
@RestController
@RequestMapping("/loginGeo")
public class LoginGeoController {

    @Resource
    private LoginGeoService loginGeoService;

    @GetMapping("/pageLoginGeos")
    public Result pageLoginGeos(LoginGeoQuery loginGeoQuery) {
        return loginGeoService.pageLoginGeos(loginGeoQuery);
    }
}
