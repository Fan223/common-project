package fan.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fan.bo.UserBO;
import fan.command.UserCommand;
import fan.command.UserRoleCommand;
import fan.consts.SystemConst;
import fan.dao.UserDAO;
import fan.entity.UserDO;
import fan.enums.MenuTypeEnum;
import fan.query.*;
import fan.service.*;
import fan.utils.*;
import fan.vo.MenuVO;
import fan.vo.RoleVO;
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
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    @Resource
    private SystemService systemService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private SystemMapStruct systemMapStruct;

    @Override
    public UserBO getUser(UserQuery userQuery) {
        LambdaQueryWrapper<UserDO> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(CommonUtil.isNotBlank(userQuery.getUsername()), UserDO::getUsername, userQuery.getUsername())
                .eq(CommonUtil.isNotBlank(userQuery.getFlag()), UserDO::getFlag, userQuery.getFlag());

        UserDO userDO = userDAO.selectOne(userQueryWrapper);
        return systemMapStruct.userDOToBO(userDO);
    }

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
        List<String> roleIds = systemService.listRoleIds(authKey, userId);

        if (null != roleIds) {
            // 通过获取的角色 ID 列表查询对应的角色信息
            List<RoleVO> roleVOS = CommonUtil.castToList(roleService.listRoles(RoleQuery.builder().roleIds(roleIds).build()).getData(), RoleVO.class);
            String roleAuthorities = roleVOS.stream().map(roleVO -> "ROLE_" + roleVO.getCode()).collect(Collectors.joining(","));
            authorities.append(roleAuthorities).append(",");

            // 通过获取的角色 ID 列表获取角色拥有的菜单 ID 列表
            List<String> menuIds = systemService.listMenuIds(authKey, roleIds);

            if (null != menuIds) {
                // 通过获取到的菜单 ID 列表查询对应的菜单信息, 包含全部类型菜单
                List<Integer> menuTypes = MenuTypeEnum.getTypeValues(CommonUtil.transToList(String.class, "目录", "菜单", "按钮"));
                List<MenuVO> menuVOS = menuService.listMenus(MenuQuery.builder().flag("Y").type(menuTypes).menuIds(menuIds).build());
                String menuAuthorities = menuVOS.stream().map(MenuVO::getPermission).collect(Collectors.joining(","));

                authorities.append(menuAuthorities);
            }
        }

        return authorities.toString();
    }

    @Override
    public Result pageUsers(UserQuery userQuery) {
        LambdaQueryWrapper<UserDO> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(CommonUtil.isNotBlank(userQuery.getFlag()), UserDO::getFlag, userQuery.getFlag())
                .like(CommonUtil.isNotBlank(userQuery.getUsername()), UserDO::getUsername, userQuery.getUsername());

        Page<UserDO> page = new Page<>(userQuery.getCurrentPage(), userQuery.getPageSize());
        Page<UserDO> userPage = userDAO.selectPage(page, userQueryWrapper);

        return Result.success("查询用户列表成功", systemMapStruct.pageUserDOToDO(userPage));
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

        userDO.setPassword(passwordEncoder.encode(userDO.getPassword()));
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
