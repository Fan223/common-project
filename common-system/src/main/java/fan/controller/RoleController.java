package fan.controller;

import fan.command.RoleCommand;
import fan.query.RoleQuery;
import fan.service.RoleService;
import fan.base.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 角色Controller
 *
 * @author Fan
 * @since 2022/11/25 10:49
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PreAuthorize("hasAnyAuthority('userRole:assignRole')")
    @GetMapping("/listRoles")
    public Response listRoles(RoleQuery roleQuery) {
        return roleService.listRoles(roleQuery);
    }

    /**
     * 获取角色分页列表
     *
     * @param roleQuery 角色查询参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/1 11:09
     */
    @PreAuthorize("hasAnyAuthority('role:list', 'userRole:assignRole')")
    @GetMapping("/pageRoles")
    public Response pageRoles(RoleQuery roleQuery) {
        return roleService.pageRoles(roleQuery);
    }

    /**
     * 添加角色
     *
     * @param roleCommand 角色更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/1 11:09
     */
    @PreAuthorize("hasAnyAuthority('role:add')")
    @PostMapping("/addRole")
    public Response addRole(@RequestBody RoleCommand roleCommand) {
        return roleService.addRole(roleCommand);
    }

    /**
     * 修改角色
     *
     * @param roleCommand 角色更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/1 11:09
     */
    @PreAuthorize("hasAnyAuthority('role:update')")
    @PutMapping("/updateRole")
    public Response updateRole(@RequestBody RoleCommand roleCommand) {
        return roleService.updateRole(roleCommand);
    }

    /**
     * 删除角色
     *
     * @param roleCommand 角色更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/1 11:09
     */
    @PreAuthorize("hasAnyAuthority('role:delete')")
    @DeleteMapping("/deleteRole")
    public Response deleteRole(@RequestBody RoleCommand roleCommand) {
        return roleService.deleteRole(roleCommand);
    }
}
