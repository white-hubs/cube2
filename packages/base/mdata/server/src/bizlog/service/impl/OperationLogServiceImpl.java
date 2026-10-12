package com.guhecloud.cube.biz.mdata.server.bizlog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.bizlog.entity.OperationLog;
import com.guhecloud.cube.biz.mdata.server.bizlog.mapper.OperationLogMapper;
import com.guhecloud.cube.biz.mdata.server.bizlog.param.OperationLogParam;
import com.guhecloud.cube.biz.mdata.server.bizlog.service.OperationLogService;
import com.guhecloud.cube.biz.mdata.server.bizlog.vo.OperationLogVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: lijie
 * @CreateTime: 2024-11-28  15:04
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public Pagination<OperationLogVO> queryLogRecordPage(OperationLogParam param) {
        Period period = Optional.ofNullable(param.getPeriod()).orElse(new Period(DateUtils.now(), DateUtils.now()));
        Page<OperationLog> page = new Page(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<OperationLog>()
                .between(ObjectUtils.isNotEmpty(param.getPeriod()), OperationLog::getCreateTime, period.getBegin(), period.getEnd())
                .like(ObjectUtils.isNotEmpty(param.getUserName()), OperationLog::getUserName, param.getUserName())
                .eq(ObjectUtils.isNotEmpty(param.getMethod()), OperationLog::getMethod, param.getMethod())
                .eq(ObjectUtils.isNotEmpty(param.getResult()), OperationLog::getResult, param.getResult())
                .orderByDesc(OperationLog::getCreateTime));
        return PagingUtils.convert(page, OperationLogVO.class);
    }
}
