package fan.service;

import fan.command.MenuCommand;
import fan.query.MenuQuery;
import fan.utils.Result;
import fan.vo.MenuVO;

import java.util.List;

/**
 * 菜单接口
 *
 * @author Fan
 * @since 2022/11/22 9:53
 */
public interface MenuService {

    /**
     * 获取菜单列表
     *
     * @param menuQuery 菜单查询参数
     * @return {@link List<MenuVO>}
     * @author Fan
     * @since 2022/11/27 6:35
     */
    List<MenuVO> listMenus(MenuQuery menuQuery);

    /**
     * 通过用户 ID 获取该用户的导航菜单列表
     *
     * @param userId 用户ID
     * @return {@link List<MenuVO>}
     * @author Fan
     * @since 2022/11/28 9:17
     */
    List<MenuVO> listNavMenusByUserId(String userId);

    /**
     * 获取菜单树形列表
     *
     * @param menuQuery 菜单查询参数
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/2 14:06
     */
    Result listMenusTree(MenuQuery menuQuery);

    /**
     * 添加菜单
     *
     * @param menuCommand 菜单更新参数
     * @return {@link int}
     * @author Fan
     * @since 2022/11/29 15:51
     */
    Result addMenu(MenuCommand menuCommand);

    /**
     * 修改菜单
     *
     * @param menuCommand 菜单更新参数
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/1 11:07
     */
    Result updateMenu(MenuCommand menuCommand);

    /**
     * 删除菜单
     *
     * @param menuCommand 菜单更新参数
     * @return {@link Result}
     * @author Fan
     * @since 2022/12/1 11:07
     */
    Result deleteMenu(MenuCommand menuCommand);
}
