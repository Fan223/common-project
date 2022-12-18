package fan.query;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 用户角色查询参数
 *
 * @author Fan
 * since 2022/12/15 14:21
 */
@Data
@Builder
public class UserRoleQuery {

    // 用户ID
    private String userId;

    // 用户 ID 列表
    private List<String> userIds;

    // 有效标志
    private String flag;

    // 角色ID
    private String roleId;

    // 角色 ID 列表
    private List<String> roleIds;
}
