package fan.vo;

import lombok.Data;

/**
 * 登录信息展示类
 *
 * @author Fan
 * @since 2022/12/14 16:57
 */
@Data
public class LoginInfoVO {

    // 用户名
    private String username;

    // IP 地址
    private String ipAddress;

    // 操作系统
    private String operateSystem;

    // 浏览器
    private String browser;

    // 国家中文名称
    private String countryZhCnName;

    // 省份中文名称
    private String subdivisionZhCnName;

    // 城市中文名称
    private String cityZhCnName;

    // 邮政编码
    private String postal;

    // 纬度
    private double latitude;

    // 经度
    private double longitude;

    // 更新时间
    private String updateTime;
}
