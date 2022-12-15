package fan.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fan.dao.LoginGeoDAO;
import fan.entity.LoginGeoDO;
import fan.query.LoginGeoQuery;
import fan.service.LoginGeoService;
import fan.utils.AuthMapStruct;
import fan.utils.CommonUtil;
import fan.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 登录地理信息实现类
 *
 * @author Fan
 * @since 2022/12/14 17:07
 */
@Service
public class LoginGeoServiceImpl implements LoginGeoService {

    @Resource
    private LoginGeoDAO loginGeoDAO;

    @Resource
    private AuthMapStruct authMapStruct;

    @Override
    public LoginGeoDO getLoginGeo(String username) {
        LambdaQueryWrapper<LoginGeoDO> loginGeoDOQueryWrapper = new LambdaQueryWrapper<>();
        loginGeoDOQueryWrapper.eq(LoginGeoDO::getUsername, username);

        return loginGeoDAO.selectOne(loginGeoDOQueryWrapper);
    }

    @Override
    public void addLoginGeo(LoginGeoDO loginGeo) {
        loginGeo.setId(UUID.randomUUID().toString());
        loginGeo.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        loginGeo.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

        loginGeoDAO.insert(loginGeo);
    }

    @Override
    public void updateLoginGeo(LoginGeoDO loginGeo) {
        loginGeo.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

        LambdaUpdateWrapper<LoginGeoDO> loginGeoDOUpdateWrapper = new LambdaUpdateWrapper<>();
        loginGeoDOUpdateWrapper.eq(LoginGeoDO::getUsername, loginGeo.getUsername());

        loginGeoDAO.update(loginGeo, loginGeoDOUpdateWrapper);
    }

    @Override
    public Result pageLoginGeos(LoginGeoQuery loginGeoQuery) {
        LambdaQueryWrapper<LoginGeoDO> loginGeoDOQueryWrapper = new LambdaQueryWrapper<>();
        loginGeoDOQueryWrapper.like(CommonUtil.isNotBlank(loginGeoQuery.getUsername()), LoginGeoDO::getUsername, loginGeoQuery.getUsername())
                .like(CommonUtil.isNotBlank(loginGeoQuery.getIpAddress()), LoginGeoDO::getIpAddress, loginGeoQuery.getIpAddress())
                .orderByAsc(LoginGeoDO::getUpdateTime);

        Page<LoginGeoDO> page = new Page<>(loginGeoQuery.getCurrentPage(), loginGeoQuery.getPageSize());
        Page<LoginGeoDO> loginGeoPage = loginGeoDAO.selectPage(page, loginGeoDOQueryWrapper);

        return Result.success("分页获取登录地理信息成功", authMapStruct.pageLoginGeoDOToVO(loginGeoPage));
    }
}
