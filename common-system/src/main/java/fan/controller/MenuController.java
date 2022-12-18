package fan.controller;

import cn.hutool.core.map.MapUtil;
import fan.command.MenuCommand;
import fan.query.MenuQuery;
import fan.service.MenuService;
import fan.service.SystemService;
import fan.utils.Result;
import fan.vo.MenuVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

/**
 * 菜单Controller
 *
 * @author Fan
 * @since 2022/11/22 9:53
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @Resource
    private SystemService systemService;

    /**
     * 获取导航菜单列表
     *
     * @param principal 主体
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/11 3:26
     */
    @GetMapping("/listNavMenus")
    public Result listNavMenus(Principal principal) {
        List<MenuVO> menuVOS = menuService.listNavMenusByUserId(principal.getName());
        String authorities = systemService.getAuthorities(principal.getName());

        return Result.success("查询导航菜单列表成功", MapUtil.builder().put("menus", menuVOS)
                .put("authorities", Arrays.asList(authorities.split(","))).build());
    }

    /**
     * 获取菜单树形列表
     *
     * @return {@link Result}
     * @author Fan
     * @since 2022/11/28 16:52
     */
    @PreAuthorize("hasAnyAuthority('menu:list', 'roleMenu:assignPermission')")
    @GetMapping("/listMenusTree")
    public Result listMenusTree(MenuQuery menuQuery) {
        return menuService.listMenusTree(menuQuery);
    }

    /**
     * 添加菜单
     *
     * @param menuCommand 菜单更新参数
     * @return {@link Result}
     * @author Fan
     * @since 2022/11/29 15:51
     */
    @PreAuthorize("hasAnyAuthority('menu:add')")
    @PostMapping("/addMenu")
    public Result addMenu(@RequestBody MenuCommand menuCommand) {
        return menuService.addMenu(menuCommand);
    }

    /**
     * 修改菜单
     *
     * @param menuCommand 菜单更新参数
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/1 11:08
     */
    @PreAuthorize("hasAnyAuthority('menu:update')")
    @PutMapping("/updateMenu")
    public Result updateMenu(@RequestBody MenuCommand menuCommand) {
        return menuService.updateMenu(menuCommand);
    }

    /**
     * 删除菜单
     *
     * @param menuCommand 菜单更新参数
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/1 11:08
     */
    @PreAuthorize("hasAnyAuthority('menu:delete')")
    @DeleteMapping("/deleteMenu")
    public Result deleteMenu(@RequestBody MenuCommand menuCommand) {
        return menuService.deleteMenu(menuCommand);
    }
}
