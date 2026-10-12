package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.querygateway;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.querygateway.query.DeviceNotifyRuleTriggerRecordQuery;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter.DeviceNotifyRuleTriggerRecordEntityTOVOConvert;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository.DeviceNotifyRuleTriggerRecordRepository;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo.DeviceNotifyRuleTriggerRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class DeviceNotifyRuleTriggerRecordQueryService {

    private final DeviceNotifyRuleTriggerRecordRepository deviceNotifyRuleTriggerRecordRepository;

    public DeviceNotifyRuleTriggerRecordQueryService(DeviceNotifyRuleTriggerRecordRepository deviceNotifyRuleTriggerRecordRepository
                                                    ) {this.deviceNotifyRuleTriggerRecordRepository = deviceNotifyRuleTriggerRecordRepository;}

    /**
     * 查询设备通知规则触发记录分页
     *
     * @param query 查询参数
     *
     * @return 设备通知规则触发记录分页
     */
    public Pagination<DeviceNotifyRuleTriggerRecordVO> getPage(DeviceNotifyRuleTriggerRecordQuery query) {
        var pagination = deviceNotifyRuleTriggerRecordRepository.findPage(query);
        return PagingUtils.convert(pagination, DeviceNotifyRuleTriggerRecordEntityTOVOConvert.deviceNotifyRuleTriggerRecordToVOS(pagination.getRecords()));
    }

    /**
     * 根据id查询设备通知规则触发记录
     *
     * @param id 设备通知规则触发记录id
     *
     * @return 设备通知规则触发记录
     */
    public DeviceNotifyRuleTriggerRecordVO getById(Long id) {
        var record = deviceNotifyRuleTriggerRecordRepository.findById(id);
        if (Objects.isNull(record)) {
            return null;
        }
        return DeviceNotifyRuleTriggerRecordEntityTOVOConvert.deviceNotifyRuleTriggerRecordToVO(record);
    }
}
