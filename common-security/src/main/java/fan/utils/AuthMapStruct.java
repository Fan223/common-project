package fan.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fan.entity.LoginGeoDO;
import fan.vo.LoginGeoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 权限转换类
 *
 * @author Fan
 * @since 2022/12/14 17:03
 */
@Mapper(uses = { MapStructRule.class }, componentModel = "spring")
public interface AuthMapStruct {

    @Mapping(target = "updateTime", source = "updateTime", qualifiedByName = "stampToString")
    LoginGeoVO LoginGeoDOToVO(LoginGeoDO loginGeoDO);

    Page<LoginGeoVO> pageLoginGeoDOToVO(Page<LoginGeoDO> loginGeoDOPage);
}
