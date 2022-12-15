package fan.service;

import fan.command.RoleCommand;
import fan.query.RoleQuery;
import fan.utils.Result;
import fan.vo.RoleVO;

import java.util.List;

/**
 * 角色接口
 *
 * @author Fan
 * @since 2022/11/25 10:47
 */
public interface RoleService {

    /**
     * 获取角色列表
     *
     * @param roleQuery 角色查询参数
     * @return {@link List<RoleVO>}
     * @author Fan
     * @since 2022/11/27 6:41
     */
    Result listRoles(RoleQuery roleQuery);

    /**
     * 获取角色分页列表
     *
     * @param roleQuery 角色查询参数
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/11 22:33
     */
    Result pageRoles(RoleQuery roleQuery);

    /**
     * 添加角色
     *
     * @param roleCommand 角色更新参数
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/1 11:10
     */
    Result addRole(RoleCommand roleCommand);

    /**
     * 修改角色
     *
     * @param roleCommand 角色更新参数
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/1 11:10
     */
    Result updateRole(RoleCommand roleCommand);

    /**
     * 删除角色
     *
     * @param roleCommand 角色更新参数
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/1 11:10
     */
    Result deleteRole(RoleCommand roleCommand);
}
