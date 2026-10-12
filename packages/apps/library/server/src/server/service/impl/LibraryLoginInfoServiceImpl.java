package com.guhecloud.cube.srv.gen.library.management.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.library.management.server.mapper.LibraryLoginInfoMapper;
import com.guhecloud.cube.srv.gen.library.management.server.po.SystemLoginInfoPO;
import com.guhecloud.cube.srv.gen.library.management.server.service.LibraryLoginInfoService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LibraryLoginInfoServiceImpl extends ServiceImpl<LibraryLoginInfoMapper, SystemLoginInfoPO> implements LibraryLoginInfoService {

    private final LibraryLoginInfoMapper libraryLoginInfoMapper;

    /**
     * 获取图书管理的登录信息
     *
     * @return 登录信息
     */
    @Override
    public SystemLoginInfoPO getLoginInfoByLibrary() {
        SystemLoginInfoPO loginInfoPO = libraryLoginInfoMapper.getLoginInfoByLibrary();
        if (loginInfoPO == null || StringUtils.isBlank(loginInfoPO.getAccessToken()) || StringUtils.isBlank(loginInfoPO.getBaseUrl())) {
            throw new ServiceException("鉴权信息不存在");
        }
        return loginInfoPO;
    }
}
