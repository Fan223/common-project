package fan.command;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 用户角色关联更新参数
 *
 * @author Fan
 * @since 2022/12/5 13:45
 */
@Data
@Builder
public class UserRoleCommand {

    // 用户ID
    private String userId;

    // 用户 ID 列表
    private List<String> userIds;

    // 角色 ID 列表
    private List<String> roleIds;

    // 有效标志
    private String flag;

    // 角色ID
    private String roleId;
}
