package fan.command;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单更新参数
 *
 * @author Fan
 * @since 2022/11/29 17:11
 */
@Data
public class MenuCommand implements Serializable {

    private static final long serialVersionUID = -1L;

    // 菜单ID
    private String id;

    // 菜单 ID 列表
    private List<String> ids;

    // 父菜单ID
    private String parentId;

    // 名称
    private String name;

    // 路径
    private String path;

    // 权限编码
    private String permission;

    // 组件
    private String component;

    // 类型
    private int type;

    // 图标
    private String icon;

    // 排序号
    private int orderNum;

    // 有效标志
    private String flag;
}
