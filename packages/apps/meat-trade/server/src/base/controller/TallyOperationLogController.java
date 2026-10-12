package com.guhecloud.cube.biz.meattrade.server.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.base.entity.TallyOperationLog;
import com.guhecloud.cube.biz.meattrade.server.base.param.TallyOperationLogCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.TallyOperationLogQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.TallyOperationLogService;
import com.guhecloud.cube.biz.meattrade.server.base.vo.TallyOperationLogVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 理货机运行日志
 *
 * @author lzy
 * @date 2021/9/9
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TallyOperationLogController {

    private final TallyOperationLogService tallyOperationLogService;

    /**
     * 理货机运行日志新增
     *
     * @param param 新增参数
     */
    @PostMapping("/tally-operation-log")
    public void save(@Valid @RequestBody TallyOperationLogCreateParam param) {
        this.tallyOperationLogService.save(param);
    }

    /**
     * 理货机运行日志分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 理货机运行日志信息
     */
    @GetMapping("/tally-operation-log")
    public Pagination<TallyOperationLogVO> page(TallyOperationLogQueryParam queryParam) {
        Page<TallyOperationLog> page = this.tallyOperationLogService.page(queryParam);
        List<TallyOperationLogVO> pageVOS = ObjectUtils.copying(page.getRecords(), TallyOperationLogVO.class);
        return PagingUtils.convert(page, pageVOS);
    }
}
