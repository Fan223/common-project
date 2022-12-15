package fan.controller;

import fan.command.UserRoleCommand;
import fan.query.UserRoleQuery;
import fan.service.UserRoleService;
import fan.utils.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户角色关联Controller
 *
 * @author Fan
 * @since 2022/12/5 15:18
 */
@RestController
@RequestMapping("/userRole")
public class UserRoleController {

    @Resource
    private UserRoleService userRoleService;

    /**
     * 通过用户 ID 获取角色列表
     *
     * @param userId 用户ID
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/11 6:35
     */
    @PreAuthorize("hasAnyAuthority('userRole:assignRole')")
    @GetMapping("/listRoleIds/{userId}")
    public Result listRoleIds(@PathVariable("userId") String userId, UserRoleQuery userRoleQuery) {
        userRoleQuery.setUserId(userId);

        return Result.success("获取角色列表成功", userRoleService.listRoleIds(userRoleQuery));
    }

    /**
     * 分配角色
     *
     * @param userId          用户ID
     * @param userRoleCommand 用户角色更新参数
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/11 6:36
     */
    @PreAuthorize("hasAnyAuthority('userRole:assignRole')")
    @PostMapping("/assignRoles/{userId}")
    public Result assignRoles(@PathVariable("userId") String userId,
                              @RequestBody UserRoleCommand userRoleCommand) {
        return userRoleService.assignRoles(userId, userRoleCommand);
    }
}
