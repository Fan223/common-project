package fan.service;

import fan.entity.LoginInfoDO;
import fan.query.LoginInfoQuery;
import fan.base.Response;

/**
 * 登录信息接口
 *
 * @author Fan
 * @since 2022/12/14 16:56
 */
public interface LoginInfoService {

    /**
     * 通过用户名查询登录信息
     *
     * @param username 用户名
     * @return {@link LoginInfoDO}
     * @author Fan
     * @since 2022/12/14 17:00
     */
    LoginInfoDO getLoginInfo(String username);

    /**
     * 添加登录信息
     *
     * @param loginInfoDO 登录信息实体类
     * @author Fan
     * @since 2022/12/15 19:55
     */
    void addLoginInfo(LoginInfoDO loginInfoDO);

    /**
     * 更新登录信息
     *
     * @param loginInfoDO 登录信息实体类
     * @author Fan
     * @since 2022/12/16 1:36
     */
    void updateLoginInfo(LoginInfoDO loginInfoDO);


    /**
     * 分页查询登录信息
     *
     * @param loginInfoQuery 登录信息查询类
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/16 1:07
     */
    Response pageLoginInfos(LoginInfoQuery loginInfoQuery);
}
