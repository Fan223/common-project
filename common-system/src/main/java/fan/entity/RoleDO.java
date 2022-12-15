package fan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 角色实体类
 *
 * @author Fan
 * @since 2022/11/25 10:34
 */
@TableName("role")
@Data
public class RoleDO implements Serializable {

    private static final long serialVersionUID = -1L;

    // 角色ID
    @TableId
    private String id;

    // 名称
    private String name;

    // 编码
    private String code;

    // 有效标志
    private String flag;

    // 备注
    private String remark;

    // 创建时间
    private Timestamp createTime;

    // 更新时间
    private Timestamp updateTime;
}
