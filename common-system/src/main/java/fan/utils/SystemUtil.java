package fan.utils;

import fan.vo.MenuVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统模块工具类
 *
 * @author Fan
 * @since 2022/11/28 16:45
 */
public class SystemUtil {

    /**
     * 将菜单列表转为树形列表
     *
     * @param menuVOS 菜单列表
     * @return {@link List<MenuVO>}
     * @author Fan
     * @since 2022/11/27 6:40
     */
    public static List<MenuVO> buildTree(List<MenuVO> menuVOS) {
        List<MenuVO> menusTree = new ArrayList<>(menuVOS.size());

        // 先将菜单列表转为 Map, Key 为ID
        Map<String, MenuVO> menuVOMap = menuVOS.stream().collect(Collectors.toMap(MenuVO::getId, menuVO -> menuVO));

        // 循环菜单列表
        for (MenuVO menuVO : menuVOS) {
            // 判断当前菜单是否有父菜单
            MenuVO parentMenuVO = menuVOMap.get(menuVO.getParentId());

            // 父菜单为空则当前菜单为顶级菜单, 直接加入结果列表
            if (null == parentMenuVO) {
                menusTree.add(menuVO);
                continue;
            }

            // 不为空则将当前菜单添加到获取到的父菜单的子菜单
            parentMenuVO.getChildren().add(menuVO);
        }

        return menusTree;
    }
}
