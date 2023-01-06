package fan.controller;

import fan.command.RoleMenuCommand;
import fan.query.RoleMenuQuery;
import fan.service.RoleMenuService;
import fan.base.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 角色菜单关联Controller
 *
 * @author Fan
 * @since 2022/11/30 15:57
 */
@RestController
@RequestMapping("/roleMenu")
public class RoleMenuController {

    @Resource
    private RoleMenuService roleMenuService;

    /**
     * 通过角色 ID 获取菜单列表
     *
     * @param roleId 角色ID
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/1 11:12
     */
    @PreAuthorize("hasAnyAuthority('roleMenu:assignPermission')")
    @GetMapping("/listMenuIds/{roleId}")
    public Response listMenuIds(@PathVariable("roleId") String roleId) {
        return Response.success("获取菜单列表成功", roleMenuService.listMenuIds(RoleMenuQuery.builder().roleId(roleId).build()));
    }

    /**
     * 分配权限
     *
     * @param roleId          角色ID
     * @param roleMenuCommand 角色菜单更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/1 11:13
     */
    @PreAuthorize("hasAnyAuthority('roleMenu:assignPermission')")
    @PostMapping("/assignPermissions/{roleId}")
    public Response assignPermissions(@PathVariable("roleId") String roleId,
                                    @RequestBody RoleMenuCommand roleMenuCommand) {
        return roleMenuService.assignPermissions(roleId, roleMenuCommand);
    }
}
