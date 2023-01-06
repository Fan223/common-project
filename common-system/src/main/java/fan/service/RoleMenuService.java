package fan.service;

import fan.command.RoleMenuCommand;
import fan.query.RoleMenuQuery;
import fan.base.Response;

import java.util.List;

/**
 * 角色菜单关联接口
 *
 * @author Fan
 * @since 2022/11/25 15:17
 */
public interface RoleMenuService {

    /**
     * 获取菜单 ID 列表
     *
     * @param roleMenuQuery 角色菜单查询参数
     * @return {@link List<String>}
     * @author Fan
     * @since 2022/12/1 11:25
     */
    List<String> listMenuIds(RoleMenuQuery roleMenuQuery);

    /**
     * 删除角色菜单
     *
     * @param roleMenuCommand 角色菜单更新参数
     * @return {@link int}
     * @author Fan
     * @since 2022/12/1 11:30
     */
    int deleteRoleMenu(RoleMenuCommand roleMenuCommand);

    /**
     * 分配权限
     *
     * @param roleId          角色ID
     * @param roleMenuCommand 角色菜单更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/1 11:30
     */
    Response assignPermissions(String roleId, RoleMenuCommand roleMenuCommand);

    /**
     * 通过菜单 ID 获取角色 ID 列表
     *
     * @param roleMenuQuery 角色菜单查询参数
     * @return {@link List<String>}
     * @author Fan
     * @since 2022/12/15 22:16
     */
    List<String> listRoleIds(RoleMenuQuery roleMenuQuery);

    /**
     * 更新角色菜单
     *
     * @param roleMenuCommand 角色菜单更新参数
     * @return {@link int}
     * @author Fan
     * @since 2022/12/16 1:12
     */
    int updateRoleMenu(RoleMenuCommand roleMenuCommand);
}
