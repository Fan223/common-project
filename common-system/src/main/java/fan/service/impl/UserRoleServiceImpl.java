package fan.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import fan.bo.UserRoleBO;
import fan.command.UserRoleCommand;
import fan.consts.SystemConst;
import fan.dao.UserRoleDAO;
import fan.entity.UserRoleDO;
import fan.query.UserRoleQuery;
import fan.service.UserRoleService;

import fan.utils.RedisUtil;
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

/**
 * 用户角色关联接口实现类
 *
 * @author Fan
 * @since 2022/11/25 14:12
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private UserRoleDAO userRoleDAO;

    @Resource
    private SystemMapStruct systemMapStruct;

    @Override
    public List<UserRoleBO> listUserRoles(UserRoleQuery userRoleQuery) {
        LambdaQueryWrapper<UserRoleDO> userRoleQueryWrapper = new LambdaQueryWrapper<>();
        userRoleQueryWrapper.eq(StringUtil.isNotBlank(userRoleQuery.getUserId()), UserRoleDO::getUserId, userRoleQuery.getUserId())
                .eq(StringUtil.isNotBlank(userRoleQuery.getRoleId()), UserRoleDO::getRoleId, userRoleQuery.getRoleId())
                .eq(StringUtil.isNotBlank(userRoleQuery.getFlag()), UserRoleDO::getFlag, userRoleQuery.getFlag())
                .in(ListUtil.isNotEmpty(userRoleQuery.getUserIds()), UserRoleDO::getUserId, userRoleQuery.getUserIds())
                .in(ListUtil.isNotEmpty(userRoleQuery.getRoleIds()), UserRoleDO::getRoleId, userRoleQuery.getRoleIds());

        List<UserRoleDO> userRoleDOS = userRoleDAO.selectList(userRoleQueryWrapper);
        if (ListUtil.isEmpty(userRoleDOS)) {
            return null;
        }

        return systemMapStruct.userRoleDOSToBOs(userRoleDOS);
    }

    @Override
    public int deleteUserRole(UserRoleCommand userRoleCommand) {
        LambdaQueryWrapper<UserRoleDO> userRoleDeleteWrapper = new LambdaQueryWrapper<>();

        userRoleDeleteWrapper.eq(StringUtil.isNotBlank(userRoleCommand.getUserId()), UserRoleDO::getUserId, userRoleCommand.getUserId())
                .in(ListUtil.isNotEmpty(userRoleCommand.getRoleIds()), UserRoleDO::getRoleId, userRoleCommand.getRoleIds())
                .in(ListUtil.isNotEmpty(userRoleCommand.getUserIds()), UserRoleDO::getUserId, userRoleCommand.getUserIds());

        return userRoleDAO.delete(userRoleDeleteWrapper);
    }

    @Override
    public int addUserRole(String userId) {
        UserRoleDO userRoleDO = new UserRoleDO();

        userRoleDO.setId(UUID.randomUUID().toString());
        userRoleDO.setUserId(userId);
        userRoleDO.setRoleId("base");
        userRoleDO.setFlag("Y");
        userRoleDO.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        userRoleDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

        return userRoleDAO.insert(userRoleDO);
    }

    @Transactional
    @Override
    public Response assignRoles(String userId, UserRoleCommand userRoleCommand) {
        // 先删除原有的角色
        deleteUserRole(UserRoleCommand.builder().userId(userId).build());

        // 再重新添加新的角色
        UserRoleDO userRoleDO = new UserRoleDO();
        userRoleDO.setUserId(userId);
        userRoleDO.setFlag("Y");
        userRoleDO.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        userRoleDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

        for (String roleId : userRoleCommand.getRoleIds()) {
            userRoleDO.setId(UUID.randomUUID().toString());
            userRoleDO.setRoleId(roleId);

            userRoleDAO.insert(userRoleDO);
        }

        RedisUtil.del(SystemConst.AUTHENTICATION + ":" + userId);
        return Response.success("分配角色成功", userRoleCommand.getRoleIds().size());
    }

    @Override
    public int updateUserRole(UserRoleCommand userRoleCommand) {
        LambdaUpdateWrapper<UserRoleDO> userRoleDOUpdateWrapper = new LambdaUpdateWrapper<>();
        userRoleDOUpdateWrapper.set(UserRoleDO::getFlag, userRoleCommand.getFlag())
                .eq(UserRoleDO::getRoleId, userRoleCommand.getRoleId());

        return userRoleDAO.update(new UserRoleDO(), userRoleDOUpdateWrapper);
    }
}
