package fan.query;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 角色菜单查询参数
 *
 * @author Fan
 * since 2022/12/15 14:31
 */
@Data
@Builder
public class RoleMenuQuery {

    // 角色 ID 列表
    private List<String> roleIds;

    // 有效标志
    private String flag;

    // 角色ID
    private String roleId;

    // 菜单ID
    private String menuId;

    // 菜单 ID 列表
    private List<String> menuIds;
}
