package fan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import fan.command.RoleMenuCommand;
import fan.command.SystemCommand;
import fan.dao.RoleMenuDAO;
import fan.entity.RoleMenuDO;
import fan.query.RoleMenuQuery;
import fan.service.RoleMenuService;
import fan.service.SystemService;
import fan.utils.CommonUtil;
import fan.utils.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 角色菜单关联接口实现类
 *
 * @author Fan
 * @since 2022/11/25 15:17
 */
@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    @Resource
    private RoleMenuDAO roleMenuDAO;

    @Resource
    private SystemService systemService;

    @Override
    public List<String> listMenuIds(RoleMenuQuery roleMenuQuery) {
        LambdaQueryWrapper<RoleMenuDO> roleMenuQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuQueryWrapper.eq(CommonUtil.isNotBlank(roleMenuQuery.getRoleId()), RoleMenuDO::getRoleId, roleMenuQuery.getRoleId())
                .eq(CommonUtil.isNotBlank(roleMenuQuery.getFlag()), RoleMenuDO::getFlag, roleMenuQuery.getFlag())
                .in(CommonUtil.isNotBlank(roleMenuQuery.getRoleIds()), RoleMenuDO::getRoleId, roleMenuQuery.getRoleIds());

        List<RoleMenuDO> roleMenuDOS = roleMenuDAO.selectList(roleMenuQueryWrapper);
        if (CommonUtil.isBlank(roleMenuDOS)) {
            return null;
        }

        return roleMenuDOS.stream().map(RoleMenuDO::getMenuId).collect(Collectors.toList());
    }

    @Override
    public int deleteRoleMenu(RoleMenuCommand roleMenuCommand) {
        LambdaQueryWrapper<RoleMenuDO> roleMenuDeleteWrapper = new LambdaQueryWrapper<>();

        roleMenuDeleteWrapper.eq(CommonUtil.isNotBlank(roleMenuCommand.getRoleId()), RoleMenuDO::getRoleId, roleMenuCommand.getRoleId())
                .in(CommonUtil.isNotBlank(roleMenuCommand.getMenuIds()), RoleMenuDO::getMenuId, roleMenuCommand.getMenuIds())
                .in(CommonUtil.isNotBlank(roleMenuCommand.getRoleIds()), RoleMenuDO::getRoleId, roleMenuCommand.getRoleIds());

        return roleMenuDAO.delete(roleMenuDeleteWrapper);
    }

    @Transactional
    @Override
    public Result assignPermissions(String roleId, RoleMenuCommand roleMenuCommand) {
        // 先删除该角色原来的菜单权限
        deleteRoleMenu(RoleMenuCommand.builder().roleId(roleId).build());

        // 再重新添加新的菜单权限
        RoleMenuDO roleMenuDO = new RoleMenuDO();
        roleMenuDO.setRoleId(roleId);
        roleMenuDO.setFlag("Y");
        roleMenuDO.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        roleMenuDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

        for (String menuId : roleMenuCommand.getMenuIds()) {
            roleMenuDO.setId(UUID.randomUUID().toString());
            roleMenuDO.setMenuId(menuId);

            roleMenuDAO.insert(roleMenuDO);
        }

        systemService.clearAuthoritiesByRole(SystemCommand.builder().roleId(roleId).build());
        return Result.success("分配菜单权限成功", roleMenuCommand.getMenuIds().size());
    }

    @Override
    public List<String> listRoleIds(RoleMenuQuery roleMenuQuery) {
        LambdaQueryWrapper<RoleMenuDO> roleMenuDOQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuDOQueryWrapper.eq(CommonUtil.isNotBlank(roleMenuQuery.getMenuId()), RoleMenuDO::getMenuId, roleMenuQuery.getMenuId())
                .in(CommonUtil.isNotBlank(roleMenuQuery.getMenuIds()), RoleMenuDO::getMenuId, roleMenuQuery.getMenuIds());

        List<RoleMenuDO> roleMenuDOS = roleMenuDAO.selectList(roleMenuDOQueryWrapper);
        return roleMenuDOS.stream().map(RoleMenuDO::getRoleId).distinct().collect(Collectors.toList());
    }

    @Override
    public int updateRoleMenu(RoleMenuCommand roleMenuCommand) {
        LambdaUpdateWrapper<RoleMenuDO> roleMenuDOUpdateWrapper = new LambdaUpdateWrapper<>();
        roleMenuDOUpdateWrapper.set(RoleMenuDO::getFlag, roleMenuCommand.getFlag())
                .eq(CommonUtil.isNotBlank(roleMenuCommand.getRoleId()), RoleMenuDO::getRoleId, roleMenuCommand.getRoleId())
                .eq(CommonUtil.isNotBlank(roleMenuCommand.getMenuId()), RoleMenuDO::getMenuId, roleMenuCommand.getMenuId());

        return roleMenuDAO.update(new RoleMenuDO(), roleMenuDOUpdateWrapper);
    }
}
