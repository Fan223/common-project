package fan.command;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色更新参数
 *
 * @author Fan
 * @since 2022/11/30 14:44
 */
@Data
public class RoleCommand implements Serializable {

    private static final long serialVersionUID = -1L;

    // 角色ID
    private String id;

    // 角色 ID 列表
    private List<String> ids;

    // 名称
    private String name;

    // 角色编码
    private String code;

    // 有效标志
    private String flag;

    // 备注
    private String remark;
}
