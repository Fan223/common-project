package fan.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单展示类
 *
 * @author Fan
 * @since 2022/11/22 16:05
 */
@Data
public class MenuVO implements Serializable {

    private static final long serialVersionUID = -1L;

    // 菜单ID
    private String id;

    // 父菜单ID
    private String parentId;

    // 父菜单名称
    private String parentName;

    // 菜单名称
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

    // 创建时间
    private String createTime;

    // 子菜单
    private List<MenuVO> children = new ArrayList<>();
}
