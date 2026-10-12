package com.guhecloud.cube.biz.linkage.server.record.application.querygateway;

import com.guhecloud.cube.biz.linkage.server.record.application.querygateway.query.EventRecordQuery;
import com.guhecloud.cube.biz.linkage.server.record.infrastructure.repository.mapper.LinkageEventRecordMapper;
import com.guhecloud.cube.biz.linkage.server.record.interfaces.vo.EventAssistConfirmVO;
import com.guhecloud.cube.biz.linkage.server.record.interfaces.vo.EventRecordVO;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.po.EventAiResultPO;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.repository.EventAiResultService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventRecordQueryService {

    private final LinkageEventRecordMapper linkageEventRecordMapper;

    private final EventAiResultService eventAiResultService;

    public Pagination<EventRecordVO> queryPage(EventRecordQuery query) {
        var eventRecordVOPage = this.linkageEventRecordMapper.selectEventRecordPage(PagingUtils.convert(query), query);
        fillAssistConfirmResult(eventRecordVOPage.getRecords(), query);
        return PagingUtils.convert(eventRecordVOPage);
    }

    public EventRecordVO queryById(Long id) {
        return this.linkageEventRecordMapper.selectEventRecordById(id);
    }

    private void fillAssistConfirmResult(List<EventRecordVO> records, EventRecordQuery query) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        var eventRecordIds = records.stream().map(EventRecordVO::getId).toList();
        var assistConfirmResult = query == null ? null : query.getAssistConfirmResult();
        var eventAiResults = this.eventAiResultService.lambdaQuery()
                .in(EventAiResultPO::getEventRecordId, eventRecordIds)
                .eq(assistConfirmResult != null && !assistConfirmResult.isEmpty(),
                    EventAiResultPO::getResult, assistConfirmResult)
                .orderByDesc(EventAiResultPO::getId)
                .list();
        if (CollectionUtils.isEmpty(eventAiResults)) {
            return;
        }
        var resultMap = eventAiResults.stream()
                .collect(Collectors.toMap(EventAiResultPO::getEventRecordId, EventAiResultPO::getResult,
                                          (existingResult, ignoredResult) -> existingResult));
        records.forEach(record -> {
            var result = resultMap.get(record.getId());
            if (result != null) {
                record.setAssistConfirmResult(new EventAssistConfirmVO(result));
            }
        });
    }
}
