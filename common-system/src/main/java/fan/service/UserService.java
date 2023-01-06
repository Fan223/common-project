package fan.service;

import fan.base.Response;
import fan.bo.UserBO;
import fan.command.UserCommand;
import fan.query.UserQuery;
import fan.base.Response;

/**
 * 用户接口
 *
 * @author Fan
 * @since 2022/11/25 10:44
 */
public interface UserService {

    /**
     * 获取用户信息
     *
     * @param userQuery 用户查询参数
     * @return {@link UserBO}
     * @author Fan
     * @since 2022/11/25 16:43
     */
    UserBO getUser(UserQuery userQuery);

    /**
     * 获取用户分页列表
     *
     * @param userQuery 用户查询参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/2 16:58
     */
    Response pageUsers(UserQuery userQuery);

    /**
     * 添加用户信息
     *
     * @param userCommand 用户更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/11 6:28
     */
    Response addUser(UserCommand userCommand);

    /**
     * 修改用户信息
     *
     * @param userCommand 用户更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/11 6:28
     */
    Response updateUser(UserCommand userCommand);

    /**
     * 删除用户信息
     *
     * @param userCommand 用户更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/11 6:28
     */
    Response deleteUser(UserCommand userCommand, String userId);
}
