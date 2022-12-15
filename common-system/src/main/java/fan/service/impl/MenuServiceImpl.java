package fan.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import fan.command.MenuCommand;
import fan.command.RoleMenuCommand;
import fan.command.SystemCommand;
import fan.consts.SystemConst;
import fan.dao.MenuDAO;
import fan.entity.MenuDO;
import fan.enums.MenuTypeEnum;
import fan.query.MenuQuery;
import fan.service.MenuService;
import fan.service.RoleMenuService;
import fan.service.SystemService;
import fan.utils.*;
import fan.vo.MenuVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单接口实现类
 *
 * @author Fan
 * @since 2022/11/22 9:54
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuDAO menuDAO;

    @Resource
    private SystemService systemService;

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private SystemMapStruct systemMapStruct;

    @Override
    public List<MenuVO> listMenus(MenuQuery menuQuery) {
        LambdaQueryWrapper<MenuDO> menuQueryWrapper = new LambdaQueryWrapper<>();

        menuQueryWrapper.eq(CommonUtil.isNotBlank(menuQuery.getFlag()), MenuDO::getFlag, menuQuery.getFlag())
                .in(CommonUtil.isNotBlank(menuQuery.getType()), MenuDO::getType, menuQuery.getType())
                .in(CommonUtil.isNotBlank(menuQuery.getMenuIds()), MenuDO::getId, menuQuery.getMenuIds())
                .like(CommonUtil.isNotBlank(menuQuery.getName()), MenuDO::getName, menuQuery.getName())
                .like(CommonUtil.isNotBlank(menuQuery.getPermission()), MenuDO::getPermission, menuQuery.getPermission())
                .orderByAsc(MenuDO::getOrderNum);

        List<MenuDO> menuDOS = menuDAO.selectList(menuQueryWrapper);
        return menuDOS.stream().map(menuDO -> systemMapStruct.menuDOToVO(menuDO)).collect(Collectors.toList());
    }

    @Override
    public List<MenuVO> listNavMenusByUserId(String userId) {
        String authKey = SystemConst.AUTHENTICATION + ":" + userId;

        // 从 Redis 中获取导航菜单列表
        List<MenuVO> navMenuVOS = CommonUtil.castToList(RedisUtil.hashGet(authKey, SystemConst.NAV_MENUS), MenuVO.class);
        if (CommonUtil.isNotBlank(navMenuVOS)) {
            return navMenuVOS;
        }

        // 获取不到再从数据库查询, 并存入 Redis
        // 先获取角色 ID 列表
        List<String> roleIds = systemService.listRoleIds(authKey, userId);
        if (null != roleIds) {
            // 通过获取的角色 ID 列表获取角色拥有的菜单 ID 列表
            List<String> menuIds = systemService.listMenuIds(authKey, roleIds);

            if (null != menuIds) {
                // 通过获取到的菜单 ID 列表查询对应的菜单信息, 只包括目录和菜单类型的菜单, 不包括按钮
                List<Integer> menuTypes = MenuTypeEnum.getTypeValues(CommonUtil.transToList(String.class, "目录", "菜单"));
                List<MenuVO> menuVOS = listMenus(MenuQuery.builder().flag("Y").menuIds(menuIds).type(menuTypes).build());
                navMenuVOS = SystemUtil.buildTree(menuVOS);

                // 将导航菜单列表存入 Redis
                RedisUtil.hashSet(authKey, SystemConst.NAV_MENUS, navMenuVOS);
            }
        }

        return navMenuVOS;
    }

    @Override
    public Result listMenusTree(MenuQuery menuQuery) {
        List<MenuVO> menuVOS = listMenus(menuQuery);

        return Result.success("获取菜单列表成功", SystemUtil.buildTree(menuVOS));
    }

    @Override
    public Result addMenu(MenuCommand menuCommand) {
        MenuDO menuDO = systemMapStruct.menuCommandToDO(menuCommand);

        if (CommonUtil.isBlank(menuDO.getParentId())) {
            menuDO.setParentId("root");
        }
        menuDO.setId(UUID.randomUUID().toString());
        menuDO.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        menuDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

        return Result.success("添加菜单成功", menuDAO.insert(menuDO));
    }

    /**
     * 通过菜单 ID 获取菜单信息
     *
     * @param menuId 菜单ID
     * @return {@link MenuDO}
     * @author Fan
     * @since 2022/12/16 1:29
     */
    private MenuDO getMenu(String menuId) {
        return menuDAO.selectById(menuId);
    }

    @Transactional
    @Override
    public Result updateMenu(MenuCommand menuCommand) {
        MenuDO menu = getMenu(menuCommand.getId());

        MenuDO menuDO = systemMapStruct.menuCommandToDO(menuCommand);
        menuDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        int updateNum = menuDAO.updateById(menuDO);

        if (!menuCommand.getFlag().equals(menu.getFlag())) {
            updateNum += roleMenuService.updateRoleMenu(RoleMenuCommand.builder().menuId(menuCommand.getId()).flag(menuCommand.getFlag()).build());

            systemService.clearAuthoritiesByMenu(SystemCommand.builder().menuId(menuCommand.getId()).build());
        } else if (!menuCommand.getPermission().equals(menu.getPermission())) {
            systemService.clearAuthoritiesByMenu(SystemCommand.builder().menuId(menuCommand.getId()).build());
        }

        return Result.success("修改菜单成功", updateNum);
    }

    @Transactional
    @Override
    public Result deleteMenu(MenuCommand menuCommand) {
        // 查询要删除的菜单列表的子菜单列表
        LambdaQueryWrapper<MenuDO> menuQueryWrapper = new LambdaQueryWrapper<>();
        menuQueryWrapper.in(CommonUtil.isNotBlank(menuCommand.getIds()), MenuDO::getParentId, menuCommand.getIds());
        List<MenuDO> menuDOS = menuDAO.selectList(menuQueryWrapper);

        if (CommonUtil.isNotBlank(menuDOS)) {
            // 获取子菜单列表的 ID 列表
            List<String> childrenIds = menuDOS.stream().map(MenuDO::getId).collect(Collectors.toList());

            if (!menuCommand.getIds().containsAll(childrenIds)) {
                return Result.fail("您删除的菜单存在子菜单, 请先删除子菜单", menuDOS);
            }
        }

        int delMenuNum = menuDAO.deleteBatchIds(menuCommand.getIds());
        int delRoleMenuNum = roleMenuService.deleteRoleMenu(RoleMenuCommand.builder().menuIds(menuCommand.getIds()).build());

        systemService.clearAuthoritiesByMenu(SystemCommand.builder().menuIds(menuCommand.getIds()).build());
        return Result.success("删除菜单成功", delMenuNum + delRoleMenuNum);
    }
}
