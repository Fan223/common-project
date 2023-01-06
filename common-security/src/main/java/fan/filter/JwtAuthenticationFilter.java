package fan.filter;

import fan.consts.AuthConst;
import fan.exception.CustomException;
import fan.hander.UnAuthenticationEntryPoint;
import fan.service.SystemService;

import fan.utils.JwtUtil;
import fan.utils.LogUtil;
import fan.utils.collection.StringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * JWT过滤器
 *
 * @author Fan
 * @since 2022/11/26 23:44
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private SystemService systemService;

    @Resource
    private UnAuthenticationEntryPoint unAuthenticationEntryPoint;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        LogUtil.info("JwtAuthenticationFilter-JWT 过滤器");

        // 登录/登出请求或 Api 请求不需要检验JWT
        if (request.getRequestURI().startsWith(contextPath + "/api") || request.getRequestURI().startsWith(contextPath + "/logout") ||
                (request.getRequestURI().equals(contextPath + "/login") && request.getMethod().equals("POST"))) {

            filterChain.doFilter(request, response);
            return;
        }

        String jwt = request.getHeader(AuthConst.AUTH_KEY);
        if (StringUtil.isBlank(jwt)) {
            LogUtil.error("JWT为空");
            filterChain.doFilter(request, response);
            return;
        }

        // 解析JWT
        Jws<Claims> claimsJws = JwtUtil.parseJwt(jwt);
        if (null == claimsJws) {
            unAuthenticationEntryPoint.commence(request, response, new CustomException("JWT解析异常"));
            return;
        }

        // 通过 JWT 中存储的用户ID和用户名获取权限
        String userId = claimsJws.getBody().getSubject();

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
                systemService.getAuthorities(userId));

        // 将权限存入上下文
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userId, claimsJws, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);
    }
}
