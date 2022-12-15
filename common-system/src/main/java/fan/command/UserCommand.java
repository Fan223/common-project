package fan.command;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户更新参数
 *
 * @author Fan
 * @since 2022/12/5 9:45
 */
@Data
public class UserCommand implements Serializable {

    private static final long serialVersionUID = -1L;

    // 用户ID
    private String id;

    // 用户 ID 列表
    private List<String> ids;

    // 用户名
    private String username;

    // 密码
    private String password;

    // 头像
    private String avatar;

    // 有效标志
    private String flag;
}
