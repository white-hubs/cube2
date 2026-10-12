package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencySuppliesInoutRecordMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencySuppliesInoutRecord;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesInoutRecordParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesInoutRecordQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencySuppliesInoutRecordService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesInoutRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：应急物资出入库记录服务层
 *
 * @author Appet
 * @date 2024-01-016 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencySuppliesInoutRecordServiceImpl extends ServiceImpl<EmergencySuppliesInoutRecordMapper, EmergencySuppliesInoutRecord> implements EmergencySuppliesInoutRecordService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EmergencySuppliesInoutRecordParam param) {
        EmergencySuppliesInoutRecord emergencySuppliesInoutRecord = ObjectUtils.copying(param, EmergencySuppliesInoutRecord.class);
        this.baseMapper.insert(emergencySuppliesInoutRecord);
        return emergencySuppliesInoutRecord.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EmergencySuppliesInoutRecordParam param) {
        EmergencySuppliesInoutRecord emergencySuppliesInoutRecord = this.baseMapper.selectById(id);
        Assert.isNotFound(emergencySuppliesInoutRecord, "未查询到相关信息");
        ObjectUtils.copying(param, emergencySuppliesInoutRecord);
        this.baseMapper.updateById(emergencySuppliesInoutRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public EmergencySuppliesInoutRecordVO getById(Long id) {
        EmergencySuppliesInoutRecord emergencySuppliesInoutRecord = this.baseMapper.selectById(id);
        return ObjectUtils.copying(emergencySuppliesInoutRecord, EmergencySuppliesInoutRecordVO.class);
    }

    @Override
    public Pagination<EmergencySuppliesInoutRecordVO> getPage(EmergencySuppliesInoutRecordQueryParam param) {
        //请自行添加查询条件
        Period period = param.getPeriod();
        Page<EmergencySuppliesInoutRecord> page = this.lambdaQuery()
                .eq(param.getSuppliesId() != null, EmergencySuppliesInoutRecord::getSuppliesId, param.getSuppliesId())
                .eq(StringUtils.isNotBlank(param.getInoutType()), EmergencySuppliesInoutRecord::getInoutType, param.getInoutType())
                .like(StringUtils.isNotBlank(param.getInoutTag()), EmergencySuppliesInoutRecord::getInoutTag, param.getInoutTag())
                .between(period !=
                         null, EmergencySuppliesInoutRecord::getInoutTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, EmergencySuppliesInoutRecordVO.class);
    }
}