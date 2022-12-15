package fan.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用工具类
 *
 * @author Fan
 * @since 2022/11/22 16:42
 */
public class CommonUtil {

    /**
     * 判断对象是否不为空, 支持 String List Map Array Set 以及 Object 对象
     *
     * @param obj 判断对象
     * @return {@link boolean}
     * @author Fan
     * @since 2022/10/21 13:56
     */
    public static boolean isNotBlank(Object obj) {
        return !isBlank(obj);
    }

    /**
     * 判断对象是否为空, 支持 String List Map Array Set 以及 Object 对象
     *
     * @param obj 判断对象
     * @return {@link boolean}
     * @author Fan
     * @since 2022/10/21 13:57
     */
    public static boolean isBlank(Object obj) {
        if (null == obj) {
            return true;
        }

        if (obj instanceof String) {
            return StringUtils.isBlank((String) obj);
        } else if (obj instanceof List) {
            return ((List<?>) obj).isEmpty();
        } else if (obj instanceof Set) {
            return ((Set<?>) obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        } else if (obj.getClass().isArray()) {
            return ((Object[]) obj).length == 0;
        }

        return false;
    }

    /**
     * 将多个对象转为List
     *
     * @param items 可变数组
     * @return {@link List}
     * @author Fan
     * @since 2022/12/6 10:02
     */
    public static <T> List<T> transToList(Class<T> clazz, Object... items) {
        ArrayList<T> result = new ArrayList<>();

        for (Object item : items) {
            result.add(clazz.cast(item));
        }

        return result;
    }

    /**
     * 将 Object 类型的 List 对象转为对应 List 泛型类型
     *
     * @param object Object类型
     * @param clazz  泛型类型
     * @return {@link List<T>}
     * @author Fan
     * @since 2022/12/11 4:07
     */
    public static <T> List<T> castToList(Object object, Class<T> clazz) {
        List<T> result = new ArrayList<>();

        if (object instanceof List<?>) {
            for (Object obj : (List<?>) object) {
                result.add(clazz.cast(obj));
            }
        }

        return result;
    }
}
