package fan.controller;

import fan.command.UserRoleCommand;
import fan.service.UserRoleService;
import fan.base.Response;
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
     * 分配角色
     *
     * @param userId          用户ID
     * @param userRoleCommand 用户角色更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/11 6:36
     */
    @PreAuthorize("hasAnyAuthority('userRole:assignRole')")
    @PostMapping("/assignRoles/{userId}")
    public Response assignRoles(@PathVariable("userId") String userId,
                              @RequestBody UserRoleCommand userRoleCommand) {
        return userRoleService.assignRoles(userId, userRoleCommand);
    }
}
