package fan.service.impl;

import fan.bo.UserRoleBO;
import fan.command.SystemCommand;
import fan.consts.SystemConst;
import fan.enums.MenuTypeEnum;
import fan.query.MenuQuery;
import fan.query.RoleMenuQuery;
import fan.query.RoleQuery;
import fan.query.UserRoleQuery;
import fan.service.*;

import fan.utils.LogUtil;
import fan.utils.RedisUtil;
import fan.utils.collection.ListUtil;
import fan.vo.MenuVO;
import fan.vo.RoleVO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统接口实现类
 *
 * @author Fan
 * since 2022/12/15 17:09
 */
@Service
public class SystemServeImpl implements SystemService {

    @Resource
    @Lazy
    private RoleService roleService;

    @Resource
    @Lazy
    private MenuService menuService;

    @Resource
    @Lazy
    private UserRoleService userRoleService;

    @Resource
    @Lazy
    private RoleMenuService roleMenuService;

    public String getAuthorities(String userId) {
        String authKey = SystemConst.AUTHENTICATION + ":" + userId;
        String authorities;

        if (RedisUtil.hasKey(authKey)) {
            LogUtil.info("从 Redis 获取用户权限");
            authorities = RedisUtil.hashGet(authKey, SystemConst.AUTHORITIES) + "";
        } else {
            LogUtil.info("从数据库获取用户权限");
            authorities = getAuthoritiesByUserId(userId, authKey);
            RedisUtil.hashSet(authKey, SystemConst.AUTHORITIES, authorities);
        }

        return authorities;
    }

    /**
     * 通过用户名获取该用户的权限信息
     *
     * @param userId 用户ID
     * @return {@link String}
     * @author Fan
     * @since 2022/12/7 15:11
     */
    private String getAuthoritiesByUserId(String userId, String authKey) {
        StringBuilder authorities = new StringBuilder();

        // 获取用户拥有的角色 ID 列表
        List<String> roleIds = listRoleIds(authKey, userId);

        if (null != roleIds) {
            // 通过获取的角色 ID 列表查询对应的角色信息
            List<RoleVO> roleVOS = ListUtil.castToList(RoleVO.class, roleService.listRoles(RoleQuery.builder().roleIds(roleIds).build()).getData());
            String roleAuthorities = roleVOS.stream().map(roleVO -> "ROLE_" + roleVO.getCode()).collect(Collectors.joining(","));
            authorities.append(roleAuthorities).append(",");

            // 通过获取的角色 ID 列表获取角色拥有的菜单 ID 列表
            List<String> menuIds = listMenuIds(authKey, roleIds);

            if (null != menuIds) {
                // 通过获取到的菜单 ID 列表查询对应的菜单信息, 包含全部类型菜单
                List<Integer> menuTypes = MenuTypeEnum.getTypeValues(Arrays.asList("目录", "菜单", "按钮"));
                List<MenuVO> menuVOS = menuService.listMenus(MenuQuery.builder().flag("Y").type(menuTypes).menuIds(menuIds).build());
                String menuAuthorities = menuVOS.stream().map(MenuVO::getPermission).collect(Collectors.joining(","));

                authorities.append(menuAuthorities);
            }
        }

        return authorities.toString();
    }

    public List<String> listRoleIds(String authKey, String userId) {
        List<String> roleIds = ListUtil.castToList(String.class, RedisUtil.hashGet(authKey, SystemConst.ROLE_IDS));

        if (ListUtil.isEmpty(roleIds)) {
            List<UserRoleBO> userRoleBOS = userRoleService.listUserRoles(UserRoleQuery.builder().userId(userId).flag("Y").build());
            roleIds = userRoleBOS.stream().map(UserRoleBO::getRoleId).collect(Collectors.toList());

            if (ListUtil.isEmpty(roleIds)) {
                LogUtil.error("用户未拥有角色权限");
                return null;
            }

            RedisUtil.hashSet(authKey, SystemConst.ROLE_IDS, roleIds);
        }

        return roleIds;
    }

    public List<String> listMenuIds(String authKey, List<String> roleIds) {
        List<String> menuIds = ListUtil.castToList(String.class, RedisUtil.hashGet(authKey, SystemConst.MENU_IDS));

        if (ListUtil.isEmpty(menuIds)) {
            menuIds = roleMenuService.listMenuIds(RoleMenuQuery.builder().roleIds(roleIds).flag("Y").build());

            if (ListUtil.isEmpty(menuIds)) {
                LogUtil.error("用户未拥有菜单权限");
                return null;
            }

            RedisUtil.hashSet(authKey, SystemConst.MENU_IDS, menuIds);
        }

        return menuIds;
    }

    @Override
    public void clearAuthoritiesByMenu(SystemCommand systemCommand) {
        List<String> roleIds = roleMenuService.listRoleIds(RoleMenuQuery.builder().menuId(systemCommand.getMenuId())
                .menuIds(systemCommand.getMenuIds()).build());

        clearAuthoritiesByRole(SystemCommand.builder().roleIds(roleIds).build());
    }

    @Override
    public void clearAuthoritiesByRole(SystemCommand systemCommand) {
        List<UserRoleBO> userRoleBOS = userRoleService.listUserRoles(UserRoleQuery.builder().roleId(systemCommand.getRoleId())
                .roleIds(systemCommand.getRoleIds()).build());

        if (ListUtil.isNotEmpty(userRoleBOS)) {
            List<String> userIds = userRoleBOS.stream().map(UserRoleBO::getUserId).collect(Collectors.toList());

            List<String> keys = userIds.stream().map(userId -> SystemConst.AUTHENTICATION + ":" + userId).collect(Collectors.toList());
            RedisUtil.del(keys);
        }
    }
}
