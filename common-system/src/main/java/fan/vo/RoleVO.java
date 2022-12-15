package fan.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色展示类
 *
 * @author Fan
 * @since 2022/11/25 10:56
 */
@Data
public class RoleVO implements Serializable {

    private static final long serialVersionUID = -1L;

    // 角色ID
    private String id;

    // 角色名称
    private String name;

    // 角色编码
    private String code;

    // 有效标志
    private String flag;

    // 备注
    private String remark;

    // 创建时间
    private String createTime;
}
