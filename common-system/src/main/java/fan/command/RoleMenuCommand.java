package fan.command;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 角色菜单关联更新参数
 *
 * @author Fan
 * @since 2022/11/30 15:23
 */
@Data
@Builder
public class RoleMenuCommand {

    // 菜单 ID 列表
    List<String> menuIds;

    // 角色ID
    private String roleId;

    // 角色 ID 列表
    List<String> roleIds;

    // 菜单ID
    private String menuId;

    // 有效标志
    private String flag;
}
