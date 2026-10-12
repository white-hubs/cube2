package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.querygateway.query.ThirdFusionFileQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionFilePO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.infrastructure.repository.mapper.ThirdFusionFileMapper;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class ThirdFusionFileService extends ServiceImpl<ThirdFusionFileMapper, ThirdFusionFilePO> {

    @Transactional(rollbackFor = Exception.class)
    public Long saveThirdFusionFile(ThirdFusionFilePO thirdFusionFilePO) {
        this.baseMapper.insert(thirdFusionFilePO);
        return thirdFusionFilePO.getId();
    }

    public Page<ThirdFusionFilePO> queryThirdFusionFilePage(ThirdFusionFileQueryParam param) {
        return this.baseMapper.selectPage(PagingUtils.convert(param), Wrappers.<ThirdFusionFilePO>lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getType()), ThirdFusionFilePO::getType, param
                        .getType())
                .between(Objects.nonNull(param.getPeriod()), ThirdFusionFilePO::getCreateTime, param.getPeriod().getBegin(), param
                        .getPeriod().getEnd())
                .and(StringUtils.isNotBlank(param.getKeyword()), q -> q
                        .like(ThirdFusionFilePO::getCalleeName, param.getKeyword())
                        .or()
                        .like(ThirdFusionFilePO::getCallerName, param.getKeyword())
                    )
                .orderByDesc(ThirdFusionFilePO::getCreateTime));
    }

}
