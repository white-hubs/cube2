package com.guhecloud.cube.onecard.server.consumption.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.onecard.server.consumption.dao.AmountChangeRecordMapper;
import com.guhecloud.cube.onecard.server.consumption.entity.AmountChangeRecord;
import com.guhecloud.cube.onecard.server.consumption.enumeration.TransactionChannelEnum;
import com.guhecloud.cube.onecard.server.consumption.enumeration.TransactionStatusEnum;
import com.guhecloud.cube.onecard.server.consumption.enumeration.TransactionTypeEnum;
import com.guhecloud.cube.onecard.server.consumption.param.AmountRecordQueryParam;
import com.guhecloud.cube.onecard.server.consumption.service.AmountChangeRecordService;
import com.guhecloud.cube.onecard.server.consumption.vo.AmountChangeRecordVo;
import com.guhecloud.cube.onecard.server.consumption.vo.AmountChangeVo;
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
public class AmountChangeRecordServiceImpl extends ServiceImpl<AmountChangeRecordMapper, AmountChangeRecord> implements AmountChangeRecordService {

    private final AmountChangeRecordMapper amountChangeRecordMapper;
    String startTime = "";
    String endTime = "";

    @Override
    public Pagination<AmountChangeRecordVo> getAmountChangeRecord(AmountRecordQueryParam param) {

        if (param != null && param.getPeriod() != null && !param.getPeriod().equals("")) {
            String[] split = param.getPeriod().split(",");
            startTime = split[0];
            endTime = split[1];
        }
        Page<AmountChangeRecordVo> page = new Page(param.getPageNumber(), param.getPageSize());
        Page<AmountChangeRecordVo> amountChangeRecordVoList = amountChangeRecordMapper.getAmountChangeRecord(param, startTime, endTime, page);
        if (amountChangeRecordVoList != null) {
            List<AmountChangeRecordVo> records = amountChangeRecordVoList.getRecords().stream()
                    .map(t -> {
                        AmountChangeRecordVo amountChangeRecordVo = new AmountChangeRecordVo();
                        ObjectUtils.copying(t, amountChangeRecordVo);
                        if (StringUtils.isNotBlank(t.getTransactionType())) {
                            amountChangeRecordVo.setTransactionType(TransactionTypeEnum.getName(Integer.valueOf(t.getTransactionType())));
                        }
                        if (StringUtils.isNotBlank(t.getTransactionChannel())) {
                            amountChangeRecordVo.setTransactionChannel(TransactionChannelEnum.getValue(Integer.valueOf(t.getTransactionChannel())));
                        }
                        if (StringUtils.isNotBlank(t.getTransactionStatus())) {
                            amountChangeRecordVo.setTransactionStatus(TransactionStatusEnum.getValue(Integer.valueOf(t.getTransactionStatus())));
                        }
                        return amountChangeRecordVo;
                    }).collect(Collectors.toList());
            amountChangeRecordVoList.setRecords(records);
        }
        Pagination<AmountChangeRecordVo> convert = PagingUtils.convert(amountChangeRecordVoList, AmountChangeRecordVo.class);
        return convert;

    }

    @Override
    public AmountChangeVo getAmountClassification(AmountRecordQueryParam param) {
        if (param != null && param.getPeriod() != null && !param.getPeriod().equals("")) {
            String[] split = param.getPeriod().split(",");
            startTime = split[0];
            endTime = split[1];
        }
        List<Map> list = amountChangeRecordMapper.getAmountClassification(param, startTime, endTime);
        AmountChangeVo amountChangeVo = new AmountChangeVo();
        if (!list.isEmpty()) {
            for (Map map : list) {
                amountChangeVo.setMap(map);
            }
        }
        return amountChangeVo;
    }
}
