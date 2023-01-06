package fan.query;

import fan.base.BaseQuery;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 角色查询参数
 *
 * @author Fan
 * @since 2022/11/25 14:10
 */
@Data
@Builder
public class RoleQuery extends BaseQuery {

    // 角色 ID 列表
    private List<String> roleIds;

    // 角色名称
    private String name;

    // 角色编码
    private String code;

    // 有效标志
    private String flag;
}
