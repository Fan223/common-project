package fan.query;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 菜单查询参数类
 *
 * @author Fan
 * @since 2022/11/25 15:38
 */
@Data
@Builder
public class MenuQuery {

    // 菜单 ID 列表
    private List<String> menuIds;

    // 名称
    private String name;

    // 权限编码
    private String permission;

    // 菜单类型
    private List<Integer> type;

    // 有效标志
    private String flag;
}
