package fan.service;

import fan.command.SystemCommand;

import java.util.List;

/**
 * 系统接口
 *
 * @author Fan
 * since 2022/12/15 17:09
 */
public interface SystemService {

    /**
     * 通过用户 ID 先从 Redis 获取权限, 假如 Redis 不存在则从数据库查询并存入 Redis
     *
     * @param userId 用户ID
     * @return {@link String}
     * @author Fan
     * @since 2022/11/27 6:30
     */
    String getAuthorities(String userId);

    /**
     * 获取角色 ID 列表, 先从 Redis 获取, 获取不到再从数据库查询, 然后存入 Redis
     *
     * @param authKey 权限顶层Key
     * @param userId  用户ID
     * @return {@link List<String>}
     * @author Fan
     * @since 2022/12/15 17:14
     */
    List<String> listRoleIds(String authKey, String userId);

    /**
     * 获取菜单 ID 列表, 先从 Redis 获取, 获取不到再从数据库查询, 然后存入 Redis
     *
     * @param authKey 权限顶层Key
     * @param roleIds 角色ID列表
     * @return {@link List<String>}
     * @author Fan
     * @since 2022/12/15 17:15
     */
    List<String> listMenuIds(String authKey, List<String> roleIds);

    /**
     * 通过菜单清除权限
     *
     * @param systemCommand 系统更新参数
     * @author Fan
     * @since 2022/12/16 1:13
     */
    void clearAuthoritiesByMenu(SystemCommand systemCommand);

    /**
     * 通过角色清除权限
     *
     * @param systemCommand 系统更新参数
     * @author Fan
     * @since 2022/12/16 1:13
     */
    void clearAuthoritiesByRole(SystemCommand systemCommand);
}
