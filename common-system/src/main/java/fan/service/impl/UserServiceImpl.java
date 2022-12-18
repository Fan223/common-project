package fan.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fan.bo.UserBO;
import fan.bo.UserRoleBO;
import fan.command.UserCommand;
import fan.command.UserRoleCommand;
import fan.dao.UserDAO;
import fan.entity.UserDO;
import fan.query.*;
import fan.service.*;
import fan.utils.*;
import fan.vo.RoleVO;
import fan.vo.UserVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口实现类
 *
 * @author Fan
 * @since 2022/11/25 10:44
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDAO userDAO;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private SystemMapStruct systemMapStruct;

    @Resource
    private RoleService roleService;

    @Override
    public UserBO getUser(UserQuery userQuery) {
        LambdaQueryWrapper<UserDO> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(CommonUtil.isNotBlank(userQuery.getUsername()), UserDO::getUsername, userQuery.getUsername())
                .eq(CommonUtil.isNotBlank(userQuery.getFlag()), UserDO::getFlag, userQuery.getFlag());

        UserDO userDO = userDAO.selectOne(userQueryWrapper);
        return systemMapStruct.userDOToBO(userDO);
    }

    @Override
    public Result pageUsers(UserQuery userQuery) {
        LambdaQueryWrapper<UserDO> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(CommonUtil.isNotBlank(userQuery.getFlag()), UserDO::getFlag, userQuery.getFlag())
                .like(CommonUtil.isNotBlank(userQuery.getUsername()), UserDO::getUsername, userQuery.getUsername());

        Page<UserDO> page = new Page<>(userQuery.getCurrentPage(), userQuery.getPageSize());
        Page<UserDO> userDOPage = userDAO.selectPage(page, userQueryWrapper);

        Page<UserVO> userVOPage = systemMapStruct.pageUserDOToDO(userDOPage);

        for (UserVO userVO : userVOPage.getRecords()) {
            List<UserRoleBO> userRoleBOS = userRoleService.listUserRoles(UserRoleQuery.builder().userId(userVO.getId()).build());
            List<String> roleIds = userRoleBOS.stream().map(UserRoleBO::getRoleId).collect(Collectors.toList());

            List<RoleVO> roleVOS = CommonUtil.castToList(roleService.listRoles(RoleQuery.builder().roleIds(roleIds).build()).getData(), RoleVO.class);
            userVO.setRoleIds(roleVOS.stream().map(RoleVO::getId).collect(Collectors.toList()));
            userVO.setRoleNames(roleVOS.stream().map(RoleVO::getName).collect(Collectors.toList()));
        }

        return Result.success("查询用户列表成功", userVOPage);
    }

    /**
     * 通过用户名验证用户是否存在
     *
     * @param username 用户名
     * @return {@link boolean}
     * @author Fan
     * @since 2022/12/16 1:27
     */
    private boolean validateUser(String username) {
        UserBO userBO = getUser(UserQuery.builder().username(username).flag("Y").build());

        return CommonUtil.isNotBlank(userBO);
    }

    @Transactional
    @Override
    public Result addUser(UserCommand userCommand) {
        if (validateUser(userCommand.getUsername())) {
            return Result.fail("用户名已存在, 请重新输入新的用户名", null);
        }

        UserDO userDO = systemMapStruct.userCommandToDO(userCommand);

        String userId = UUID.randomUUID().toString();

        userDO.setId(userId);
        userDO.setPassword(passwordEncoder.encode(userDO.getPassword()));
        userDO.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        userDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

        int userInsert = userDAO.insert(userDO);
        int userRoleInsert = userRoleService.addUserRole(userId);

        return Result.success("添加用户成功", userInsert + userRoleInsert);
    }

    @Override
    public Result updateUser(UserCommand userCommand) {
        if (validateUser(userCommand.getUsername())) {
            return Result.fail("用户名已存在, 请重新输入新的用户名", null);
        }

        UserDO userDO = systemMapStruct.userCommandToDO(userCommand);

        if (CommonUtil.isNotBlank(userCommand.getPassword())) {
            userDO.setPassword(passwordEncoder.encode(userDO.getPassword()));
        }
        userDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        return Result.success("修改用户成功", userDAO.updateById(userDO));
    }

    @Transactional
    @Override
    public Result deleteUser(UserCommand userCommand, String userId) {
        if (userCommand.getIds().contains(userId)) {
            return Result.fail("删除的用户中包含当前登录用户, 不允许删除", null);
        }

        int deleteUserNum = userDAO.deleteBatchIds(userCommand.getIds());
        int deleteUserRoleNum = userRoleService.deleteUserRole(UserRoleCommand.builder().userIds(userCommand.getIds()).build());

        return Result.success("删除用户成功", deleteUserNum + deleteUserRoleNum);
    }
}
