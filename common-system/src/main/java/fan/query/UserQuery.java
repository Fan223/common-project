package fan.query;

import fan.base.BaseQuery;
import lombok.Builder;
import lombok.Data;

/**
 * 用户查询参数类
 *
 * @author Fan
 * @since 2022/11/25 11:51
 */
@Data
@Builder
public class UserQuery extends BaseQuery {

    // 用户名
    private String username;

    // 有效标志
    private String flag;

    // 用户ID
    private String userId;
}
