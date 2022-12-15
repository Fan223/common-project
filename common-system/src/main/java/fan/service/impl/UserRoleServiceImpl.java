package fan.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import fan.command.UserRoleCommand;
import fan.consts.SystemConst;
import fan.dao.UserRoleDAO;
import fan.entity.UserRoleDO;
import fan.query.UserRoleQuery;
import fan.service.UserRoleService;
import fan.utils.CommonUtil;
import fan.utils.RedisUtil;
import fan.utils.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<String> listRoleIds(UserRoleQuery userRoleQuery) {
        LambdaQueryWrapper<UserRoleDO> userRoleQueryWrapper = new LambdaQueryWrapper<>();
        userRoleQueryWrapper.eq(UserRoleDO::getUserId, userRoleQuery.getUserId())
                .eq(CommonUtil.isNotBlank(userRoleQuery.getFlag()), UserRoleDO::getFlag, userRoleQuery.getFlag());

        List<UserRoleDO> userRoleDOS = userRoleDAO.selectList(userRoleQueryWrapper);
        if (CommonUtil.isBlank(userRoleDOS)) {
            return null;
        }

        return userRoleDOS.stream().map(UserRoleDO::getRoleId).collect(Collectors.toList());
    }

    @Override
    public int deleteUserRole(UserRoleCommand userRoleCommand) {
        LambdaQueryWrapper<UserRoleDO> userRoleDeleteWrapper = new LambdaQueryWrapper<>();

        userRoleDeleteWrapper.eq(CommonUtil.isNotBlank(userRoleCommand.getUserId()), UserRoleDO::getUserId, userRoleCommand.getUserId())
                .in(CommonUtil.isNotBlank(userRoleCommand.getRoleIds()), UserRoleDO::getRoleId, userRoleCommand.getRoleIds())
                .in(CommonUtil.isNotBlank(userRoleCommand.getUserIds()), UserRoleDO::getUserId, userRoleCommand.getUserIds());

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
    public Result assignRoles(String userId, UserRoleCommand userRoleCommand) {
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
        return Result.success("分配角色成功", userRoleCommand.getRoleIds().size());
    }

    @Override
    public List<String> listUserIds(UserRoleQuery userRoleQuery) {
        LambdaQueryWrapper<UserRoleDO> userRoleDOQueryWrapper = new LambdaQueryWrapper<>();
        userRoleDOQueryWrapper.eq(CommonUtil.isNotBlank(userRoleQuery.getRoleId()), UserRoleDO::getRoleId, userRoleQuery.getRoleId())
                .in(CommonUtil.isNotBlank(userRoleQuery.getRoleIds()), UserRoleDO::getRoleId, userRoleQuery.getRoleIds());

        List<UserRoleDO> userRoleDOS = userRoleDAO.selectList(userRoleDOQueryWrapper);
        return userRoleDOS.stream().map(UserRoleDO::getUserId).distinct().collect(Collectors.toList());
    }

    @Override
    public int updateUserRole(UserRoleCommand userRoleCommand) {
        LambdaUpdateWrapper<UserRoleDO> userRoleDOUpdateWrapper = new LambdaUpdateWrapper<>();
        userRoleDOUpdateWrapper.set(UserRoleDO::getFlag, userRoleCommand.getFlag())
                .eq(UserRoleDO::getRoleId, userRoleCommand.getRoleId());

        return userRoleDAO.update(new UserRoleDO(), userRoleDOUpdateWrapper);
    }
}
