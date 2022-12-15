package fan.service;

import fan.command.UserRoleCommand;
import fan.query.UserRoleQuery;
import fan.utils.Result;

import java.util.List;

/**
 * 用户角色关联接口
 *
 * @author Fan
 * @since 2022/11/25 14:12
 */
public interface UserRoleService {

    /**
     * 获取角色 ID 列表
     *
     * @param userRoleQuery 用户角色查询参数
     * @return {@link List<String>}
     * @author Fan
     * @since 2022/11/25 14:18
     */
    List<String> listRoleIds(UserRoleQuery userRoleQuery);

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
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/11 6:35
     */
    Result assignRoles(String userId, UserRoleCommand userRoleCommand);

    /**
     * 获取用户 ID 列表
     *
     * @param userRoleQuery 用户角色查询参数
     * @return {@link List<String>}
     * @author Fan
     * @since 2022/12/15 22:20
     */
    List<String> listUserIds(UserRoleQuery userRoleQuery);

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
