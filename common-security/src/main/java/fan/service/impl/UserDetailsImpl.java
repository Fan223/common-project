package fan.service.impl;

import fan.bo.UserBO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 用户信息实现类
 *
 * @author Fan
 * @since 2022/11/25 16:17
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = -1L;

    private transient UserBO userBO;

    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public UserDetailsImpl(UserBO userBO, List<GrantedAuthority> grantedAuthorities) {
        this.userBO = userBO;
        this.grantedAuthorities = grantedAuthorities;
    }

    public String getRealUsername() {
        return userBO.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return userBO.getPassword();
    }

    @Override
    public String getUsername() {
        return userBO.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
