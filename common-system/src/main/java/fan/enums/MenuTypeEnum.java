package fan.enums;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单枚举类
 *
 * @author Fan
 * @since 2022/12/11 2:22
 */
@AllArgsConstructor
public enum MenuTypeEnum {

    ONE("目录", 1),

    TWO("菜单", 2),

    THREE("按钮", 3);

    private final String type;

    private final Integer value;

    /**
     * 通过菜单类型列表获取对应的值
     *
     * @param types 菜单类型列表
     * @return {@link List<Integer>}
     * @author Fan
     * @since 2022/12/11 3:51
     */
    public static List<Integer> getTypeValues(List<String> types) {
        List<Integer> typeValues = new ArrayList<>();

        for (MenuTypeEnum menuTypeEnum : values()) {
            if (types.contains(menuTypeEnum.type)) {
                typeValues.add(menuTypeEnum.value);
            }
        }

        return typeValues;
    }
}
