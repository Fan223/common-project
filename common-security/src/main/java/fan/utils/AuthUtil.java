package fan.utils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import fan.entity.LoginInfoDO;
import fan.utils.collection.StringUtil;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

/**
 * 权限工具类
 *
 * @author Fan
 * @since 2022/11/27 2:23
 */
public class AuthUtil {

    private static final DatabaseReader reader;

    static {
        try {
            InputStream database = new ClassPathResource("GeoLite2-City.mmdb").getInputStream();
            reader = new DatabaseReader.Builder(database).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取 IP 地址
     *
     * @param request 请求
     * @return {@link String}
     * @author Fan
     * @since 2022/11/28 9:08
     */
    public static String getIpAddress(HttpServletRequest request) {
        // 首先, 获取 x-forwarded-for 中的 IP 地址, 它在 HTTP 扩展协议中能表示真实的客户端 IP
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (StringUtil.isNotBlank(ipAddress) && !"unknown".equalsIgnoreCase(ipAddress)) {
            // 多次反向代理后会有多个 ip 值, 第一个 ip 才是真实 ip, 例: X-Forwarded-For: client, proxy1, proxy2, proxy…
            int index = ipAddress.indexOf(",");
            if (index != -1) {
                return ipAddress.substring(0, index);
            }

            return ipAddress;
        }

        // 如果 X-Forwarded-For 获取不到, 就去获取 X-Real-IP
        ipAddress = request.getHeader("X-Real-IP");
        if (StringUtil.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            // 如果 X-Real-IP 获取不到, 就去获取 Proxy-Client-IP
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            // 如果 Proxy-Client-IP 获取不到, 就去获取 WL-Proxy-Client-IP
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            // 如果 WL-Proxy-Client-IP 获取不到, 就去获取 HTTP_CLIENT_IP
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtil.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            // 如果 HTTP_CLIENT_IP 获取不到, 就去获取 HTTP_X_FORWARDED_FOR
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtil.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            // 都获取不到, 最后才通过 request.getRemoteAddr() 获取IP
            ipAddress = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ipAddress) ? "127.0.0.1" : ipAddress;
    }

    /**
     * 通过 IP 地址获取地理信息
     *
     * @param ipAddress IP地址
     * @return {@link LoginInfoDO}
     * @author Fan
     * @since 2022/12/14 16:35
     */
    public static LoginInfoDO getGeoInformation(String ipAddress) {
        LoginInfoDO loginInfoDO = new LoginInfoDO();
        loginInfoDO.setIpAddress(ipAddress);

        try {
            // 获取 IP 地址信息
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            // 获取查询信息
            CityResponse response = reader.city(inetAddress);

            // 国家信息
            Country country = response.getCountry();
            loginInfoDO.setCountryIsoCode(country.getIsoCode());
            loginInfoDO.setCountryName(country.getName());
            loginInfoDO.setCountryZhCnName(country.getNames().get("zh-CN"));

            // 省级信息
            Subdivision subdivision = response.getMostSpecificSubdivision();
            loginInfoDO.setSubdivisionIsoCode(subdivision.getIsoCode());
            loginInfoDO.setSubdivisionName(subdivision.getName());
            loginInfoDO.setSubdivisionZhCnName(subdivision.getNames().get("zh-CN"));

            // 城市信息
            City city = response.getCity();
            loginInfoDO.setCityName(city.getName());
            loginInfoDO.setCityZhCnName(city.getNames().get("zh-CN"));

            // 邮政编码(国内的可能获取不到)
            Postal postal = response.getPostal();
            loginInfoDO.setPostal(postal.getCode());

            // 经纬度
            Location location = response.getLocation();
            loginInfoDO.setLatitude(location.getLatitude());
            loginInfoDO.setLongitude(location.getLongitude());

            return loginInfoDO;
        } catch (IOException | GeoIp2Exception exception) {
            LogUtil.error(exception.getMessage());
            return loginInfoDO;
        }
    }
}
