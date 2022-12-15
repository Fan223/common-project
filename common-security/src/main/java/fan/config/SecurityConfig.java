package fan.config;

import fan.filter.CaptchaFilter;
import fan.filter.JwtAuthenticationFilter;
import fan.hander.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * SpringSecurity配置类
 *
 * @author Fan
 * @since 2022/11/23 13:48
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Resource
    private UnAuthenticationEntryPoint unAuthenticationEntryPoint;

    @Resource
    private UnAccessDeniedHandler unAccessDeniedHandler;

    @Resource
    private CaptchaFilter captchaFilter;

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    public static final String[] AUTH_WHITELIST = {
            "/login",
            "/logout",
            "/api/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 关闭跨站 csrf 攻击防护
        http.csrf().disable();
        // 1. 配置权限认证
        http.authorizeRequests()
                // 放行路径, 即不需要通过登录验证就可以被访问到的资源路径
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated(); // 任何其他请求, 都需要认证
        // 2. 配置登录表单认证方式
        http.formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler);
        // 3. 添加自定义异常处理器
        http.exceptionHandling()
                .authenticationEntryPoint(unAuthenticationEntryPoint)
                .accessDeniedHandler(unAccessDeniedHandler);
        // 4. 添加验证码过滤器和 JWT 过滤器到登录认证之前
        http.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 5. 添加注销成功处理器
        http.logout().logoutSuccessHandler(customLogoutSuccessHandler);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
