package com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao.PlayRecordMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.PlayRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.PlayRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.PlayRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.PlayRecordPageVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayRecordServiceImpl extends ServiceImpl<PlayRecordMapper, PlayRecord> implements PlayRecordService {

    private final PlayRecordMapper playRecordMapper;

    @Override
    public Pagination<PlayRecordPageVo> getPlayRecordPage(PlayRecordParam param) {
        Page<PlayRecordPageVo> playRecordPage = playRecordMapper.getPlayRecordPage(PagingUtils.convert(param), param);
        return PagingUtils.convert(playRecordPage);
    }

    @Override
    public Long getPlayRecordCount(PlayRecordParam param) {
        Period period = param.getPeriod();
        return playRecordMapper.selectCount(Wrappers.<PlayRecord>lambdaQuery()
                                                    .between(period !=
                                                             null, PlayRecord::getStartTime, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd())));
    }
}
