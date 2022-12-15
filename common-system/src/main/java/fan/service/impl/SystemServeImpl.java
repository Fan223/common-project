package fan.service.impl;

import fan.command.SystemCommand;
import fan.consts.SystemConst;
import fan.query.RoleMenuQuery;
import fan.query.UserRoleQuery;
import fan.service.RoleMenuService;
import fan.service.SystemService;
import fan.service.UserRoleService;
import fan.utils.CommonUtil;
import fan.utils.LogUtil;
import fan.utils.RedisUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private UserRoleService userRoleService;

    @Resource
    @Lazy
    private RoleMenuService roleMenuService;

    public List<String> listRoleIds(String authKey, String userId) {
        List<String> roleIds = CommonUtil.castToList(RedisUtil.hashGet(authKey, SystemConst.ROLE_IDS), String.class);

        if (CommonUtil.isBlank(roleIds)) {
            roleIds = userRoleService.listRoleIds(UserRoleQuery.builder().userId(userId).flag("Y").build());

            if (CommonUtil.isBlank(roleIds)) {
                LogUtil.error("用户未拥有角色权限");
                return null;
            }

            RedisUtil.hashSet(authKey, SystemConst.ROLE_IDS, roleIds);
        }

        return roleIds;
    }

    public List<String> listMenuIds(String authKey, List<String> roleIds) {
        List<String> menuIds = CommonUtil.castToList(RedisUtil.hashGet(authKey, SystemConst.MENU_IDS), String.class);

        if (CommonUtil.isBlank(menuIds)) {
            menuIds = roleMenuService.listMenuIds(RoleMenuQuery.builder().roleIds(roleIds).flag("Y").build());

            if (CommonUtil.isBlank(menuIds)) {
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
        List<String> userIds = userRoleService.listUserIds(UserRoleQuery.builder().roleId(systemCommand.getRoleId())
                .roleIds(systemCommand.getRoleIds()).build());

        List<String> keys = userIds.stream().map(userId -> SystemConst.AUTHENTICATION + ":" + userId).collect(Collectors.toList());
        RedisUtil.del(keys);
    }
}
