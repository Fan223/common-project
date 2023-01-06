package fan.controller;

import fan.command.UserCommand;
import fan.query.UserQuery;
import fan.service.UserService;
import fan.base.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * 用户Controller
 *
 * @author Fan
 * since 2022/12/15 11:54
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 获取用户分页列表
     *
     * @param userQuery 用户查询参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/11/25 21:57
     */
    @GetMapping("/pageUsers")
    @PreAuthorize("hasAnyAuthority('user:list')")
    public Response pageUsers(UserQuery userQuery) {
        return userService.pageUsers(userQuery);
    }

    /**
     * 添加用户信息
     *
     * @param userCommand 用户更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/11 6:28
     */
    @PostMapping("/addUser")
    @PreAuthorize("hasAnyAuthority('user:add')")
    public Response addUser(@RequestBody UserCommand userCommand) {
        return userService.addUser(userCommand);
    }

    /**
     * 修改用户信息
     *
     * @param userCommand 用户更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/11 6:28
     */
    @PutMapping("/updateUser")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public Response updateUser(@RequestBody UserCommand userCommand) {
        return userService.updateUser(userCommand);
    }

    /**
     * 删除用户信息
     *
     * @param userCommand 用户更新参数
     * @return {@link Response}
     * @author Fan
     * @since 2022/12/11 6:28
     */
    @DeleteMapping("/deleteUser")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public Response deleteUser(@RequestBody UserCommand userCommand, Principal principal) {
        return userService.deleteUser(userCommand, principal.getName());
    }
}
