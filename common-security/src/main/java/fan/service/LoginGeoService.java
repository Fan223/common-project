package fan.service;

import fan.entity.LoginGeoDO;
import fan.query.LoginGeoQuery;
import fan.utils.Result;

/**
 * 登录地理信息接口
 *
 * @author Fan
 * @since 2022/12/14 16:56
 */
public interface LoginGeoService {

    /**
     * 通过用户名查询登录地理信息
     *
     * @param username 用户名
     * @return {@link LoginGeoDO}
     * @author Fan
     * @since 2022/12/14 17:00
     */
    LoginGeoDO getLoginGeo(String username);

    /**
     * 添加登录地理信息
     *
     * @param loginGeo 登录地理信息实体类
     * @author Fan
     * @since 2022/12/15 19:55
     */
    void addLoginGeo(LoginGeoDO loginGeo);

    /**
     * 更新登录地理信息
     *
     * @param loginGeo 登录地理信息实体类
     * @author Fan
     * @since 2022/12/16 1:36
     */
    void updateLoginGeo(LoginGeoDO loginGeo);


    /**
     * 分页查询登录地理信息
     *
     * @param loginGeoQuery 登录地理信息查询类
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/16 1:07
     */
    Result pageLoginGeos(LoginGeoQuery loginGeoQuery);
}
