package com.guhecloud.cube.biz.smartcard.server.service.impl;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.smartcard.server.dao.AccessEventMapper;
import com.guhecloud.cube.biz.smartcard.server.entity.AccessEvent;
import com.guhecloud.cube.biz.smartcard.server.service.AccessEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("smartcard_access_event_service")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccessEventServiceImpl extends ServiceImpl<AccessEventMapper, AccessEvent> implements AccessEventService {
    @Override
    public void insertOrUpdate(AccessEvent event) {
        try {
            AccessEvent temp = this.baseMapper.selectOne(Wrappers.<AccessEvent>lambdaQuery()
                                                                 .eq(AccessEvent::getDeviceCode, event.getDeviceCode())
                                                                 .eq(AccessEvent::getEventTime, event.getEventTime())
                                                                 .eq(AccessEvent::getTenant, event.getTenant())
                                                                 .eq(StringUtils.isNotBlank(event.getPersonId()), AccessEvent::getPersonId, event.getPersonId()));
            if (temp != null) {
                temp.setUpdateTime(event.getUpdateTime());
                this.baseMapper.updateById(temp);
            } else {
                this.baseMapper.insert(event);
            }
        } catch (Exception e) {
            log.error("门禁系统访问记录同步异常：", e);
        }
    }
}
