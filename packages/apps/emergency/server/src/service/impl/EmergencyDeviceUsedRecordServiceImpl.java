package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyDeviceUsedRecordMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyDeviceUsedRecord;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyDeviceUsedRecordParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyDeviceUsedRecordQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyDeviceUsedRecordService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyDeviceUsedRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：应急设备使用记录服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyDeviceUsedRecordServiceImpl extends ServiceImpl<EmergencyDeviceUsedRecordMapper, EmergencyDeviceUsedRecord> implements EmergencyDeviceUsedRecordService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EmergencyDeviceUsedRecordParam param) {
        EmergencyDeviceUsedRecord emergencyDeviceUsedRecord = ObjectUtils.copying(param, EmergencyDeviceUsedRecord.class);
        this.baseMapper.insert(emergencyDeviceUsedRecord);
        return emergencyDeviceUsedRecord.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EmergencyDeviceUsedRecordParam param) {
        EmergencyDeviceUsedRecord emergencyDeviceUsedRecord = this.baseMapper.selectById(id);
        Assert.isNotFound(emergencyDeviceUsedRecord, "未查询到相关信息");
        ObjectUtils.copying(param, emergencyDeviceUsedRecord);
        this.baseMapper.updateById(emergencyDeviceUsedRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public EmergencyDeviceUsedRecordVO getById(Long id) {
        EmergencyDeviceUsedRecord emergencyDeviceUsedRecord = this.baseMapper.selectById(id);
        return ObjectUtils.copying(emergencyDeviceUsedRecord, EmergencyDeviceUsedRecordVO.class);
    }

    @Override
    public Pagination<EmergencyDeviceUsedRecordVO> getPage(EmergencyDeviceUsedRecordQueryParam param) {
        //请自行添加查询条件
        Page<EmergencyDeviceUsedRecord> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, EmergencyDeviceUsedRecordVO.class);
    }
}