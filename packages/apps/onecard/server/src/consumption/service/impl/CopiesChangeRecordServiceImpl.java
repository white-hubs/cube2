package com.guhecloud.cube.onecard.server.consumption.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.onecard.server.consumption.dao.CopiesChangeRecordMapper;
import com.guhecloud.cube.onecard.server.consumption.entity.CopiesChangeRecord;
import com.guhecloud.cube.onecard.server.consumption.enumeration.TransactionStatusEnum;
import com.guhecloud.cube.onecard.server.consumption.enumeration.TransactionTypeEnum;
import com.guhecloud.cube.onecard.server.consumption.param.CopiesRecordQueryParam;
import com.guhecloud.cube.onecard.server.consumption.service.CopiesChangeRecordService;
import com.guhecloud.cube.onecard.server.consumption.vo.CopiesChangeRecordVo;
import com.guhecloud.cube.onecard.server.consumption.vo.CopiesChangeVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CopiesChangeRecordServiceImpl extends ServiceImpl<CopiesChangeRecordMapper, CopiesChangeRecord> implements CopiesChangeRecordService {

    private final CopiesChangeRecordMapper copiesChangeRecordMapper;

    @Override
    public Pagination<CopiesChangeRecordVo> getCopiesChangeRecord(CopiesRecordQueryParam param) {
        String startTime = "";
        String endTime = "";
        if (param != null && param.getPeriod() != null && !param.getPeriod().equals("")) {
            String[] split = param.getPeriod().split(",");
            startTime = split[0];
            endTime = split[1];
        }
        Page<CopiesChangeRecordVo> page = new Page(param.getPageNumber(), param.getPageSize());
        Page<CopiesChangeRecordVo> copiesChangeRecord = copiesChangeRecordMapper.getCopiesChangeRecord(param, startTime, endTime, page);
        if (copiesChangeRecord != null) {
            List<CopiesChangeRecordVo> records = copiesChangeRecord.getRecords().stream()
                    .map(t -> {
                        CopiesChangeRecordVo copiesChangeRecordVo = new CopiesChangeRecordVo();
                        ObjectUtils.copying(t, copiesChangeRecordVo);
                        if (StringUtils.isNotBlank(t.getTransactionStatus())) {
                            copiesChangeRecordVo.setTransactionStatus(TransactionStatusEnum.getValue(Integer.valueOf(t.getTransactionStatus())));
                        }
                        if (StringUtils.isNotBlank(t.getTransactionType())) {
                            copiesChangeRecordVo.setTransactionType(TransactionTypeEnum.getName(Integer.valueOf(t.getTransactionType())));
                        }
                        return copiesChangeRecordVo;
                    }).collect(Collectors.toList());
            copiesChangeRecord.setRecords(records);
        }
        Pagination<CopiesChangeRecordVo> convert = PagingUtils.convert(copiesChangeRecord, CopiesChangeRecordVo.class);
        return convert;
    }

    @Override
    public CopiesChangeVo getShareStatistics(CopiesRecordQueryParam param) {
        String startTime = "";
        String endTime = "";
        if (param != null && param.getPeriod() != null && !param.getPeriod().equals("")) {
            String[] split = param.getPeriod().split(",");
            startTime = split[0];
            endTime = split[1];
        }
        List<Map> list = copiesChangeRecordMapper.getShareStatistics(param, startTime, endTime);
        CopiesChangeVo copiesChangeVo = new CopiesChangeVo();
        if (!list.isEmpty()) {
            for (Map map : list) {
                copiesChangeVo.setMap(map);
            }
        }
        return copiesChangeVo;
    }
}
