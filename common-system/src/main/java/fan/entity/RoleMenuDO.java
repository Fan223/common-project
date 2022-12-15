package fan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 角色菜单关联实体类
 *
 * @author Fan
 * @since 2022/11/25 10:41
 */
@TableName("role_menu")
@Data
public class RoleMenuDO implements Serializable {

    private static final long serialVersionUID = -1L;

    // 主键ID
    @TableId
    private String id;

    // 角色ID
    private String roleId;

    // 菜单ID
    private String menuId;

    // 有效标志
    private String flag;

    // 创建时间
    private Timestamp createTime;

    // 更新时间
    private Timestamp updateTime;
}
