package com.guhecloud.cube.srv.gen.device.supervise.server.account.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.interfaces.vo.DeviceLifeEventsVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceLifeEventsPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.DeviceLifeEventsQueryService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.query.DeviceLifeEventsQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备生命事件 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-06-20
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceLifeEventsController {

    private final DeviceLifeEventsQueryService queryService;

    // 分页查询
    @GetMapping("/device-life-events/page")
    public Pagination<DeviceLifeEventsVO> page(DeviceLifeEventsQuery query) {
        return queryService.getPage(query);
    }

    // 事件类型
    @GetMapping("/device-life-events/types")
    public Map<String, String> getTypes() {
        return queryService.getEventTypeMap();
    }

    @GetMapping("/device/{deviceId}/life-events/types")
    public Map<String, String> getEventTypesByDeviceId(@PathVariable("deviceId") Long deviceId) {
        List<DeviceLifeEventsPO> pos = queryService.lambdaQuery()
                .select(DeviceLifeEventsPO::getEventType, DeviceLifeEventsPO::getEventTypeName)
                .eq(DeviceLifeEventsPO::getDeviceId, deviceId)
                .ne(DeviceLifeEventsPO::getEventType, StringUtils.EMPTY_STRING)
                .ne(DeviceLifeEventsPO::getEventTypeName, StringUtils.EMPTY_STRING)
                .groupBy(DeviceLifeEventsPO::getEventType, DeviceLifeEventsPO::getEventTypeName)
                .list();

        return pos.stream().collect(Collectors.toMap(DeviceLifeEventsPO::getEventType, DeviceLifeEventsPO::getEventTypeName));
    }

    /**
     * 总数
     * 时间
     * 应用名称
     * 事件名称
     * 状态
     *
     * @param deviceInstanceId
     */
    @GetMapping("/device-life-events/{deviceInstanceId}/page")
    public Pagination<DeviceLifeEventsVO> pageByDeviceId(@PathVariable("deviceInstanceId") Long deviceInstanceId, Paging paging) {
        Page<DeviceLifeEventsPO> page = queryService.page(PagingUtils.convert(paging),
                                                          Wrappers.<DeviceLifeEventsPO>lambdaQuery()
                                                                  .eq(DeviceLifeEventsPO::getDeviceId, deviceInstanceId)
                                                                  .orderByDesc(DeviceLifeEventsPO::getEventTime)
                                                         );

        List<DeviceLifeEventsPO> records = page.getRecords();
        List<DeviceLifeEventsVO> deviceLifeEventsVOS = ObjectUtils.copying(records, DeviceLifeEventsVO.class);
        return new Pagination<>(page.getTotal(), page.getPages(), deviceLifeEventsVOS);
    }

}
