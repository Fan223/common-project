package fan.service.impl;

import fan.bo.UserBO;
import fan.query.UserQuery;
import fan.service.SystemService;
import fan.service.UserService;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户信息接口实现类
 *
 * @author Fan
 * @since 2022/11/25 9:36
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    @Resource
    private SystemService systemService;

    /**
     * 根据用户名从数据库获取用户信息和用户权限信息, 校验用户
     *
     * @param username 用户名
     * @return {@link UserDetails}
     * @author Fan
     * @since 2022/11/25 16:19
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        // 查询用户信息
        UserBO userBO = userService.getUser(UserQuery.builder().flag("Y").username(username).build());
        if (null == userBO) {
            throw new InternalAuthenticationServiceException("用户名不存在");
        }

        // 查询用户权限信息
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
                systemService.getAuthorities(userBO.getId()));

        return new UserDetailsImpl(userBO, grantedAuthorities);
    }
}
