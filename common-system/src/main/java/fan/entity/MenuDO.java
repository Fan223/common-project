package fan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 菜单实体类
 *
 * @author Fan
 * @since 2022/11/22 13:49
 */
@TableName("menu")
@Data
public class MenuDO implements Serializable {

    private static final long serialVersionUID = -1L;

    // 菜单ID
    @TableId
    private String id;

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

    // 创建时间
    private Timestamp createTime;

    // 更新时间
    private Timestamp updateTime;
}
