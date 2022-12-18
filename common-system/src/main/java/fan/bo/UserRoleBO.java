package fan.bo;

import lombok.Data;

/**
 * 用户角色业务类
 *
 * @author Fan
 * @since 2022/12/18 2:15
 */
@Data
public class UserRoleBO {

    private static final long serialVersionUID = -1L;

    // 用户ID
    private String userId;

    // 角色ID
    private String roleId;
}
