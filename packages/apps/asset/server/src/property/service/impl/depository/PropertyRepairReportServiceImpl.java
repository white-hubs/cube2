package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyDetailedMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyRepairReportMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyRepairReport;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyRepairReportService;
import com.guhecloud.platform.base.util.ObjectUtils;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-28  11:24
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyRepairReportServiceImpl extends ServiceImpl<PropertyRepairReportMapper, PropertyRepairReport> implements PropertyRepairReportService {
    private final PropertyDetailedMapper propertyDetailedMapper;

    @Override
    public Boolean insert(ProcessStartedMessage message, Long propertyId) {
        PropertyRepairReport repairReport = new PropertyRepairReport();
        PropertyRepairReport repair = this.baseMapper.selectOne(new LambdaQueryWrapper<PropertyRepairReport>()
                                                                        .eq(PropertyRepairReport::getPropertyId, propertyId)
                                                                        .orderByDesc(PropertyRepairReport::getCreateTime)
                                                                        .last("limit 1"));
        if (ObjectUtils.isEmpty(repair)) {
            PropertyDetailed propertyDetailed = propertyDetailedMapper.selectById(propertyId);
            repairReport.setPropertyStatus(propertyDetailed.getStatus());
        } else {
            repairReport.setPropertyStatus(repair.getPropertyStatus());
        }
        repairReport.setStatus("ACTIVE");
        repairReport.setProcessInstanceId(message.getProcessInstanceId());
        repairReport.setPropertyId(propertyId);
        return this.baseMapper.insert(repairReport) > 0;
    }

    @Override
    public String selectRepairReport(Long propertyId, String processInstanceId) {
        boolean bool = this.update().set("status", "FINISHED").eq("process_instance_id", processInstanceId).update();
        if (bool) {
            List<PropertyRepairReport> repairReport = this.baseMapper.selectList(new LambdaQueryWrapper<PropertyRepairReport>()
                                                                                         .eq(PropertyRepairReport::getPropertyId, propertyId)
                                                                                         .eq(PropertyRepairReport::getStatus, "ACTIVE"));
            if (ObjectUtils.isEmpty(repairReport)) {
                PropertyRepairReport repair = this.baseMapper.selectOne(new LambdaQueryWrapper<PropertyRepairReport>()
                                                                                .eq(PropertyRepairReport::getProcessInstanceId, processInstanceId)
                                                                                .last("limit 1"));
                log.info("资产：{}；报修完成。还原状态：{}", propertyId, repair.getPropertyStatus());
                return repair.getPropertyStatus();

            }
        }
        log.info("资产：{}；还有报修未完成", propertyId);
        return "";

    }

    public Boolean updateRepairReport(Long propertyId, String propertyStatus) {
        return this.update().set("property_status", propertyStatus)
                .eq("property_id", propertyId)
                .update();
    }
}
