package com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.dao.DeviceLifeEventsMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.interfaces.vo.DeviceLifeEventsVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceBusinessUrlPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceLifeEventsPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.DeviceBusinessUrlQueryService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.DeviceLifeEventsQueryService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.query.DeviceLifeEventsQuery;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceLifeEventsQueryServiceImpl extends ServiceImpl<DeviceLifeEventsMapper, DeviceLifeEventsPO> implements DeviceLifeEventsQueryService {

    private final DeviceBusinessUrlQueryService businessUrlQueryService;

    @Override
    public Pagination<DeviceLifeEventsVO> getPage(DeviceLifeEventsQuery query) {
        Integer tenant = SessionContextHolder.getRequesterProperty(Requester::getTenant);
        Page<DeviceLifeEventsPO> page = this.lambdaQuery()
                .eq(DeviceLifeEventsPO::getTenant, tenant)
                .eq(null != query.getDeviceId(), DeviceLifeEventsPO::getDeviceId, query.getDeviceId())
                .eq(StringUtils.isNotEmpty(query.getEventType()), DeviceLifeEventsPO::getEventType, query.getEventType())
                .between(query.getPeriod() !=
                         null, DeviceLifeEventsPO::getEventTime, ObjectUtils.ifNull(query.getPeriod(), Period::getBegin), ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .orderByDesc(DeviceLifeEventsPO::getEventTime)
                .page(PagingUtils.convert(query));

        List<DeviceLifeEventsVO> vos = ObjectUtils.copying(page.getRecords(), DeviceLifeEventsVO.class);
        this.extendBusinessUrl(vos);

        return PagingUtils.convert(page, vos);
    }

    @Override
    public Map<String, String> getEventTypeMap() {
        List<DeviceLifeEventsPO> pos = this.lambdaQuery()
                .select(DeviceLifeEventsPO::getEventType, DeviceLifeEventsPO::getEventTypeName)
                .ne(DeviceLifeEventsPO::getEventType, StringUtils.EMPTY_STRING)
                .ne(DeviceLifeEventsPO::getEventTypeName, StringUtils.EMPTY_STRING)
                .groupBy(DeviceLifeEventsPO::getEventType)
                .list();

        return pos.stream().collect(Collectors.toMap(DeviceLifeEventsPO::getEventType, DeviceLifeEventsPO::getEventTypeName));
    }

    private void extendBusinessUrl(List<DeviceLifeEventsVO> vos) {
        Set<String> eventTypes = vos.stream().map(DeviceLifeEventsVO::getEventType).collect(Collectors.toSet());

        List<DeviceBusinessUrlPO> urlPOS = businessUrlQueryService.listByBusinessTypes(eventTypes, SessionContextHolder.getRequester()
                .getTerminal());

        Map<String, DeviceBusinessUrlPO> urlPOMap = urlPOS.stream()
                .collect(Collectors.toMap(DeviceBusinessUrlPO::getBusinessType, Function.identity()));

        vos.parallelStream().forEach(vo -> {
            DeviceBusinessUrlPO businessUrlPO = urlPOMap.getOrDefault(vo.getEventType(), null);
            if (businessUrlPO != null) {
                vo.setUrl(businessUrlPO.getUrl());
            }
        });

    }
}
