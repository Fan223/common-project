package fan.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    // 角色 ID 列表
    private List<String> roleIds;

    // 角色名称列表
    private List<String> roleNames = new ArrayList<>();

    // 头像
    private String avatar;

    // 有效标志
    private String flag;

    // 创建时间
    private String createTime;
}
