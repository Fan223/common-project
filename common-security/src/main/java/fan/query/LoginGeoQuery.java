package fan.query;

import lombok.Builder;
import lombok.Data;

/**
 * 登录地理信息查询参数
 *
 * @author Fan
 * @since 2022/12/14 16:59
 */
@Data
@Builder
public class LoginGeoQuery extends BaseQuery{

    // 用户名
    private String username;

    // IP 地址
    private String ipAddress;
}
