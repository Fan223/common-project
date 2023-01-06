package fan.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fan.base.Response;
import fan.bo.UserBO;
import fan.bo.UserRoleBO;
import fan.command.UserCommand;
import fan.command.UserRoleCommand;
import fan.dao.UserDAO;
import fan.entity.UserDO;
import fan.query.*;
import fan.service.*;
import fan.utils.*;
import fan.utils.collection.ListUtil;
import fan.utils.collection.StringUtil;
import fan.vo.RoleVO;
import fan.vo.UserVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
        userQueryWrapper.eq(StringUtil.isNotBlank(userQuery.getUserId()), UserDO::getId, userQuery.getUserId())
                .eq(StringUtil.isNotBlank(userQuery.getUsername()), UserDO::getUsername, userQuery.getUsername())
                .eq(StringUtil.isNotBlank(userQuery.getFlag()), UserDO::getFlag, userQuery.getFlag());

        UserDO userDO = userDAO.selectOne(userQueryWrapper);
        return systemMapStruct.userDOToBO(userDO);
    }

    @Override
    public Response pageUsers(UserQuery userQuery) {
        LambdaQueryWrapper<UserDO> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(StringUtil.isNotBlank(userQuery.getFlag()), UserDO::getFlag, userQuery.getFlag())
                .like(StringUtil.isNotBlank(userQuery.getUsername()), UserDO::getUsername, userQuery.getUsername());

        Page<UserDO> page = new Page<>(userQuery.getCurrentPage(), userQuery.getPageSize());
        Page<UserDO> userDOPage = userDAO.selectPage(page, userQueryWrapper);

        List<String> userIds = userDOPage.getRecords().stream().map(UserDO::getId).collect(Collectors.toList());
        List<UserRoleBO> userRoleBOS = userRoleService.listUserRoles(UserRoleQuery.builder().userIds(userIds).build());

        Map<String, List<UserRoleBO>> userRoleBOMap = userRoleBOS.stream().collect(Collectors.groupingBy(UserRoleBO::getUserId));
        List<String> roleIds = userRoleBOS.stream().map(UserRoleBO::getRoleId).collect(Collectors.toList());
        List<RoleVO> roleVOS = ListUtil.castToList(RoleVO.class, roleService.listRoles(RoleQuery.builder().roleIds(roleIds).build()).getData());

        Page<UserVO> userVOPage = systemMapStruct.pageUserDOToVO(userDOPage);
        for (UserVO userVO : userVOPage.getRecords()) {
            List<String> tempRoleIds = userRoleBOMap.get(userVO.getId()).stream().map(UserRoleBO::getRoleId).collect(Collectors.toList());
            userVO.setRoleIds(tempRoleIds);
            for (RoleVO roleVO : roleVOS) {
                if (tempRoleIds.contains(roleVO.getId())) {
                    userVO.getRoleNames().add(roleVO.getName());
                }
            }
        }

        return Response.success("查询用户列表成功", userVOPage);
    }

    @Transactional
    @Override
    public Response addUser(UserCommand userCommand) {
        UserBO userBO = getUser(UserQuery.builder().username(userCommand.getUsername()).build());
        if (null != userBO) {
            return Response.fail("用户名已存在, 请重新输入新的用户名", null);
        }

        UserDO userDO = systemMapStruct.userCommandToDO(userCommand);

        String userId = UUID.randomUUID().toString();

        userDO.setId(userId);
        userDO.setPassword(passwordEncoder.encode(userDO.getPassword()));
        userDO.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        userDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

        int userInsert = userDAO.insert(userDO);
        int userRoleInsert = userRoleService.addUserRole(userId);

        return Response.success("添加用户成功", userInsert + userRoleInsert);
    }

    @Override
    public Response updateUser(UserCommand userCommand) {
        UserBO userBO = getUser(UserQuery.builder().username(userCommand.getUsername()).build());
        if (null != userBO && !userBO.getId().equals(userCommand.getId())) {
            return Response.fail("用户名已存在, 请重新输入新的用户名", null);
        }

        UserDO userDO = systemMapStruct.userCommandToDO(userCommand);

        if (StringUtil.isNotBlank(userCommand.getPassword())) {
            userDO.setPassword(passwordEncoder.encode(userDO.getPassword()));
        }
        userDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        return Response.success("修改用户成功", userDAO.updateById(userDO));
    }

    @Transactional
    @Override
    public Response deleteUser(UserCommand userCommand, String userId) {
        if (userCommand.getIds().contains(userId)) {
            return Response.fail("删除的用户中包含当前登录用户, 不允许删除", null);
        }

        int deleteUserNum = userDAO.deleteBatchIds(userCommand.getIds());
        int deleteUserRoleNum = userRoleService.deleteUserRole(UserRoleCommand.builder().userIds(userCommand.getIds()).build());

        return Response.success("删除用户成功", deleteUserNum + deleteUserRoleNum);
    }
}
