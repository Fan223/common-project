package fan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户实体类
 *
 * @author Fan
 * @since 2022/11/25 10:05
 */
@TableName("user")
@Data
public class UserDO implements Serializable {

    private static final long serialVersionUID = -1L;

    // 用户ID
    @TableId
    private String id;

    // 用户名
    private String username;

    // 密码
    private String password;

    // 头像
    private String avatar;

    // 有效标志
    private String flag;

    // 创建时间
    private Timestamp createTime;

    // 更新时间
    private Timestamp updateTime;
}
