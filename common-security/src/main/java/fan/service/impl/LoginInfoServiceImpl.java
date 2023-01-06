package fan.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fan.dao.LoginInfoDAO;
import fan.entity.LoginInfoDO;
import fan.query.LoginInfoQuery;
import fan.service.LoginInfoService;
import fan.utils.AuthMapStruct;

import fan.base.Response;
import fan.utils.collection.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 登录信息实现类
 *
 * @author Fan
 * @since 2022/12/14 17:07
 */
@Service
public class LoginInfoServiceImpl implements LoginInfoService {

    @Resource
    private LoginInfoDAO loginInfoDAO;

    @Resource
    private AuthMapStruct authMapStruct;

    @Override
    public LoginInfoDO getLoginInfo(String username) {
        LambdaQueryWrapper<LoginInfoDO> loginInfoDOQueryWrapper = new LambdaQueryWrapper<>();
        loginInfoDOQueryWrapper.eq(LoginInfoDO::getUsername, username);

        return loginInfoDAO.selectOne(loginInfoDOQueryWrapper);
    }

    @Override
    public void addLoginInfo(LoginInfoDO loginInfoDO) {
        loginInfoDO.setId(UUID.randomUUID().toString());
        loginInfoDO.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        loginInfoDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

        loginInfoDAO.insert(loginInfoDO);
    }

    @Override
    public void updateLoginInfo(LoginInfoDO loginInfoDO) {
        loginInfoDO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

        LambdaUpdateWrapper<LoginInfoDO> loginInfoDOUpdateWrapper = new LambdaUpdateWrapper<>();
        loginInfoDOUpdateWrapper.eq(LoginInfoDO::getUsername, loginInfoDO.getUsername());

        loginInfoDAO.update(loginInfoDO, loginInfoDOUpdateWrapper);
    }

    @Override
    public Response pageLoginInfos(LoginInfoQuery loginInfoQuery) {
        LambdaQueryWrapper<LoginInfoDO> loginInfoDOQueryWrapper = new LambdaQueryWrapper<>();
        loginInfoDOQueryWrapper.like(StringUtil.isNotBlank(loginInfoQuery.getUsername()), LoginInfoDO::getUsername, loginInfoQuery.getUsername())
                .like(StringUtil.isNotBlank(loginInfoQuery.getIpAddress()), LoginInfoDO::getIpAddress, loginInfoQuery.getIpAddress())
                .orderByAsc(LoginInfoDO::getUpdateTime);

        Page<LoginInfoDO> page = new Page<>(loginInfoQuery.getCurrentPage(), loginInfoQuery.getPageSize());
        Page<LoginInfoDO> loginInfoDOPage = loginInfoDAO.selectPage(page, loginInfoDOQueryWrapper);

        return Response.success("分页获取登录信息成功", authMapStruct.pageLoginInfoDOToVO(loginInfoDOPage));
    }
}
