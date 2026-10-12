package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.ApplicationExtendMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationExtend;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationExtendInfo;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationExtendService;
import com.guhecloud.platform.base.util.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/5 10:41
 * @Description:
 */
@Service
public class ApplicationExtendServiceImpl extends ServiceImpl<ApplicationExtendMapper, ApplicationExtend> implements ApplicationExtendService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long id, ApplicationExtendInfo extendParam) {
        if (Objects.isNull(extendParam) || Objects.isNull(id)) {
            return;
        }
        Optional<ApplicationExtend> applicationExtendOptional = this.lambdaQuery().eq(ApplicationExtend::getId, id)
                .oneOpt();
        if (applicationExtendOptional.isPresent()) {
            extendParam.setId(id);
            ObjectUtils.copying(extendParam, applicationExtendOptional.get());
            this.updateById(applicationExtendOptional.get());
        } else {
            ApplicationExtend applicationExtend = ObjectUtils.copying(extendParam, ApplicationExtend.class);
            applicationExtend.setId(id);
            this.save(applicationExtend);
        }
    }
}
