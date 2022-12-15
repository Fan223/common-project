package fan.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户业务类
 *
 * @author Fan
 * since 2022/12/15 11:53
 */
@Data
public class UserBO implements Serializable {

    private static final long serialVersionUID = -1L;

    // 用户ID
    private String id;

    // 用户名
    private String username;

    // 密码
    private String password;
}
