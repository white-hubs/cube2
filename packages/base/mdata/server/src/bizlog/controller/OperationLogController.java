package com.guhecloud.cube.biz.mdata.server.bizlog.controller;

import com.guhecloud.cube.biz.mdata.server.bizlog.param.OperationLogParam;
import com.guhecloud.cube.biz.mdata.server.bizlog.service.OperationLogService;
import com.guhecloud.cube.biz.mdata.server.bizlog.vo.OperationLogVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统操作日志
 *
 * @Author: lijie
 * @CreateTime: 2024-11-28  14:57
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OperationLogController {
    private final OperationLogService operationLogService;

    /**
     * 系统操作日志分页查询
     */
    @GetMapping("/operation/log/record/page")
    public Pagination<OperationLogVO> queryLogRecordPage(OperationLogParam param) {
        return operationLogService.queryLogRecordPage(param);
    }
}
