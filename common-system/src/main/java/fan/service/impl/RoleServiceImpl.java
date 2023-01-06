package fan.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fan.command.RoleCommand;
import fan.command.RoleMenuCommand;
import fan.command.SystemCommand;
import fan.command.UserRoleCommand;
import fan.dao.RoleDAO;
import fan.entity.RoleDO;
import fan.query.RoleQuery;
import fan.service.RoleMenuService;
import fan.service.RoleService;
import fan.service.SystemService;
import fan.service.UserRoleService;

import fan.base.Response;
import fan.utils.SystemMapStruct;
import fan.utils.collection.ListUtil;
import fan.utils.collection.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色接口实现类
 *
 * @author Fan
 * @since 2022/11/25 10:49
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDAO roleDAO;

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private SystemMapStruct systemMapStruct;

    @Resource
    private SystemService systemService;

    @Override
    public Response listRoles(RoleQuery roleQuery) {
        List<RoleDO> roleDOS = roleDAO.selectList(getRoleQueryWrapper(roleQuery));

        return Response.success("获取角色列表成功", roleDOS.stream().map(roleDO -> systemMapStruct.roleDOToVO(roleDO))
                .collect(Collectors.toList()));
    }

    private LambdaQueryWrapper<RoleDO> getRoleQueryWrapper(RoleQuery roleQuery) {
        LambdaQueryWrapper<RoleDO> roleQueryWrapper = new LambdaQueryWrapper<>();

        roleQueryWrapper.eq(StringUtil.isNotBlank(roleQuery.getFlag()), RoleDO::getFlag, roleQuery.getFlag())
                .in(ListUtil.isNotEmpty(roleQuery.getRoleIds()), RoleDO::getId, roleQuery.getRoleIds())
                .like(StringUtil.isNotBlank(roleQuery.getName()), RoleDO::getName, roleQuery.getName())
                .like(StringUtil.isNotBlank(roleQuery.getCode()), RoleDO::getCode, roleQuery.getCode());

        return roleQueryWrapper;
    }

    @Override
    public Response pageRoles(RoleQuery roleQuery) {
        Page<RoleDO> page = new Page<>(roleQuery.getCurrentPage(), roleQuery.getPageSize());
        Page<RoleDO> rolePage = roleDAO.selectPage(page, getRoleQueryWrapper(roleQuery));

        return Response.success("分页获取角色列表成功", systemMapStruct.pageRoleDOToVO(rolePage));
    }

    @Override
    public Response addRole(RoleCommand roleCommand) {
        RoleDO roleDO = systemMapStruct.roleCommandToDO(roleCommand);

        roleDO.setId(UUID.randomUUID().toString());
        roleDO.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        roleDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

        return Response.success("添加角色成功", roleDAO.insert(roleDO));
    }

    /**
     * 通过角色 ID 获取角色信息
     *
     * @param roleId 角色ID
     * @return {@link RoleDO}
     * @author Fan
     * @since 2022/12/16 1:28
     */
    private RoleDO getRole(String roleId) {
        return roleDAO.selectById(roleId);
    }

    @Transactional
    @Override
    public Response updateRole(RoleCommand roleCommand) {
        if ("root".equals(roleCommand.getId()) && !("root".equals(roleCommand.getCode()) && "Y".equals(roleCommand.getFlag()))
                || "base".equals(roleCommand.getId()) && !("base".equals(roleCommand.getCode()) && "Y".equals(roleCommand.getFlag()))) {
            return Response.fail("超级管理员或基础角色, 不允许修改角色编码或禁用", "root");
        }

        RoleDO role = getRole(roleCommand.getId());

        RoleDO roleDO = systemMapStruct.roleCommandToDO(roleCommand);
        roleDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        int updateNum = roleDAO.updateById(roleDO);

        if (!roleCommand.getFlag().equals(role.getFlag())) {
            updateNum += roleMenuService.updateRoleMenu(RoleMenuCommand.builder().roleId(roleCommand.getId()).flag(roleCommand.getFlag()).build());
            updateNum += userRoleService.updateUserRole(UserRoleCommand.builder().roleId(roleCommand.getId()).flag(roleCommand.getFlag()).build());

            systemService.clearAuthoritiesByRole(SystemCommand.builder().roleId(roleCommand.getId()).build());
        } else if (!roleCommand.getCode().equals(role.getCode())) {
            systemService.clearAuthoritiesByRole(SystemCommand.builder().roleId(roleCommand.getId()).build());
        }

        return Response.success("修改角色成功", updateNum);
    }

    @Transactional
    @Override
    public Response deleteRole(RoleCommand roleCommand) {
        if (roleCommand.getIds().contains("root") || roleCommand.getIds().contains("base")) {
            return Response.fail("超级管理员或基础角色, 不允许删除", "root");
        }

        int deleteRoleNum = roleDAO.deleteBatchIds(roleCommand.getIds());
        int deleteRoleMenuNum = roleMenuService.deleteRoleMenu(RoleMenuCommand.builder().roleIds(roleCommand.getIds()).build());
        int deleteUserRoleNum = userRoleService.deleteUserRole(UserRoleCommand.builder().roleIds(roleCommand.getIds()).build());

        systemService.clearAuthoritiesByRole(SystemCommand.builder().roleIds(roleCommand.getIds()).build());

        return Response.success("删除角色成功", deleteRoleNum + deleteRoleMenuNum + deleteUserRoleNum);
    }
}
