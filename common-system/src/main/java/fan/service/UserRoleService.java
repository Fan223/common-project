package fan.service;

import fan.bo.UserRoleBO;
import fan.command.UserRoleCommand;
import fan.query.UserRoleQuery;
import fan.base.Response;

import java.util.List;

/**
 * 用户角色关联接口
 *
 * @author Fan
 * @since 2022/11/25 14:12
 */
public interface UserRoleService {

    /**
     * 获取用户角色列表
     *
     * @param userRoleQuery 用户角色查询参数
     * @return {@link List<String>}
     * @author Fan
     * @since 2022/11/25 14:18
     */
    List<UserRoleBO> listUserRoles(UserRoleQuery userRoleQuery);

    /**
     * 删除用户角色
     *
     * @param userRoleCommand 用户角色更新参数
     * @return {@link int}
     * @author Fan
     * @since 2022/12/11 5:19
     */
    int deleteUserRole(UserRoleCommand userRoleCommand);

    /**
     * 添加用户角色
     *
     * @param userId 用户ID
     * @return {@link int}
     * @author Fan
     * @since 2022/12/11 6:33
     */
    int addUserRole(String userId);

    /**
     * 分配角色
     *
     * @param userId          用户ID
     * @param userRoleCommand 用户角色更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/11 6:35
     */
    Response assignRoles(String userId, UserRoleCommand userRoleCommand);

    /**
     * 更新用户角色
     *
     * @param userRoleCommand 用户角色更新参数
     * @return {@link int}
     * @author Fan
     * @since 2022/12/16 1:14
     */
    int updateUserRole(UserRoleCommand userRoleCommand);
}
