package com.guhecloud.cube.biz.linkage.server.record.interfaces;

import com.guhecloud.cube.biz.linkage.server.record.application.querygateway.EventRecordQueryService;
import com.guhecloud.cube.biz.linkage.server.record.application.querygateway.query.EventRecordQuery;
import com.guhecloud.cube.biz.linkage.server.record.interfaces.vo.EventRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventRecordController {

    private final EventRecordQueryService eventRecordQueryService;

    /**
     * 事件记录列表
     *
     * @param query 查询条件
     *
     * @return 事件记录分页列表
     */
    @GetMapping("/event-records")
    public Pagination<EventRecordVO> getPage(EventRecordQuery query) {
        return eventRecordQueryService.queryPage(query);
    }

    /**
     * 事件记录详情
     *
     * @param id 主键
     *
     * @return 事件记录详情
     */
    @GetMapping("/event-records/{id}")
    public EventRecordVO getById(@PathVariable("id") Long id) {
        return eventRecordQueryService.queryById(id);
    }
}
