package fan.command;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 系统更新参数
 *
 * @author Fan
 * since 2022/12/15 22:33
 */
@Data
@Builder
public class SystemCommand {

    // 菜单ID
    private String menuId;

    // 菜单 ID 列表
    private List<String> menuIds;

    // 角色 ID 列表
    private List<String> roleIds;

    // 角色ID
    private String roleId;
}
