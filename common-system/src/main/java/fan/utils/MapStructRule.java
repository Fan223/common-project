package fan.utils;

import fan.utils.collection.StringUtil;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Mapstruct 规则转换类
 *
 * @author Fan
 * @since 2022/11/22 16:26
 */
@Component
public class MapStructRule {

    /**
     * Timestamp 类型转String
     *
     * @param timestamp 日期
     * @return {@link String}
     * @author Fan
     * @since 2022/11/27 6:12
     */
    @Named("stampToString")
    public String stampToString(Timestamp timestamp) {
        if (null == timestamp) {
            return "时间为空";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(timestamp);
    }
}
