package fan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 登录信息实体类
 *
 * @author Fan
 * @since 2022/12/14 15:47
 */
@TableName("login_info")
@Data
public class LoginInfoDO implements Serializable {

    private static final long serialVersionUID = -1L;

    // 主键ID
    private String id;

    // 用户名
    private String username;

    // 操作系统
    private String operateSystem;

    // 浏览器
    private String browser;

    // IP 地址
    private String ipAddress;

    // 国家 ISO 代码
    private String countryIsoCode;

    // 国家名称
    private String countryName;

    // 国家中文名称
    private String countryZhCnName;

    // 省级 ISO 代码
    private String subdivisionIsoCode;

    // 省级名称
    private String subdivisionName;

    // 省级中文名称
    private String subdivisionZhCnName;

    // 城市名称
    private String cityName;

    // 城市中文名称
    private String cityZhCnName;

    // 邮政编码
    private String postal;

    // 纬度
    private double latitude;

    // 经度
    private double longitude;

    // 创建时间
    private Timestamp createTime;

    // 更新时间
    private Timestamp updateTime;
}
