package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.querygateway.BroadcastPlayRecordQueryService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.querygateway.query.BroadcastPlayRecordQuery;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces.vo.BroadcastPlayRecordPageVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 广播记录
 *
 * @author lanchun
 * @date 2022/12/13 9:56
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastPlayRecordController {

    private final BroadcastPlayRecordQueryService broadcastPlayRecordQueryService;

    /**
     * 播放记录分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/broadcast-play-record/page")
    public Pagination<BroadcastPlayRecordPageVO> getPage(@Valid BroadcastPlayRecordQuery query) {
        return this.broadcastPlayRecordQueryService.getPage(query);
    }
}
