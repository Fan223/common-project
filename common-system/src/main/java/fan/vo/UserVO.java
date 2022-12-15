package fan.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户展示类
 *
 * @author Fan
 * since 2022/12/15 11:54
 */
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = -1L;

    // 用户ID
    private String id;

    // 用户名
    private String username;

    // 头像
    private String avatar;

    // 有效标志
    private String flag;

    // 创建时间
    private String createTime;
}
