package fan.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志工具类
 *
 * @author Fan
 * @since 2022/11/24 10:43
 */
@Slf4j
public class LogUtil {

    /**
     * 打印 info 日志
     *
     * @param infoMsg info信息
     * @author Fan
     * @since 2022/11/25 22:49
     */
    public static void info(String infoMsg) {
        log.info(infoMsg);
    }

    /**
     * 打印 error 日志
     *
     * @param errorMsg 错误信息
     * @author Fan
     * @since 2022/11/25 22:49
     */
    public static void error(String errorMsg) {
        log.error(errorMsg);
    }
}
