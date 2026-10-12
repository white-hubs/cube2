package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.ExecuteRecordPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskExecuteRecordService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.KeyPlaceExecuteRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 执行记录
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class BimDisinfectionExecuteRecordController {
    private final DisinfectionTaskExecuteRecordService disinfectionTaskExecuteRecordService;

    /**
     * 重点场所的执行记录
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/bim/disinfection/execute-record/page")
    public Pagination<KeyPlaceExecuteRecordVO> KeyPlaceExecuteRecordPage(ExecuteRecordPageParam param) {
        return disinfectionTaskExecuteRecordService.KeyPlaceExecuteRecordPage(param);
    }

    /**
     * 获取重点场所已完成执行记录条数
     *
     * @param keyPlaceId 重点场所id
     *
     * @return 总条数
     */
    @GetMapping("/bim/disinfection/execute-record/key-place-count")
    public Integer keyPlaceExecuteCount(Long keyPlaceId) {
        return disinfectionTaskExecuteRecordService.keyPlaceExecuteCount(keyPlaceId);
    }
}
