package fan.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fan.bo.UserBO;
import fan.bo.UserRoleBO;
import fan.command.MenuCommand;
import fan.command.RoleCommand;
import fan.command.UserCommand;
import fan.entity.MenuDO;
import fan.entity.RoleDO;
import fan.entity.UserDO;
import fan.entity.UserRoleDO;
import fan.vo.MenuVO;
import fan.vo.RoleVO;
import fan.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 系统管理转换类
 *
 * @author Fan
 * @since 2022/11/22 16:09
 */
@Mapper(uses = { MapStructRule.class }, componentModel = "spring")
public interface SystemMapStruct {

    UserBO userDOToBO(UserDO userDO);

    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "stampToString")
    RoleVO roleDOToVO(RoleDO roleDO);

    Page<RoleVO> pageRoleDOToVO(Page<RoleDO> roleDOPage);

    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "stampToString")
    MenuVO menuDOToVO(MenuDO menuDO);

    MenuDO menuCommandToDO(MenuCommand menuCommand);

    RoleDO roleCommandToDO(RoleCommand roleCommand);

    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "stampToString")
    UserVO userDOToVO(UserDO userDO);

    Page<UserVO> pageUserDOToVO(Page<UserDO> userDOPage);

    UserDO userCommandToDO(UserCommand userCommand);

    UserRoleBO UserRoleDOToBO(UserRoleDO userRoleDO);

    List<UserRoleBO> userRoleDOSToBOs(List<UserRoleDO> userRoleDOS);
}
